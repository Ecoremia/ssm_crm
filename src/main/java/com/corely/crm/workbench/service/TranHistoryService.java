package com.corely.crm.workbench.service;

import com.corely.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {
    /**
     * 根据tranid查询所有的历史记录
     */
    List<TranHistory> selectTranHistoryForDetailByTranId(String id);
}
