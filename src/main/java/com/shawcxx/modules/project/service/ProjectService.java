package com.shawcxx.modules.project.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.project.dao.ProjectDAO;
import com.shawcxx.modules.project.domain.AddressDO;
import com.shawcxx.modules.project.domain.ProjectDO;
import com.shawcxx.modules.project.dto.ProjectAddressDTO;
import com.shawcxx.modules.project.dto.ProjectDTO;
import com.shawcxx.modules.project.dto.ProjectListDTO;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.service.SysDeptService;
import org.springframework.stereotype.Service;
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
                    //todo 设备数统计
                    projectListDTO.setProjectList(pList.stream().map(ProjectDTO::new).collect(Collectors.toList()));
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
        //todo 设备列表
        return new ArrayList<>(map.computeIfAbsent("0", k -> new ArrayList<>()));
    }

    public void fileImport(MultipartFile file) {
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            List<Map<String, Object>> maps = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

}
