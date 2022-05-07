package com.shawcxx.modules.project.controller;

import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.project.service.AddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2022/5/7 10:47
 * @description
 */
@RestController
@RequestMapping("address")
public class AddressController {
    @Resource
    private AddressService addressService;


}
