package com.corely.crm.workbench.web.controller;

import com.corely.crm.common.constants.Constant;
import com.corely.crm.common.domain.ReturnObject;
import com.corely.crm.common.utils.DateUtils;
import com.corely.crm.common.utils.UUIDUtils;
import com.corely.crm.settings.domain.User;
import com.corely.crm.workbench.domain.ClueRemark;
import com.corely.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ClueRemarkController {
    @Autowired
    private ClueRemarkService clueRemarkService;
    /**
     * 保存新创建的clueRemark
     */
    @RequestMapping("/workbench/clue/saveCreateClueRemark.do")
    @ResponseBody
    public Object saveCreateClueRemark(ClueRemark clueRemark, HttpSession session){
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateUtils.formatDataTime(new Date()));
        clueRemark.setEditFlag(Constant.REMARK_EDIT_FLAG_NO);
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueRemarkService.insertCreateClueRemark(clueRemark);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(clueRemark);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMsg("系统忙，请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("系统忙，请稍后再试...");
        }
        return returnObject;
    }
}
