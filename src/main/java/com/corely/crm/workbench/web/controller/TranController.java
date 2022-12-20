package com.corely.crm.workbench.web.controller;

import com.corely.crm.common.constants.Constant;
import com.corely.crm.common.domain.ReturnObject;
import com.corely.crm.settings.domain.DicValue;
import com.corely.crm.settings.domain.User;
import com.corely.crm.settings.service.DicValueService;
import com.corely.crm.settings.service.UserService;
import com.corely.crm.workbench.domain.Contacts;
import com.corely.crm.workbench.domain.Tran;
import com.corely.crm.workbench.domain.TranHistory;
import com.corely.crm.workbench.mapper.TranHistoryMapper;
import com.corely.crm.workbench.service.CustomerService;
import com.corely.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private TranService tranService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TranHistoryMapper tranHistoryMapper;
    /**
     * 跳转到交易的主页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/gotoTranIndex.do")
    public String gotoTranIndex(HttpServletRequest request){
        List<DicValue> stageList = dicValueService.selectDicValueByTypeCode("stage");
        List<DicValue> typeList = dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");
        request.setAttribute("stageList",stageList);
        request.setAttribute("typeList",typeList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/transaction/index";
    }
    /**
     * 跳转到交易的创建页面
     */
    @RequestMapping("/workbench/transaction/gotoSave.do")
    public String gotoSave(HttpServletRequest request, HttpSession session){
        List<User> users = userService.selectAllUsers();
        List<DicValue> stageList = dicValueService.selectDicValueByTypeCode("stage");
        List<DicValue> typeList = dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");
        request.setAttribute("users",users);
        request.setAttribute("stageList",stageList);
        request.setAttribute("typeList",typeList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/transaction/save";
    }
    /**根据联系人名称模糊查找
     *
     */
    @RequestMapping("/workbench/transaction/selectContactsByName.do")
    @ResponseBody
    public Object selectContactsByName(String fullname){
        ReturnObject returnObject = new ReturnObject();
        try {
            List<Contacts> contacts = tranService.selectContactsByName(fullname);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(contacts);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("超时请重试...");
        }
        return returnObject;
    }
    /**
     * 返回每个阶段对应的可能性
     */
    @RequestMapping("/workbench/transaction/returnPossibility.do")
    @ResponseBody
    public Object returnPossibility(String stage) throws IOException {
        //也可以用ResourceBundle.getBundle("possibility")来获取
        Properties properties = new Properties();
            FileInputStream input = new FileInputStream(new File("D:\\1javacode\\03ssm\\05ssmProject_crm\\crm\\src\\main\\resources\\possbility.properties"));
            properties.load(input);
            String possbility =(String) properties.get(stage);
        return possbility;
    }
    /**
     * 自动补全客户名称
     */
    @RequestMapping("/workbench/transaction/queryAllCustomerName.do")
    @ResponseBody
    public Object queryAllCustomerName(String name){
        List<String> customersNames = customerService.selectCustomersNameByName(name);
        return customersNames;
    }
    /**
     * 保存一个交易
     */
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(Tran tran,HttpSession session){
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.saveCreateTran(tran,user);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("系统忙，请稍后再试...");
        }
        return returnObject;
    }
    /**
     * 为交易的主页面查询所有交易
     */
    @RequestMapping("/workbench/transaction/selectAllTransForIndex.do")
    @ResponseBody
    public Object selectAllTransForIndex(){
        ReturnObject returnObject = new ReturnObject();
        try {
            List<Tran> trans = tranService.selectAllTransForIndex();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(trans);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMsg("系统忙，请稍后再试");
        }
        return returnObject;
    }
    /**
     * 跳转到明细页面
     */
    @RequestMapping("/workbench/transaction/gotoDetail.do")
    public String gotoDetail(String id,HttpServletRequest request){
        //基本信息，历史信息
        Tran tran = tranService.selectTranForDetail(id);
        List<TranHistory> tranHistories = tranHistoryMapper.selectTranHistoryForDetailByTranId(id);
        //读取对应的stage的可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possbility");
        String possibility = bundle.getString(tran.getStage());
        //查stage的类型
        List<DicValue> stageList = dicValueService.selectDicValueByTypeCode("stage");
        request.setAttribute("tran",tran);
        request.setAttribute("tranHistories",tranHistories);
        request.setAttribute("possibility",possibility);
        request.setAttribute("stageList",stageList);
        return "workbench/transaction/detail";
    }
}
