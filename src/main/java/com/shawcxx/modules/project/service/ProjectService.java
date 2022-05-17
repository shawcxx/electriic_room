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
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.modules.project.bo.AddressEnum;
import com.shawcxx.modules.project.bo.ProjectImportBO;
import com.shawcxx.modules.project.dao.ProjectDAO;
import com.shawcxx.modules.project.domain.AddressDO;
import com.shawcxx.modules.project.domain.ProjectDO;
import com.shawcxx.modules.project.dto.ProjectAddressDTO;
import com.shawcxx.modules.project.dto.ProjectDTO;
import com.shawcxx.modules.project.dto.ProjectListDTO;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.service.SysDeptService;
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
public class ProjectService extends ServiceImpl<ProjectDAO, ProjectDO> {
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private AddressService addressService;
    @Resource
    private DeviceService deviceService;

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
                        projectDTO.setDeviceNum(deviceService.count(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getProjectId, projectDTO.getProjectId()).lt(DeviceDO::getDeviceType, 3000)));
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
                projectAddressDTO.setType(deviceDO.getDeviceType()/1000);
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
            throw new MyException("文件读取失败,请确认文件格式");
        }
        try {
            projectBO = this.parseProject(list);
        } catch (Exception e) {
            throw new MyException("导入文件解析失败");
        }
        try {
            deptName = list.get(0).get(0).toString();
        } catch (Exception e) {
            throw new MyException("运营商字段获取失败");
        }
        SysDeptDO deptDO = sysDeptService.findOrCreate(deptName);
        ProjectDO projectDO = new ProjectDO();
        projectDO.setProjectName(projectBO.getName());
        projectDO.setDeptId(deptDO.getDeptId());
        this.saveProject(projectDO);
        this.saveItem(projectBO.getChild(), projectDO.getProjectId(), deptDO.getDeptId(), "0");

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
            String projectName = values.get(0) == null ? "" : values.get(0).toString();
            String roomName = values.get(1) == null ? "" : values.get(1).toString();
            String cabinetName = values.get(2) == null ? "" : values.get(2).toString();
            String lineName = values.get(3) == null ? "" : values.get(3).toString();
            String censorId = values.get(4) == null ? "" : values.get(4).toString();
            String modbus = values.get(5) == null ? "" : values.get(5).toString();
            String imei = values.get(6) == null ? "" : values.get(6).toString();
            if (projectBO == null) {
                projectBO = new ProjectImportBO(projectName);
            }

            DeviceEnum deviceEnum = DeviceEnum.getByName(roomName);
            if (deviceEnum == null) {
                projectBO.addChildAddress(roomName, AddressEnum.ROOM.getAddressType(), true);
            } else {
                projectBO.addChildDevice(roomName, deviceEnum.getDeviceType(), imei, modbus);
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

    public static void main(String[] args) {
        ExcelReader reader = ExcelUtil.getReader("C:\\Users\\admin\\Desktop\\第一个测试项目.xls");
        List<List<Object>> list = reader.read();
        SysDeptDO deptDO = null;
        ProjectImportBO projectBO = null;
        for (int i = 0; i < list.size(); i++) {
            List<Object> values = list.get(i);
            if (i == 0) {
                String deptName = values.get(0).toString();
                if (deptDO == null) {
                    System.out.println(deptName);
                }
                //todo 运营商判断操作
                continue;
            }
            if (i == 1) {
                continue;
            }
            String projectName = values.get(0) == null ? "" : values.get(0).toString();
            String roomName = values.get(1) == null ? "" : values.get(1).toString();
            String cabinetName = values.get(2) == null ? "" : values.get(2).toString();
            String lineName = values.get(3) == null ? "" : values.get(3).toString();
            String censorId = values.get(4) == null ? "" : values.get(4).toString();
            String modbus = values.get(5) == null ? "" : values.get(5).toString();
            String imei = values.get(6) == null ? "" : values.get(6).toString();
            if (projectBO == null) {
                projectBO = new ProjectImportBO(projectName);
            }

            DeviceEnum deviceEnum = DeviceEnum.getByName(roomName);
            if (deviceEnum == null) {
                projectBO.addChildAddress(roomName, AddressEnum.ROOM.getAddressType(), true);
            } else {
                projectBO.addChildDevice(roomName, deviceEnum.getDeviceType(), imei, modbus);
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
        System.out.println(JSON.toJSONString(projectBO));
    }

}
