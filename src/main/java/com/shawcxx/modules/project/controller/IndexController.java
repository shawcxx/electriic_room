package com.shawcxx.modules.project.controller;

import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.project.dto.IndexDTO;
import com.shawcxx.modules.project.service.IndexService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Chen jl
 * @date 2022/5/21 16:32
 * @description
 **/
@RestController
@RequestMapping("index")
public class IndexController {
    @Resource
    private IndexService indexService;


    @PostMapping("info")
    public MyResult index() {
        IndexDTO indexDTO = indexService.index();
        return MyResult.data(indexDTO);
    }
}
