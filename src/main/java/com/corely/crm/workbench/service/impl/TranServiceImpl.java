package com.corely.crm.workbench.service.impl;

import com.corely.crm.common.utils.DateUtils;
import com.corely.crm.common.utils.UUIDUtils;
import com.corely.crm.settings.domain.User;
import com.corely.crm.workbench.domain.*;
import com.corely.crm.workbench.mapper.ContactsMapper;
import com.corely.crm.workbench.mapper.CustomerMapper;
import com.corely.crm.workbench.mapper.TranHistoryMapper;
import com.corely.crm.workbench.mapper.TranMapper;
import com.corely.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranHistoryMapper tranHistoryMapper;
    @Override
    public List<Contacts> selectContactsByName(String fullname) {
        return contactsMapper.selectContactsByName(fullname);
    }

    @Override
    public void saveCreateTran(Tran tran, User user) {
        //先根据name查询id(这里我们直接把name封装成id里了)
        String custName = tran.getCustomerId();
        String custId = customerMapper.selectCustomerIdByName(custName);
        if(custId==null||custId==""){
            //我们就去创建一个客户-->并保存
            Customer customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName(custName);
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formatDataTime(new Date()));
//            customer.setContactSummary(tran.getContactSummary());
//            customer.setNextContactTime(tran.getNextContactTime());
//            customer.setDescription(tran.getDescription());
            //并保存
            customerMapper.insertCustomer(customer);
            custId = customer.getId();
        }
        //然后再去保存tran
        tran.setCustomerId(custId);
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtils.formatDataTime(new Date()));
        tranMapper.insertTran(tran);
        //保存好tran之后再保存tran_history
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(DateUtils.formatDataTime(new Date()));
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHis(tranHistory);
    }

    @Override
    public List<Tran> selectAllTransForIndex() {
        return tranMapper.selectAllTransForIndex();
    }

    @Override
    public Tran selectTranForDetail(String id) {
        return tranMapper.selectTranForDetail(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.queryCountOfTranGroupByStage();
    }

}
