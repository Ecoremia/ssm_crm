package com.corely.crm.settings.service;

import com.corely.crm.settings.domain.User;
import com.corely.crm.settings.mapper.UserMapper;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 按照用户名和密码做登录查询，查询该用户是否存在
     * @param map
     * @return
     */
    User queryUserByActAndPwd(Map<String,Object> map);
    /**
     * 查询所有用户
     */
    List<User> selectAllUsers();
}
