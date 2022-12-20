package com.corely.crm.workbench.web.controller;

import com.corely.crm.common.constants.Constant;
import com.corely.crm.common.domain.ReturnObject;
import com.corely.crm.common.utils.DateUtils;
import com.corely.crm.common.utils.UUIDUtils;
import com.corely.crm.settings.domain.DicValue;
import com.corely.crm.settings.domain.User;
import com.corely.crm.settings.service.DicValueService;
import com.corely.crm.settings.service.UserService;
import com.corely.crm.workbench.domain.Activity;
import com.corely.crm.workbench.domain.Clue;
import com.corely.crm.workbench.domain.ClueActivityRelation;
import com.corely.crm.workbench.domain.ClueRemark;
import com.corely.crm.workbench.service.ActivityService;
import com.corely.crm.workbench.service.ClueActivityRelationService;
import com.corely.crm.workbench.service.ClueRemarkService;
import com.corely.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @RequestMapping("/workbench/clue/goToClueIndex.do")
    public String goToClueIndex(HttpServletRequest request){
        //先查询所有用户
        List<User> users = userService.selectAllUsers();
        //再查询所有的dicValue
        //称呼
        List<DicValue> appellationList = dicValueService.selectDicValueByTypeCode("appellation");
        //线索状态
        List<DicValue> clueStateList = dicValueService.selectDicValueByTypeCode("clueState");
        //线索来源
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");
        //是不是还需要查看所有的线索啊--->经过我的深思熟虑后，我觉得不合适，查看线索要和查看字段分开
        List<Clue> clueList = clueService.selectAllClues();
        request.setAttribute("users",users);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("clueList",clueList);
        return "workbench/clue/index";
    }
    //保存创建的clue
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formatDataTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueService.insertCreateClue(clue);
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
    //在页面按照条数和页数上显示线索-->异步请求
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String fullname,String company,String phone,String mphone,String source,String owner,String state,int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("state",state);
        map.put("pageSize",pageSize);
        int startRow = (pageNo-1)*pageSize;
        map.put("startRow",startRow);
        //1.查询符合条件的线索
        List<Clue> clues = clueService.selectClueByConditionForPage(map);
        //2.查询符合条件线索的条数
        int totalRows = clueService.selectTotalRowsOfClueByCondition(map);
        //除了object以外可以返回map，自动转化为json字符串
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clues",clues);
        retMap.put("totalRows",totalRows);
        return retMap;
    }
    /**
     * 跳转到clue对应的详细信息的页面
     */
    @RequestMapping("/workbench/clue/getClueDetailByClueId.do")
    public String getClueDetailByClueId(String id,HttpServletRequest request){
        //详细线索
        Clue clue = clueService.selectClueForDetailById(id);
        //线索备注
        List<ClueRemark> clueRemarks = clueRemarkService.selectClueRemarksByClueId(id);
        //对应的activity
        List<Activity> activities = activityService.selectActivityForDetailByClueId(id);
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarks",clueRemarks);
        request.setAttribute("activities",activities);
        return "workbench/clue/detail";
    }
    /**
     * 负责根据ActivityName模糊关联对应的activity并排除已经关联过的
     */
    @RequestMapping("/workbench/clue/queryActivityForDetailByActivityNameAndClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByActivityNameAndClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activities = activityService.selectActivityForDetailByNameAndClueId(map);
        return activities;
    }
    /**
     * 保存线索和市场活动的关联关系
     */
    @RequestMapping("/workbench/clue/saveClueActivityRelation.do")
    @ResponseBody
    public Object saveClueActivityRelation(String[] activityId,String clueId,HttpServletRequest request){
        List<ClueActivityRelation> list = new ArrayList<>();
        ClueActivityRelation clueActivityRelation = null;
        for (int i = 0; i < activityId.length; i++) {
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtils.getUUID());
            clueActivityRelation.setActivityId(activityId[i]);
            clueActivityRelation.setClueId(clueId);
            list.add(clueActivityRelation);
        }
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.insertClueActivityRelationList(list);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                //成功后继续查询对应的市场活动列表
                List<Activity> activities = activityService.selectActivitiesByIds(activityId);
                returnObject.setRetData(activities);
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
    /**
     * 解除市场活动和线索联系
     */
    @RequestMapping("/workbench/clue/removeClueActivityRelation.do")
    @ResponseBody
    public Object removeClueActivityRelation(ClueActivityRelation relation){
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.deleteClueActivityRelationList(relation);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
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
    /**
     * 跳转到线索转换页面
     */
    @RequestMapping("/workbench/clue/gotoClueConvert.do")
    public String gotoClueConvert(String id,HttpServletRequest request){
        Clue clue = clueService.selectClueForDetailById(id);
        List<DicValue> stages = dicValueService.selectDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stages",stages);
        return "workbench/clue/convert";
    }
    /**
     * 查询转换页面的市场活动源
     */
    @RequestMapping("/workbench/clue/queryActivitySourceForClue.do")
    @ResponseBody
    public Object queryActivitySourceForClue(String name,String clueId){
        ReturnObject returnObject = new ReturnObject();
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("clueId",clueId);
        try {
            List<Activity> activities = activityService.selectActivityForDetailByName(map);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activities);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("系统忙，请稍后再试...");
        }
        return returnObject;
    }
    /**
     * 转换线索
     */
    @RequestMapping("/workbench/clue/saveConvertFromClue.do")
    @ResponseBody
    private Object saveConvertFromClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        map.put("clueId",clueId);
        map.put(Constant.SESSION_USER,user);
        map.put("money",money);
        map.put("name",name);
        map.put("exceptedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);

        ReturnObject returnObject = new ReturnObject();
        try {
            clueService.saveConvert(map);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("系统忙，请稍后再试...");
        }
        return returnObject;
    }
}
