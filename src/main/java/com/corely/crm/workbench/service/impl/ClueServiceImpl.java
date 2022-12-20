package com.corely.crm.workbench.service.impl;

import com.corely.crm.common.constants.Constant;
import com.corely.crm.common.utils.DateUtils;
import com.corely.crm.common.utils.UUIDUtils;
import com.corely.crm.settings.domain.User;
import com.corely.crm.workbench.domain.*;
import com.corely.crm.workbench.mapper.*;
import com.corely.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    @Override
    public List<Clue> selectAllClues() {
        return clueMapper.selectAllClues();
    }

    @Override
    public int insertCreateClue(Clue clue) {
        return clueMapper.insertCreateClue(clue);
    }

    @Override
    public List<Clue> selectClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int selectTotalRowsOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectTotalRowsOfClueByCondition(map);
    }

    @Override
    public Clue selectClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        //1.根据id查询线索
        String clueId = (String)map.get("clueId");
        User user = (User)map.get(Constant.SESSION_USER);
        Clue clue = clueMapper.selectClueForDetailAndIdsById(clueId);
        //2.把信息保存到客户中
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        //谁创建的谁是所有者
        customer.setOwner(user.getId());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDataTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        //不报异常，就可以认为是成功
        //2.5.保存客户
        customerMapper.insertCustomer(customer);
        //3.把信息保存到联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDataTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        contactsMapper.insertContact(contacts);
        //4.查询该线索下的所有备注
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkAndIdByCustomerId(clueId);
        //4.5 如果备注表不为空
        if(clueRemarks != null &&clueRemarks.size()!=0) {
            //5.把备注保存到客户备注表中
            List<CustomerRemark> customerRemarks = new ArrayList<>();
            CustomerRemark customerRemark = null;
            for (int i = 0; i < clueRemarks.size(); i++) {
                customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setNoteContent(clueRemarks.get(i).getNoteContent());
                customerRemark.setCreateBy(clueRemarks.get(i).getCreateBy());
                customerRemark.setCreateTime(clueRemarks.get(i).getCreateTime());
                customerRemark.setEditBy(clueRemarks.get(i).getEditBy());
                customerRemark.setEditTime(clueRemarks.get(i).getEditTime());
                customerRemark.setEditFlag(clueRemarks.get(i).getEditFlag());
                customerRemark.setCustomerId(customer.getId());
                customerRemarks.add(customerRemark);
            }
            customerRemarkMapper.insertCustomerRemarks(customerRemarks);
            //6.把备注保存到联系人备注表中
            List<ContactsRemark> contactsRemarks = new ArrayList<>();
            ContactsRemark contactsRemark = null;
            for (int i = 0; i < clueRemarks.size(); i++) {
                contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setNoteContent(clueRemarks.get(i).getNoteContent());
                contactsRemark.setCreateBy(clueRemarks.get(i).getCreateBy());
                contactsRemark.setCreateTime(clueRemarks.get(i).getCreateTime());
                contactsRemark.setEditBy(clueRemarks.get(i).getEditBy());
                contactsRemark.setEditTime(clueRemarks.get(i).getEditTime());
                contactsRemark.setEditFlag(clueRemarks.get(i).getEditFlag());
                contactsRemark.setContactsId(contacts.getId());
                contactsRemarks.add(contactsRemark);
            }
            contactsRemarkMapper.insertContactsRemarks(contactsRemarks);
        }
        //6.5查询线索和市场活动的关联关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectClueActivityRelationListByClueId(clueId);
        //7.转换联系人和市场活动的关联关系
        if(clueActivityRelationList!=null&&clueActivityRelationList.size()!=0) {
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            ContactsActivityRelation contactsActivityRelation = null;
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setActivityId(clueActivityRelation.getId());
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationList(contactsActivityRelationList);
        }
            //8.如果需要创建交易，在交易表里添加一条记录
            String isCreateTran = (String)map.get("isCreateTran");
            if("true".equals(isCreateTran)){
                String money = (String) map.get("money");
                String name = (String) map.get("name");
                String exceptedDate = (String) map.get("exceptedDate");
                String stage = (String) map.get("stage");
                String activityId = (String) map.get("activityId");
                Tran tran = new Tran();
                tran.setId(UUIDUtils.getUUID());
                tran.setOwner(user.getId());
                tran.setMoney(money);
                tran.setName(name);
                tran.setExpectedDate(exceptedDate);
                tran.setCustomerId(customer.getId());
                tran.setStage(stage);
                tran.setActivityId(activityId);
                tran.setContactsId(contacts.getId());
                tran.setCreateBy(user.getId());
                tran.setCreateTime(DateUtils.formatDataTime(new Date()));
                tranMapper.insertTran(tran);
                //9.把线索中的备注保存到交易中的备注
                if(clueRemarks!=null&&clueRemarks.size()!=0){
                    TranRemark tranRemark = null;
                    List<TranRemark> tranRemarkList = new ArrayList<>();
                    for (ClueRemark clueRemark : clueRemarks) {
                        tranRemark = new TranRemark();
                        tranRemark.setId(UUIDUtils.getUUID());
                        tranRemark.setNoteContent(clueRemark.getNoteContent());
                        tranRemark.setCreateBy(clueRemark.getCreateBy());
                        tranRemark.setCreateTime(clueRemark.getCreateTime());
                        tranRemark.setEditBy(clueRemark.getEditBy());
                        tranRemark.setEditTime(clueRemark.getEditTime());
                        tranRemark.setEditFlag(clueRemark.getEditFlag());
                        tranRemark.setTranId(tran.getId());
                        tranRemarkList.add(tranRemark);
                    }
                    tranRemarkMapper.insertTranRemarkList(tranRemarkList);
                }
            }
        //10.删除线索下的所有备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
            //11.删除clue和activity的关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //12.删除该线索
        clueMapper.deleteClueByClueId(clueId);
    }
}
