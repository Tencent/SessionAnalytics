/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.SessionEventEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * describe: 和弦图不去重分析
 *
 * @author author
 * @date 2023/02/25
 */
public interface ChordSessionEventRepository extends JpaRepository<SessionEventEntity, Integer> {

    /**
     * 获取和弦图session事件不去重结果
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " substring(substring_index(f_event_to,'_',-1),1,20) as event_to,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " group by event_from, event_to "
            + " having event_session>=?4 and event_session<=?5"
            + " and event_user>=?6 and event_user<=?7"
            + " and event_pv>=?8 and event_pv<=?9", nativeQuery = true)
    List<Map<String, Object>> getChordSessionEventEntity(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom, int fSessionNumStart, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd);



    /**
     * 获取和弦图分渠道事件汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_channel,substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " substring(substring_index(f_event_to,'_',-1),1,20) as event_to,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_event_to,'_',-1),1,20)=?5 "
            + " group by f_channel,event_from, event_to "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntByChannelTo(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA,String fEventToA);


    /**
     * 获取和弦图分渠道事件汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_channel,substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " group by f_channel,event_from "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntByChannelFrom(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA);


    /**
     * 获取和弦图分省份事件汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_province,substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " substring(substring_index(f_event_to,'_',-1),1,20) as event_to,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_event_to,'_',-1),1,20)=?5 "
            + " group by f_province,event_from, event_to "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntByProvinceTo(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA,String fEventToA);


    /**
     * 获取和弦图分省份事件汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_province,substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " group by f_province,event_from "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntByProvinceFrom(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA);


    /**
     * 获取和弦图分性别事件汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_sex,substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " substring(substring_index(f_event_to,'_',-1),1,20) as event_to,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_event_to,'_',-1),1,20)=?5 "
            + " group by f_sex,event_from, event_to "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntBySexTo(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA,String fEventToA);



    /**
     * 获取和弦图分性别事件汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_sex,substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " group by f_sex,event_from "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntBySexFrom(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA);



    /**
     * 获取和弦图分年龄事件汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " substring(substring_index(f_event_to,'_',-1),1,20) as event_to,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_event_to,'_',-1),1,20)=?5 "
            + " group by age,event_from, event_to "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntByAgeTo(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA,String fEventToA);



    /**
     * 获取和弦图分年龄事件汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " substring(substring_index(f_event_from,'_',-1),1,20) as event_from, "
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_event_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_event_from,'_',-1),1,20)=?4 "
            + " group by age,event_from "
            + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordEventCntByAgeFrom(String fUserSceneVersionId,
            int fMakeVersionId, String eventFrom,
            String fEventFromA);






    /**
     * 获取和弦图session大类不去重结果
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " substring(substring_index(f_category_to,'_',-1),1,20) as category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " group by category_from, category_to "
            + " having category_session>=?4 and category_session<=?5 "
            + " and category_user>=?6 and category_user<=?7"
            + " and category_pv>=?8 and category_pv<=?9 ", nativeQuery = true)
    List<Map<String, Object>> getChordSessionCategoryEntity(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom, int fSessionNumStart, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd);



    /**
     * 获取和弦图分渠道大类汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select f_channel,substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " substring(substring_index(f_category_to,'_',-1),1,20) as category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_category_to,'_',-1),1,20)=?5 "
            + " group by f_channel,category_from, category_to "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntByChannelTo(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA,String fCategoryToA);



    /**
     * 获取和弦图分渠道大类汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select f_channel,substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " group by f_channel,category_from "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntByChannelFrom(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA);



    /**
     * 获取和弦图分省份大类汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select f_province,substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " substring(substring_index(f_category_to,'_',-1),1,20) as category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_category_to,'_',-1),1,20)=?5 "
            + " group by f_province,category_from, category_to "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntByProvinceTo(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA,String fCategoryToA);


    /**
     * 获取和弦图分省份大类汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select f_province,substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " group by f_province,category_from "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntByProvinceFrom(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA);


    /**
     * 获取和弦图分性别大类汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select f_sex,substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " substring(substring_index(f_category_to,'_',-1),1,20) as category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_category_to,'_',-1),1,20)=?5 "
            + " group by f_sex,category_from, category_to "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntBySexTo(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA,String fCategoryToA);


    /**
     * 获取和弦图分性别大类汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select f_sex,substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " group by f_sex,category_from "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntBySexFrom(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA);




    /**
     * 获取和弦图分年龄大类汇总结果分布（to）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " substring(substring_index(f_category_to,'_',-1),1,20) as category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_category_to,'_',-1),1,20)=?5 "
            + " group by age,category_from, category_to "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntByAgeTo(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA,String fCategoryToA);



    /**
     * 获取和弦图分年龄大类汇总结果分布（from）不去重
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " substring(substring_index(f_category_from,'_',-1),1,20) as category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_category_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_category_from,'_',-1),1,20)=?4 "
            + " group by age,category_from "
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getChordCateCntByAgeFrom(String fUserSceneVersionId,
            int fMakeVersionId, String categoryFrom,
            String fCategoryFromA);


    /**
     * 获取和弦图小类不去重结果
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " substring(substring_index(f_subcategory_to,'_',-1),1,20) as subcategory_to,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " group by subcategory_from, subcategory_to "
            + " having subcategory_session>=?4 and subcategory_session<=?5 "
            + " and subcategory_user>=?6 and subcategory_user<=?7"
            + " and subcategory_pv>=?8 and subcategory_pv<=?9", nativeQuery = true)
    List<Map<String, Object>> getChordSessionSubcategoryEntity(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom, int fSessionNumStart, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd);


    /**
     * 获取和弦图分渠道小类汇总结果分布（to）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_channel,substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " substring(substring_index(f_subcategory_to,'_',-1),1,20) as subcategory_to,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_subcategory_to,'_',-1),1,20)=?5 "
            + " group by f_channel,subcategory_from, subcategory_to "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntByChannelTo(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA,String fSubcategoryToA);



    /**
     * 获取和弦图分渠道小类汇总结果分布（from）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_channel,substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv "
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " group by f_channel,subcategory_from "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntByChannelFrom(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA);


    /**
     * 获取和弦图分省份小类汇总结果分布（to）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " substring(substring_index(f_subcategory_to,'_',-1),1,20) as subcategory_to,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_subcategory_to,'_',-1),1,20)=?5 "
            + " group by f_province,subcategory_from, subcategory_to "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntByProvinceTo(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA,String fSubcategoryToA);



    /**
     * 获取和弦图分省份小类汇总结果分布（from）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " group by f_province,subcategory_from "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntByProvinceFrom(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA);



    /**
     * 获取和弦图分性别小类汇总结果分布（to）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_sex,substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " substring(substring_index(f_subcategory_to,'_',-1),1,20) as subcategory_to,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_subcategory_to,'_',-1),1,20)=?5 "
            + " group by f_sex,subcategory_from, subcategory_to "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntBySexTo(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA,String fSubcategoryToA);



    /**
     * 获取和弦图分性别小类汇总结果分布（from）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_sex,substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " group by f_sex,subcategory_from "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntBySexFrom(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA);


    /**
     * 获取和弦图分年龄小类汇总结果分布（to）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " substring(substring_index(f_subcategory_to,'_',-1),1,20) as subcategory_to,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " and substring(substring_index(f_subcategory_to,'_',-1),1,20)=?5 "
            + " group by age,subcategory_from, subcategory_to "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntByAgeTo(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA,String fSubcategoryToA);


    /**
     * 获取和弦图分年龄小类汇总结果分布（from）不去重
     *
     * @return
     */
    @Transactional
    @Query(value = "select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) as subcategory_from, "
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',"
            + " substring(substring_index(f_subcategory_from,'_',-1),1,20) like concat('%', ?3, '%'),1=1)\n"
            + " and substring(substring_index(f_subcategory_from,'_',-1),1,20)=?4 "
            + " group by age,subcategory_from "
            + " order by subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getChordSubCntByAgeFrom(String fUserSceneVersionId,
            int fMakeVersionId, String subcategoryFrom,
            String fSubcategoryFromA);

}
