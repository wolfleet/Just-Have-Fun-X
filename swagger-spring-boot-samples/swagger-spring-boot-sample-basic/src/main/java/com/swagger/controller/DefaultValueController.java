package com.swagger.controller;

import com.swagger.entity.DefaultPojo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * swagger
 *
 * @author Levin
 * @since 2018/5/16 0016
 */
@RestController
@RequestMapping("/defaults")
@Api(tags = "1.6", description = "默认值", value = "默认值")
public class DefaultValueController {

    @GetMapping
    public DefaultPojo defaultPojo() {

        return new DefaultPojo();
    }


    @PostMapping
    public DefaultPojo defaultPojo1(@RequestBody DefaultPojo defaultPojo) {

        return defaultPojo;
    }
}
