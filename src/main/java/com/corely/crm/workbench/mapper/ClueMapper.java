package com.corely.crm.workbench.mapper;

import com.corely.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Sep 14 09:45:33 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Sep 14 09:45:33 CST 2022
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Sep 14 09:45:33 CST 2022
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Sep 14 09:45:33 CST 2022
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Sep 14 09:45:33 CST 2022
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Sep 14 09:45:33 CST 2022
     */
    int updateByPrimaryKey(Clue record);
    /**
     * 查询所有的线索
     */
    List<Clue> selectAllClues();
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
     * 根据id查询一个clue的详细信息（此表中的关联字段是名字）
     */
    Clue selectClueForDetailById(String id);
    /**
     * 根据clueid查询clue的详细信息（此表中的关联字段是id）
     */
    Clue selectClueForDetailAndIdsById(String clueId);
    /**
     * 根据clueid删除该线索
     */
    int deleteClueByClueId(String clueId);
}
