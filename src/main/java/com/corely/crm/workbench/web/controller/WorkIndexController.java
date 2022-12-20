package com.corely.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkIndexController {
    /**
     * @return 跳转到主页面
     */
    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }
}
