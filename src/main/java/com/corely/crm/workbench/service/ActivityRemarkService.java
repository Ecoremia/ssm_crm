package com.corely.crm.workbench.service;

import com.corely.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    /**
     * 根据activity的id查询备注信息
     */
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String activityID);
    /**
     * 添加创建的市场活动
     */
    int insertActivityRemark(ActivityRemark activityRemark);
    /**
     * 删除特定id的市场活动备注
     */
    int deleteActivityRemarkById(String id);
    /**
     * 更新修改后的市场活动
     */
    int updateActivityRemark(ActivityRemark activityRemark);
}
