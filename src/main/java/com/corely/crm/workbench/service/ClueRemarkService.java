package com.corely.crm.workbench.service;

import com.corely.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    /**
     * 根据clueid获取其信息
     */
    List<ClueRemark> selectClueRemarksByClueId(String id);
    /**
     * 保存备注
     */
    int insertCreateClueRemark(ClueRemark clueRemark);
}
