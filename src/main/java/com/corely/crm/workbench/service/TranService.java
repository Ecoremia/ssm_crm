package com.corely.crm.workbench.service;

import com.corely.crm.settings.domain.User;
import com.corely.crm.workbench.domain.Contacts;
import com.corely.crm.workbench.domain.FunnelVO;
import com.corely.crm.workbench.domain.Tran;

import java.util.List;

public interface TranService {
    /**
     * 根据联系人的名称模糊查询联系人
     */
    List<Contacts> selectContactsByName(String fullname);
    /**
     * 保存一份交易
     */
    void saveCreateTran(Tran tran, User user);
    /**
     * 为交易的主页面查询所有交易
     */
    List<Tran> selectAllTransForIndex();
    /**
     * 查询交易的所有信息
     */
    Tran selectTranForDetail(String id);
    /**
     * 查询所有交易的阶段以及其数量
     */
    List<FunnelVO> queryCountOfTranGroupByStage();
}
