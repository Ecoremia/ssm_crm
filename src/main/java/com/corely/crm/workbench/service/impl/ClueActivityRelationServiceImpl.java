package com.corely.crm.workbench.service.impl;

import com.corely.crm.workbench.domain.ClueActivityRelation;
import com.corely.crm.workbench.mapper.ClueActivityRelationMapper;
import com.corely.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Override
    public int insertClueActivityRelationList(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertClueActivityRelationList(list);
    }

    @Override
    public int deleteClueActivityRelationList(ClueActivityRelation relation) {
        return clueActivityRelationMapper.deleteClueActivityRelationList(relation);
    }
}
