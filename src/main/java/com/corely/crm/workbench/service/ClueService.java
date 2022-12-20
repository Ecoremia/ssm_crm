package com.corely.crm.workbench.service;

import com.corely.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    /**
     * 查询所有的线索
     */
    public List<Clue> selectAllClues();
    /**
     * 保存创建的clue
     */
    int insertCreateClue(Clue clue);
    /**
     * 按条件查询显示clue
     */
    List<Clue> selectClueByConditionForPage(Map<String,Object> map);
    /**
     * 按条件查询显示clue的条数
     */
    int selectTotalRowsOfClueByCondition(Map<String,Object> map);
    /**
     * 根据id查询一个clue的详细信息
     */
    Clue selectClueForDetailById(String id);
    /**
     * 保存转换的线索
     */
    void saveConvert(Map<String,Object> map);
}
