package com.corely.crm.workbench.service;

import com.corely.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    /**
     * 批量保存clue和activity的关系
     */
    int insertClueActivityRelationList(List<ClueActivityRelation> list);
    /**
     * 解除关联
     */
    int deleteClueActivityRelationList(ClueActivityRelation relation);
}
