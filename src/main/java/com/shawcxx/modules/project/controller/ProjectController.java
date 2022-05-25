package com.shawcxx.modules.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.project.dto.ProjectAddressDTO;
import com.shawcxx.modules.project.dto.ProjectListDTO;
import com.shawcxx.modules.project.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjl
 * @date 2022/5/7 10:00
 * @description
 */
@RestController
@RequestMapping("project")
public class ProjectController {
    @Resource
    private ProjectService projectService;


    @PostMapping("projectList")
    @SaCheckLogin
    public MyResult projectList() {
        List<ProjectListDTO> list = projectService.projectList();
        return MyResult.data(list);
    }

    @PostMapping("addressInfo")
    @SaCheckLogin
    public MyResult addressInfo(@RequestParam String id) {
        List<ProjectAddressDTO> list = projectService.addressInfo(id);
        return MyResult.data(list);
    }


    @PostMapping("projectImport")
    @SaCheckLogin
    public MyResult projectImport(@RequestParam("file") MultipartFile file) {
        projectService.fileImport(file);
        return MyResult.ok();
    }

    @PostMapping("delete")
    @SaCheckLogin
    public MyResult delete(@RequestParam String id) {
        projectService.delete(id);
        return MyResult.ok();
    }
}
