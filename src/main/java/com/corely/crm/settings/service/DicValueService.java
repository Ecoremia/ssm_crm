package com.corely.crm.settings.service;

import com.corely.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueService {
    /**
     * 根据code值查询value列表
     */
    public List<DicValue> selectDicValueByTypeCode(String typeCode);
}
