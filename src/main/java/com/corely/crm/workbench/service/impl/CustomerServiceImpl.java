package com.corely.crm.workbench.service.impl;

import com.corely.crm.workbench.mapper.CustomerMapper;
import com.corely.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public List<String> selectCustomersNameByName(String name) {
        return customerMapper.selectCustomersNameByName(name);
    }
}
