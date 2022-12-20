package com.corely.crm.settings.web.controller;

import com.corely.crm.common.constants.Constant;
import com.corely.crm.common.domain.ReturnObject;
import com.corely.crm.common.utils.DateUtils;
import com.corely.crm.settings.domain.User;
import com.corely.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
//    跳转到登录页面
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(HttpServletRequest request) {
        //这里做的是，读取cookie中的用户名和密码，做到自动填充
        Cookie[] cookies = request.getCookies();
        String loginAct = "";
        String loginPwd = "";
        if(cookies!=null&&cookies.length!=0) {
            for (Cookie cookie : cookies) {
                if ("loginAct".equals(cookie.getName())) {
                    loginAct = cookie.getValue();
                } else if ("loginPwd".equals(cookie.getName())) {
                    loginPwd = cookie.getValue();
                }
            }
        }
        request.setAttribute("loginAct",loginAct);
        request.setAttribute("loginPwd",loginPwd);

        return "settings/qx/user/login";
    }

//    处理登录请求
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response){
        //获取user对象
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userService.queryUserByActAndPwd(map);
        //这个对象中含有成功/失败的信息。
        ReturnObject reObject;
        String nowTime = DateUtils.formatDataTime(new Date());
        //对user对象是否过期做一个验证
        if (user == null){
            reObject = new ReturnObject(Constant.RETURN_OBJECT_CODE_FAIL,"您的用户名或密码有误");

        }else if(user.getExpireTime().compareTo(nowTime) < 0){
            reObject = new ReturnObject(Constant.RETURN_OBJECT_CODE_FAIL,"您的身份已过期");
        }else if(user.getLockState().equals("0")){
            reObject = new ReturnObject(Constant.RETURN_OBJECT_CODE_FAIL,"您的状态已锁定");
        }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
            reObject = new ReturnObject(Constant.RETURN_OBJECT_CODE_FAIL,"您的ip地址超出范围");
        }else {
            //验证成功后进行的操作
            //填充object的信息（成功/失败）
            reObject = new ReturnObject(Constant.RETURN_OBJECT_CODE_SUCCESS,"登录成功");
            //作用域中放入user对象
            request.getSession().setAttribute(Constant.SESSION_USER,user);
            if(isRemPwd.equals("true")) {
                Cookie cookie1 = new Cookie("loginAct", loginAct);
                cookie1.setMaxAge(60*60*24*10);
                Cookie cookie2 = new Cookie("loginPwd", loginPwd);
                cookie2.setMaxAge(60*60*24*10);
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }else {
                Cookie cookie1 = new Cookie("loginAct", "1");
                cookie1.setMaxAge(0);
                Cookie cookie2 = new Cookie("loginPwd", "1");
                cookie2.setMaxAge(0);
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }
        }
//        因为这里是异步请求，所以只需要返回一个对象
        return reObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
//        销毁cookie
        Cookie cookie1 = new Cookie("loginAct", "1");
        cookie1.setMaxAge(0);
        Cookie cookie2 = new Cookie("loginPwd", "1");
        cookie2.setMaxAge(0);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
//        销毁session
        session.invalidate();
        //用重定向的方式跳转到首页
        return "redirect:/";
    }
}

