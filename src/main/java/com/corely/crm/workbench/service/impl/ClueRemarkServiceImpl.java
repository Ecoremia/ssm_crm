package com.corely.crm.workbench.service.impl;

import com.corely.crm.workbench.domain.ClueRemark;
import com.corely.crm.workbench.mapper.ClueRemarkMapper;
import com.corely.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Override
    public List<ClueRemark> selectClueRemarksByClueId(String id) {
        return clueRemarkMapper.selectClueRemarksByClueId(id);
    }

    @Override
    public int insertCreateClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertCreateClueRemark(clueRemark);
    }
}
