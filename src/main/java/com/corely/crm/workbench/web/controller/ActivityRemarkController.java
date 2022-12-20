package com.corely.crm.workbench.web.controller;

import com.corely.crm.common.constants.Constant;
import com.corely.crm.common.domain.ReturnObject;
import com.corely.crm.common.utils.DateUtils;
import com.corely.crm.common.utils.UUIDUtils;
import com.corely.crm.settings.domain.User;
import com.corely.crm.workbench.domain.ActivityRemark;
import com.corely.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark activityRemark, HttpSession session){
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateTime(DateUtils.formatDataTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag(Constant.REMARK_EDIT_FLAG_NO);
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRemarkService.insertActivityRemark(activityRemark);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
            }else{
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
    /**
     * 删除特定id的市场活动备注
     */
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRemarkService.deleteActivityRemarkById(id);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMsg("系统忙，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("系统忙，请稍后再试");
        }
        return returnObject;
    }
    /**更新修改后的市场活动
     *
     */
    @RequestMapping("/workbench/activity/updateEditActivityRemark.do")
    @ResponseBody
    public Object updateEditActivityRemark(ActivityRemark activityRemark,HttpSession httpSession){
        User user =(User) httpSession.getAttribute(Constant.SESSION_USER);
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditTime(DateUtils.formatDataTime(new Date()));
        activityRemark.setEditFlag(Constant.REMARK_EDIT_FLAG_YES);
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRemarkService.updateActivityRemark(activityRemark);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                //还要返回整个更新后的remark，以便前端调用
                returnObject.setRetData(activityRemark);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMsg("更新失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("更新失败，请稍后再试");
        }
        return returnObject;
    }
}
