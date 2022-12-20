package com.corely.crm.workbench.service;

import java.util.List;

public interface CustomerService {
    /**
     * 查询客户名称
     */
    List<String> selectCustomersNameByName(String name);
}
