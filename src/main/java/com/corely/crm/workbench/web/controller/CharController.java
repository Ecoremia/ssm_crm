package com.corely.crm.workbench.web.controller;

import com.corely.crm.workbench.domain.FunnelVO;
import com.corely.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CharController {
    @Autowired
    private TranService tranService;
    @RequestMapping("/workbench/chart/transaction/tranCharIndex.do")
    public String tranCharIndex(){
        return "workbench/chart/transaction/index";
    }
    /**
     * 查询阶段信息
     */
    @RequestMapping("/workbench/chart/transaction/queryStageCountGroupByStage.do")
    @ResponseBody
    public Object queryStageCountGroupByStage(){
        List<FunnelVO> funnelVOS = tranService.queryCountOfTranGroupByStage();
        return funnelVOS;
    }
}
