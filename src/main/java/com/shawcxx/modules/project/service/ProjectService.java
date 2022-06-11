package com.shawcxx.modules.project.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.device.bo.DeviceEnum;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceSensorDO;
import com.shawcxx.modules.device.service.DeviceSensorService;
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.modules.device.service.DeviceTemperatureService;
import com.shawcxx.modules.project.bo.AddressEnum;
import com.shawcxx.modules.project.bo.ProjectImportBO;
import com.shawcxx.modules.project.dao.ProjectDAO;
import com.shawcxx.modules.project.domain.AddressDO;
import com.shawcxx.modules.project.domain.ProjectDO;
import com.shawcxx.modules.project.dto.ProjectAddressDTO;
import com.shawcxx.modules.project.dto.ProjectDTO;
import com.shawcxx.modules.project.dto.ProjectListDTO;
import com.shawcxx.modules.project.form.ProjectForm;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjl
 * @date 2022/5/7 9:58
 * @description
 */
@Service
@Slf4j
public class ProjectService extends ServiceImpl<ProjectDAO, ProjectDO> {
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private AddressService addressService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceSensorService deviceSensorService;
    @Resource
    private DeviceTemperatureService deviceTemperatureService;

    public List<ProjectListDTO> projectList() {
        List<ProjectListDTO> list = new ArrayList<>();
        Long deptId = MyUserUtil.getDeptId();
        LambdaQueryWrapper<SysDeptDO> deptQueryWrapper = new LambdaQueryWrapper<>();
        deptQueryWrapper.eq(deptId != null, SysDeptDO::getDeptId, deptId);
        List<SysDeptDO> deptList = sysDeptService.list(deptQueryWrapper);
        if (CollUtil.isNotEmpty(deptList)) {
            LambdaQueryWrapper<ProjectDO> projectQueryWrapper = new LambdaQueryWrapper<>();
            projectQueryWrapper.in(ProjectDO::getDeptId, deptList.stream().map(SysDeptDO::getDeptId).collect(Collectors.toList()));
            List<ProjectDO> projectList = this.list(projectQueryWrapper);
            Map<Long, List<ProjectDO>> collect = projectList.stream().collect(Collectors.groupingBy(ProjectDO::getDeptId));
            for (SysDeptDO sysDeptDO : deptList) {
                List<ProjectDO> pList = collect.computeIfAbsent(sysDeptDO.getDeptId(), s -> new ArrayList<>());
                if (CollUtil.isNotEmpty(pList)) {
                    ProjectListDTO projectListDTO = new ProjectListDTO();
                    projectListDTO.setDeptName(sysDeptDO.getDeptName());
                    List<ProjectDTO> deptProjectList = pList.stream().map(ProjectDTO::new).collect(Collectors.toList());
                    // 设备数统计
                    for (ProjectDTO projectDTO : deptProjectList) {
                        projectDTO.setDeviceNum(deviceService.count(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getProjectId, projectDTO.getProjectId())));
                    }
                    projectListDTO.setProjectList(deptProjectList);
                    list.add(projectListDTO);
                }
            }
        }
        return list;
    }

    public List<ProjectAddressDTO> addressInfo(String id) {
        List<AddressDO> addressList = addressService.list(new LambdaQueryWrapper<AddressDO>().eq(AddressDO::getProjectId, id));
        Map<String, List<ProjectAddressDTO>> map = new HashMap<>();
        for (AddressDO addressDO : addressList) {
            List<ProjectAddressDTO> child = map.computeIfAbsent(addressDO.getAddressId(), o -> new ArrayList<>());
            ProjectAddressDTO projectAddressDTO = new ProjectAddressDTO();
            projectAddressDTO.setId(addressDO.getAddressId());
            projectAddressDTO.setName(addressDO.getAddressName());
            projectAddressDTO.setType(1);
            projectAddressDTO.setChild(child);
            List<ProjectAddressDTO> parent = map.computeIfAbsent(addressDO.getParentAddressId(), o -> new ArrayList<>());
            parent.add(projectAddressDTO);
        }

        // 设备列表
        List<DeviceDO> deviceList = deviceService.list(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getProjectId, id));
        for (DeviceDO deviceDO : deviceList) {
            List<ProjectAddressDTO> list = map.get(deviceDO.getAddressId());
            if (list != null) {
                ProjectAddressDTO projectAddressDTO = new ProjectAddressDTO();
                projectAddressDTO.setId(deviceDO.getDeviceId().toString());
                projectAddressDTO.setName(deviceDO.getDeviceName());
                projectAddressDTO.setType(deviceDO.getDeviceType() / 1000);
                list.add(projectAddressDTO);
            }
        }

        //传感器列表
        List<DeviceSensorDO> sensorList = deviceSensorService.list(new LambdaQueryWrapper<DeviceSensorDO>().eq(DeviceSensorDO::getProjectId, id));
        for (DeviceSensorDO deviceSensorDO : sensorList) {
            List<ProjectAddressDTO> list = map.get(deviceSensorDO.getAddressId());
            if (list != null) {
                ProjectAddressDTO projectAddressDTO = new ProjectAddressDTO();
                projectAddressDTO.setId(deviceSensorDO.getSensorId().toString());
                projectAddressDTO.setName(deviceSensorDO.getSensorName());
                projectAddressDTO.setType(deviceSensorDO.getSensorType() / 1000);
                Double temperature = deviceTemperatureService.getDeviceLastRecords(deviceSensorDO.getSensorId());
                projectAddressDTO.setTemperature(temperature);
                list.add(projectAddressDTO);
            }
        }

        return new ArrayList<>(map.computeIfAbsent("0", k -> new ArrayList<>()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void fileImport(MultipartFile file) {
        ExcelReader reader = null;
        List<List<Object>> list = null;
        ProjectImportBO projectBO = null;
        String deptName = null;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
            list = reader.read();
        } catch (IOException e) {
            log.error("文件读取失败,请确认文件格式", e);
            throw new MyException("文件读取失败,请确认文件格式");
        }
        try {
            projectBO = this.parseProject(list);
        } catch (Exception e) {
            log.error("导入文件解析失败", e);
            throw new MyException("导入文件解析失败");
        }
        try {
            deptName = list.get(0).get(0).toString();
        } catch (Exception e) {
            log.error("运营商获取失败", e);
            throw new MyException("运营商获取失败");
        }
        SysDeptDO deptDO = sysDeptService.findOrCreate(deptName);
        ProjectDO projectDO = new ProjectDO();
        projectDO.setProjectName(projectBO.getName());
        projectDO.setDeptId(deptDO.getDeptId());
        this.saveProject(projectDO);
        this.setImeiAndModbus(projectBO);
        this.saveItem(projectBO.getChild(), projectDO.getProjectId(), deptDO.getDeptId(), "0");

    }

    private void setImeiAndModbus(ProjectImportBO projectBO) {
        if (CollUtil.isNotEmpty(projectBO.getChild())) {
            for (ProjectImportBO projectImportBO : projectBO.getChild()) {
                if (StrUtil.isNotBlank(projectImportBO.getImei())) {
                    projectBO.setImei(projectImportBO.getImei());
                }
                if (StrUtil.isNotBlank(projectImportBO.getModbus())) {
                    projectBO.setModbus(projectImportBO.getModbus());
                }
            }
            for (ProjectImportBO projectImportBO : projectBO.getChild()) {
                if (StrUtil.isAllBlank(projectImportBO.getImei(), projectImportBO.getModbus())) {
                    projectImportBO.setImei(projectBO.getImei());
                    projectImportBO.setModbus(projectBO.getModbus());
                }
                this.setImeiAndModbus(projectImportBO);
            }
        }
    }

    private void saveItem(List<ProjectImportBO> list, String projectId, Long deptId, String parentAddressId) {
        for (ProjectImportBO projectImportBO : list) {
            if (projectImportBO.getFlag() == 2) {
                AddressDO addressDO = new AddressDO();
                addressDO.setAddressName(projectImportBO.getName());
                addressDO.setAddressType(projectImportBO.getType());
                addressDO.setProjectId(projectId);
                addressDO.setParentAddressId(parentAddressId);
                addressService.save(addressDO);
                this.saveItem(projectImportBO.getChild(), projectId, deptId, addressDO.getAddressId());
            } else if (projectImportBO.getFlag() == 3) {

                if (projectImportBO.getType().equals(DeviceEnum.DEVICE_3001.getDeviceType())) {
                    DeviceSensorDO deviceSensorDO = new DeviceSensorDO();
                    deviceSensorDO.setAddressId(parentAddressId);
                    deviceSensorDO.setProjectId(projectId);
                    deviceSensorDO.setSensorName(projectImportBO.getName());
                    deviceSensorDO.setSensorType(projectImportBO.getType());
                    deviceSensorDO.setDeptId(deptId);
                    deviceSensorDO.setImei(projectImportBO.getImei());
                    deviceSensorDO.setModbus(projectImportBO.getModbus());
                    deviceSensorService.save(deviceSensorDO);
                } else {
                    DeviceDO deviceDO = new DeviceDO();
                    deviceDO.setAddressId(parentAddressId);
                    deviceDO.setProjectId(projectId);
                    deviceDO.setDeviceName(projectImportBO.getName());
                    deviceDO.setDeviceType(projectImportBO.getType());
                    deviceDO.setDeptId(deptId);
                    deviceDO.setImei(projectImportBO.getImei());
                    deviceDO.setModbus(projectImportBO.getModbus());
                    deviceService.saveDevice(deviceDO);
                }
            }
        }
    }

    private void saveProject(ProjectDO projectDO) {
        String projectName = projectDO.getProjectName();
        Long deptId = projectDO.getDeptId();
        LambdaQueryWrapper<ProjectDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectDO::getProjectName, projectName);
        queryWrapper.eq(ProjectDO::getDeptId, deptId);
        ProjectDO one = this.getOne(queryWrapper);
        if (one != null) {
            throw new MyException("已存在的项目名");
        }
        this.save(projectDO);
    }

    private ProjectImportBO parseProject(List<List<Object>> list) {
        ProjectImportBO projectBO = null;
        for (int i = 0; i < list.size(); i++) {
            List<Object> values = list.get(i);
            if (i <= 1) {
                continue;
            }
            String projectName = CollUtil.get(values, 0) == null ? "" : values.get(0).toString();
            String roomName = CollUtil.get(values, 1) == null ? "" : values.get(1).toString();
            String cabinetName = CollUtil.get(values, 2) == null ? "" : values.get(2).toString();
            String lineName = CollUtil.get(values, 3) == null ? "" : values.get(3).toString();
            String censorId = CollUtil.get(values, 4) == null ? "" : values.get(4).toString();
            String modbus = CollUtil.get(values, 5) == null ? null : values.get(5).toString();
            String imei = CollUtil.get(values, 6) == null ? null : values.get(6).toString();
            if (projectBO == null) {
                projectBO = new ProjectImportBO(projectName);
            }

            if (StrUtil.isBlank(roomName)) {
                continue;
            }
            DeviceEnum deviceEnum = DeviceEnum.getByName(roomName);
            if (deviceEnum == null) {
                projectBO.addChildAddress(roomName, AddressEnum.ROOM.getAddressType(), true);
            } else {
                projectBO.addChildDevice(roomName, deviceEnum.getDeviceType(), imei, modbus);
                continue;
            }


            if (StrUtil.isBlank(cabinetName)) {
                continue;
            }
            ProjectImportBO room = projectBO.getChildByName(roomName);
            deviceEnum = DeviceEnum.getByName(cabinetName);
            if (deviceEnum == null) {
                room.addChildAddress(cabinetName, AddressEnum.CABINET.getAddressType(), true);
            } else {
                room.addChildDevice(cabinetName, deviceEnum.getDeviceType(), imei, modbus);
                continue;
            }

            if (StrUtil.isBlank(lineName)) {
                continue;
            }
            ProjectImportBO cabinet = room.getChildByName(cabinetName);
            deviceEnum = DeviceEnum.getByName(lineName);
            if (deviceEnum == null) {
                cabinet.addChildAddress(lineName, AddressEnum.LINE.getAddressType(), true);
            } else {
                cabinet.addChildDevice(lineName, deviceEnum.getDeviceType(), imei, modbus);
                continue;
            }

            ProjectImportBO line = cabinet.getChildByName(lineName);
            if (StrUtil.isNotBlank(censorId)) {
                line.addChildDevice(censorId, DeviceEnum.DEVICE_3001.getDeviceType(), imei, modbus);
            }
        }
        return projectBO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        deviceSensorService.remove(new LambdaQueryWrapper<DeviceSensorDO>().eq(DeviceSensorDO::getProjectId, id));
        deviceService.remove(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getProjectId, id));
        this.removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProject(ProjectForm form) {
        ProjectDO projectDO = this.getById(form.getProjectId());
        if (projectDO == null) {
            return;
        }
        if (StrUtil.isNotBlank(form.getProjectName()) && !form.getProjectName().equals(projectDO.getProjectName())) {
            LambdaQueryWrapper<ProjectDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProjectDO::getProjectName, form.getProjectName());
            queryWrapper.eq(ProjectDO::getDeptId, projectDO.getDeptId());
            ProjectDO one = this.getOne(queryWrapper);
            if (one != null && !one.getProjectId().equals(projectDO.getProjectId())) {
                throw new MyException("已存在的项目名");
            }
        }
        projectDO.setProjectName(form.getProjectName());
        projectDO.setAddress(form.getAddress());
        projectDO.setLatitude(form.getLatitude());
        projectDO.setLongitude(form.getLongitude());
        projectDO.setManager(form.getManager());
        projectDO.setPhone(form.getPhone());
        this.updateById(projectDO);
    }
}
