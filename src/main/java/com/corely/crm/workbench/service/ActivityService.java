package com.corely.crm.workbench.service;

import com.corely.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    /**
     * 生成一条新的市场活动记录
     */
    int insertActivity(Activity activity);

    /**
     * 根据条件分页查询市场活动列表信息
     * @param map
     * @return
     */
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询市场活动的总条数
     * @param map
     * @return
     */
    int queryCountOfActivityByCondition(Map<String,Object> map);
    /**
     * 批量删除指定id的市场活动
     */
    int deleteActivityByIds(String[] ids);
    /**
     * 查询指定id的市场活动
     */
    Activity selectActivityById(String id);
    /**
     * 保存修改后的市场活动
     */
    int updateActivity(Activity activity);
    /**
     * 查询所有的市场活动
     */
    List<Activity> selectAllActivities();
    /**
     * 根据id查询部分市场活动
     */
    List<Activity> selectActivitiesByIds(String[] ids);
    /**
     * 添加市场活动的list
     */
    int insertActivitiesByList(List<Activity> activities);
    /**
     * 查询详细的市场活动信息
     */
    Activity selectActivityForDetailById(String id);
    /**
     * 根据clueid获取对应的activity
     */
    List<Activity> selectActivityForDetailByClueId(String id);
    /**
     * 根据name（模糊查询）和clueid查询市场活动
     */
    List<Activity> selectActivityForDetailByNameAndClueId(Map<String,Object> map);
    /**
     * 根据市场活动的name进行【模糊查询】,并且是跟当前clue关联过的市场活动
     */
    List<Activity> selectActivityForDetailByName(Map<String,Object> map);
}
