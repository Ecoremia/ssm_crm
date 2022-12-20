package com.corely.crm.workbench.mapper;

import com.corely.crm.workbench.domain.FunnelVO;
import com.corely.crm.workbench.domain.Tran;

import java.util.List;

public interface TranMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Thu Sep 29 16:45:15 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Thu Sep 29 16:45:15 CST 2022
     */
    int insert(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Thu Sep 29 16:45:15 CST 2022
     */
    int insertSelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Thu Sep 29 16:45:15 CST 2022
     */
    Tran selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Thu Sep 29 16:45:15 CST 2022
     */
    int updateByPrimaryKeySelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Thu Sep 29 16:45:15 CST 2022
     */
    int updateByPrimaryKey(Tran record);
    /**
     * 创建一个交易
     */
    int insertTran(Tran tran);
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
