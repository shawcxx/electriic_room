package com.shawcxx.modules.sys.controller;

import com.shawcxx.common.base.MyResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: dj
 * @create: 2020-03-02 09:28
 * @description:
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    /**
     * home
     */
    @GetMapping
    public MyResult home() {
        return MyResult.ok("hello user");
    }

}
