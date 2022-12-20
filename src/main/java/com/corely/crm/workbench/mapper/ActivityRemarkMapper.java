package com.corely.crm.workbench.mapper;

import com.corely.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Sep 11 09:50:51 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Sep 11 09:50:51 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Sep 11 09:50:51 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Sep 11 09:50:51 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Sep 11 09:50:51 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);
    /**
     * 根据activity的id查询备注信息
     */
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String activityId);
    /**
     * 添加市场活动的备注
     */
    int insertActivityRemark(ActivityRemark activityRemark);
    /**
     * 删除特定id的市场活动的备注
     */
    int deleteActivityRemarkById(String id);
    /**
     * 更新修改后的市场活动
     */
    int updateActivityRemark(ActivityRemark activityRemark);
}
