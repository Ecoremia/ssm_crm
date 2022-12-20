package com.corely.crm.workbench.web.controller;

import com.corely.crm.common.constants.Constant;
import com.corely.crm.common.domain.ReturnObject;
import com.corely.crm.common.utils.DateUtils;
import com.corely.crm.common.utils.HSSFUtils;
import com.corely.crm.common.utils.UUIDUtils;
import com.corely.crm.settings.domain.User;
import com.corely.crm.settings.service.UserService;
import com.corely.crm.workbench.domain.Activity;
import com.corely.crm.workbench.domain.ActivityRemark;
import com.corely.crm.workbench.service.ActivityRemarkService;
import com.corely.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    /**
     * 查询用户，并返回到市场活动的主页
     */
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/selectUser.do")
    public String queryAllUsers(HttpServletRequest request){//这个功能也是跳转到首页之前的
        List<User> users = userService.selectAllUsers();
        request.setAttribute("users",users);
        return "workbench/activity/index";
    }
    /**
     * 生成一条新的市场活动记录，使用异步请求
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        //参数太多了，直接把它封装成一个对象
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDataTime(new Date()));
        User user =(User) session.getAttribute(Constant.SESSION_USER);
        //这里封装用户的id，因为用户id具有唯一标识性
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        //try catch的目的是，即使这个功能没有报异常，也能为客户提供信息。
        try {
            int num = activityService.insertActivity(activity);
            if(num>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMsg("系统忙，请稍后再试......");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("系统忙，请稍后再试......");
        }
        return returnObject;
    }
    /**
     * 根据条件分页查询市场活动列表信息
     */
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("startRow",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        List<Activity> activities = activityService.selectActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //这里返回一个map对象，可以自动转化为json字符串
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("activities",activities);
        resMap.put("totalRows",totalRows);
        return resMap;
    }
    /**
     * 批量删除指定id的市场活动
     */
    @RequestMapping("/workbench/activity/deleteActivitiesByIds.do")
    @ResponseBody//变量这里为了和客户端保持一致，使用id
    public Object deleteActivitiesByIds(String[] id){
        int i = activityService.deleteActivityByIds(id);
        ReturnObject object = null;
        try {
            object = new ReturnObject();
            if(i > 0){
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMsg("删除失败，请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMsg("系统忙，请稍后再试...");
        }
        return object;
    }
    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        //查数据不考虑trycatch，直接返回activity
            Activity activity = activityService.selectActivityById(id);
            return activity;
    }
    /**
     * 保存修改后的市场活动
     */
    @RequestMapping("/workbench/acivity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        activity.setEditTime(DateUtils.formatDataTime(new Date()));
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        activity.setEditBy(user.getId());
        ReturnObject object = new ReturnObject();
        try {
            int i = activityService.updateActivity(activity);
            if(i > 0){
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMsg("更新失败，请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMsg("更新失败，请稍后再试...");
        }
        return object;
    }
    /**
     * 导出全部市场活动的controler
     */
    @RequestMapping("/workbench/activity/outputActivities.do")
    public void outputActivities(HttpServletResponse response) throws IOException {
        List<Activity> activities = activityService.selectAllActivities();
        exportActivitiesToWeb(activities,response);
    }
    /**
     * 导出部分市场活动的controller
     */
    @RequestMapping("/workbench/activity/outputActivitiesByIds.do")
    public void outputActivitiesByIds(HttpServletResponse response,String[] id) throws IOException {
        List<Activity> activities = activityService.selectActivitiesByIds(id);
        exportActivitiesToWeb(activities,response);
    }
    /**
 * 一个向浏览器输出市场活动文件的方法
 */
public void exportActivitiesToWeb(List<Activity> activities,HttpServletResponse response) throws IOException {
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet("市场活动列表");
    HSSFRow row = sheet.createRow(0);
    HSSFCell cell = row.createCell(0);
    cell.setCellValue("ID");
    cell = row.createCell(1);
    cell.setCellValue("所有者");
    cell = row.createCell(2);
    cell.setCellValue("活动名称");
    cell = row.createCell(3);
    cell.setCellValue("开始日期");
    cell = row.createCell(4);
    cell.setCellValue("结束日期");
    cell = row.createCell(5);
    cell.setCellValue("成本");
    cell = row.createCell(6);
    cell.setCellValue("具体信息");
    cell = row.createCell(7);
    cell.setCellValue("创建时间");
    cell = row.createCell(8);
    cell.setCellValue("创建者");
    cell = row.createCell(9);
    cell.setCellValue("编辑时间");
    cell = row.createCell(10);
    cell.setCellValue("编辑者");
    //i最好从0开始，这样防止没有数据的时候进行遍历
    for (int i = 0; i < activities.size(); i++) {
        Activity activity = activities.get(i);
        row = sheet.createRow(i+1);
        cell = row.createCell(0);
        cell.setCellValue(activity.getId());
        cell = row.createCell(1);
        cell.setCellValue(activity.getOwner());
        cell = row.createCell(2);
        cell.setCellValue(activity.getName());
        cell = row.createCell(3);
        cell.setCellValue(activity.getStartDate());
        cell = row.createCell(4);
        cell.setCellValue(activity.getEndDate());
        cell = row.createCell(5);
        cell.setCellValue(activity.getCost());
        cell = row.createCell(6);
        cell.setCellValue(activity.getDescription());
        cell = row.createCell(7);
        cell.setCellValue(activity.getCreateTime());
        cell = row.createCell(8);
        cell.setCellValue(activity.getCreateBy());
        cell = row.createCell(9);
        cell.setCellValue(activity.getEditTime());
        cell = row.createCell(10);
        cell.setCellValue(activity.getEditBy());
    }
//     又存在磁盘，又从磁盘里读出来，效率低
//        FileOutputStream outputStream = new FileOutputStream("D:\\2020年文件\\ActivityList.xls");
//        wb.write(outputStream);
//        outputStream.close();
//        wb.close();

    //直接让wb把它发到浏览器上
    response.setContentType("application/octet-stream;charset=UTF-8");
    response.setHeader("Content-Disposition","attachment;filename=AllActivities.xls");
    ServletOutputStream output = response.getOutputStream();
//        FileInputStream inputStream = new FileInputStream("D:\\2020年文件\\ActivityList.xls");
//        int len = 0;
//        byte buff[] = new byte[1024];
//        while((len = inputStream.read(buff))!= -1){
//            output.write(buff,0,len);
//        }
//        inputStream.close();
    wb.write(output);
    wb.close();
    output.flush();
}
/**
 * 批量导入市场活动的controller
 */
@RequestMapping("/workbench/activity/importActivitiesByUpload.do")
@ResponseBody
public Object importActivitiesByUpload(MultipartFile activityFile,HttpSession session){
    User user = (User) session.getAttribute(Constant.SESSION_USER);
    ReturnObject returnObject = new ReturnObject();
    List<Activity> activities = new ArrayList<>();
//    File file = new File("D:\\1javacode\\资料\\aa模拟接收\\服务器\\" + activityFile.getOriginalFilename());


    try {
//        activityFile.transferTo(file);
//        FileInputStream is = new FileInputStream("D:\\1javacode\\资料\\aa模拟接收\\服务器\\" + activityFile.getOriginalFilename());
        //直接通过文件获取输出流
        InputStream is = activityFile.getInputStream();
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
            row = sheet.getRow(i);
            Activity activity = new Activity();
            activity.setId(UUIDUtils.getUUID());
            activity.setOwner(user.getId());
            activity.setCreateTime(DateUtils.formatDataTime(new Date()));
            activity.setCreateBy(user.getId());
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                if(j==0){
                    activity.setName(HSSFUtils.getCellValueStr(cell));
                }else if(j==1){
                    activity.setStartDate(HSSFUtils.getCellValueStr(cell));
                }else if(j==2){
                    activity.setEndDate(HSSFUtils.getCellValueStr(cell));
                }else if(j==3){
                    activity.setCost(HSSFUtils.getCellValueStr(cell));
                }else if(j==4){
                    activity.setDescription(HSSFUtils.getCellValueStr(cell));
                }
            }
            activities.add(activity);
        }
        int ret = activityService.insertActivitiesByList(activities);
        if(ret > 0){
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        }else {
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setRetData("上传失败，请稍后再试");
        }
    } catch (IOException e) {
        e.printStackTrace();
        returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
        returnObject.setRetData("上传失败，请稍后再试");
    }
    return returnObject;
}
/**
 * 查询市场活动详细信息以及其备注的controller
 */
@RequestMapping("/workbench/activity/getActivityAndRemarkDetail.do")
public String getActivityAndRemarkDetail(String activityID,HttpServletRequest request){
    //先查询市场活动的详细信息-->注意这个方法要和外键一起使用
    Activity activity = activityService.selectActivityForDetailById(activityID);
    //再查询备注信息
    List<ActivityRemark> activityRemarks = activityRemarkService.selectActivityRemarkForDetailByActivityId(activityID);
    request.setAttribute("activity",activity);
    request.setAttribute("activityRemarks",activityRemarks);
    return "workbench/activity/detail";
}
























    /**
     * 向浏览器发送文件的测试
     */
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownloadTest(HttpServletResponse response) throws IOException {
        //设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置响应头，使浏览器接受到相应信息后，直接下载
        response.setHeader("Content-Disposition","attachment;filename=myStudentList.xls");
        //获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        FileInputStream input = new FileInputStream("D:\\1javacode\\经验总结\\studentList.xls");
        byte buff[] = new byte[1024];
        int len = 0;
        while ((len = input.read(buff))!= -1){
            outputStream.write(buff,0,len);
        }
        input.close();
        outputStream.flush();
    }
    /**
     * 获取从浏览器发过来的请求
     * 记得在springmvc中配置文件解析器
     */
    @RequestMapping("/workbench/activity/receiveFile.do")
    @ResponseBody
    public Object receiveFile(String name, MultipartFile myFile) throws IOException {
        System.out.println(name);
        String originalFilename = myFile.getOriginalFilename();
        File file = new File("D:\\1javacode\\资料\\aa模拟接收\\服务器\\" + originalFilename);
        myFile.transferTo(file);
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMsg("成功接收");
        return returnObject;
    }
}
