/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
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

/**
 * describe: 漏斗图不去重分析
 *
 * @author author
 * @date 2023/02/25
 */
public interface FunnelSessionEventRepository extends JpaRepository<SessionEventEntity, Integer> {

    /**
     * 获取漏斗图event不去重结果(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_event,event_session,event_user,event_pv from(\n"
            + "            select f_event_from as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5\n"
            + " and f_make_version_id=?6 and f_event_from=?1\n"
            + "    group by f_event_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?1\n"
            + "    and f_event_to=?2\n"
            + "    group by f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?2\n"
            + "    and f_event_to=?3\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?3\n"
            + "    and f_event_to=?4\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_event_to\n"
            + ")t\n"
            + "    order by substring(substring_index(f_event,'_',1),3,10) asc,event_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionEventEntity(String fEventFrom1, String fEventFrom2,
            String fEventFrom3, String fEventFrom4, String fUserSceneVersionId, int fMakeVersionId);




    /**
     * 获取漏斗图event不去重结果(层级1234)分渠道
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_channel,f_event,event_session,event_user,event_pv from(\n"
            + "            select f_channel,f_event_from as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5\n"
            + " and f_make_version_id=?6 and f_event_from=?1\n"
            + "    group by f_channel,f_event_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?1\n"
            + "    and f_event_to=?2\n"
            + "    group by f_channel,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?2\n"
            + "    and f_event_to=?3\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_channel,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?3\n"
            + "    and f_event_to=?4\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_channel,f_event_to\n"
            + ")t\n"
            + "    where f_event=?7\n"
            + "    order by substring(substring_index(f_event,'_',1),3,10) asc,event_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelEventCntByChannelTo(String fEventFrom1, String fEventFrom2,
            String fEventFrom3, String fEventFrom4, String fUserSceneVersionId, int fMakeVersionId,String fEventFromA);


    /**
     * 获取漏斗图event不去重结果(层级1234)分省份
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_province,f_event,event_session,event_user,event_pv from(\n"
            + "            select f_province,f_event_from as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5\n"
            + " and f_make_version_id=?6 and f_event_from=?1\n"
            + "    group by f_province,f_event_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?1\n"
            + "    and f_event_to=?2\n"
            + "    group by f_province,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?2\n"
            + "    and f_event_to=?3\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_province,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?3\n"
            + "    and f_event_to=?4\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_province,f_event_to\n"
            + ")t\n"
            + "    where f_event=?7\n"
            + "    order by substring(substring_index(f_event,'_',1),3,10) asc,event_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelEventCntByProvinceTo(String fEventFrom1, String fEventFrom2,
            String fEventFrom3, String fEventFrom4, String fUserSceneVersionId, int fMakeVersionId,String fEventFromA);



    /**
     * 获取漏斗图event不去重结果(层级1234)分性别
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_sex,f_event,event_session,event_user,event_pv from(\n"
            + "            select f_sex,f_event_from as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5\n"
            + " and f_make_version_id=?6 and f_event_from=?1\n"
            + "    group by f_sex,f_event_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?1\n"
            + "    and f_event_to=?2\n"
            + "    group by f_sex,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?2\n"
            + "    and f_event_to=?3\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_sex,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?3\n"
            + "    and f_event_to=?4\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_sex,f_event_to\n"
            + ")t\n"
            + "    where f_event=?7\n"
            + "    order by substring(substring_index(f_event,'_',1),3,10) asc,event_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelEventCntBySexTo(String fEventFrom1, String fEventFrom2,
            String fEventFrom3, String fEventFrom4, String fUserSceneVersionId, int fMakeVersionId,String fEventFromA);



    /**
     * 获取漏斗图event不去重结果(层级1234)分年龄
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select age,f_event,event_session,event_user,event_pv from(\n"
            + "            select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_event_from as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5\n"
            + " and f_make_version_id=?6 and f_event_from=?1\n"
            + "    group by age,f_event_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?1\n"
            + "    and f_event_to=?2\n"
            + "    group by age,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?2\n"
            + "    and f_event_to=?3\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by age,f_event_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_event_to as f_event,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_event_from=?3\n"
            + "    and f_event_to=?4\n"
            + "    and f_event_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by age,f_event_to\n"
            + ")t\n"
            + "    where f_event=?7\n"
            + "    order by substring(substring_index(f_event,'_',1),3,10) asc,event_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelEventCntByAgeTo(String fEventFrom1, String fEventFrom2,
            String fEventFrom3, String fEventFrom4, String fUserSceneVersionId, int fMakeVersionId,String fEventFromA);



    /**
     * 获取漏斗图大类不去重结果（层级1234）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_category,category_session,category_user,category_pv from(\n"
            + "            select f_category_from as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    group by f_category_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    and f_category_to=?2\n"
            + "    group by f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?2\n"
            + "    and f_category_to=?3\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?3\n"
            + "    and f_category_to=?4\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_category_to\n"
            + ")t\n"
            + "order by substring(substring_index(f_category,'_',1),3,10) asc,"
            + " category_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionCategoryEntity(
            String fCategoryFrom1, String fCategoryFrom2, String fCategoryFrom3, String fCategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId);




    /**
     * 获取漏斗图大类不去重结果（层级1234）分渠道
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_channel,f_category,category_session,category_user,category_pv from(\n"
            + "            select f_channel,f_category_from as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    group by f_channel,f_category_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    and f_category_to=?2\n"
            + "    group by f_channel,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?2\n"
            + "    and f_category_to=?3\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_channel,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?3\n"
            + "    and f_category_to=?4\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_channel,f_category_to\n"
            + ")t\n"
            + "    where f_category=?7\n"
            + "order by substring(substring_index(f_category,'_',1),3,10) asc,"
            + " category_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelCateCntByChannelTo(
            String fCategoryFrom1, String fCategoryFrom2, String fCategoryFrom3, String fCategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId,String fCategoryFromA);



    /**
     * 获取漏斗图大类不去重结果（层级1234）分省份
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_province,f_category,category_session,category_user,category_pv from(\n"
            + "            select f_province,f_category_from as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    group by f_province,f_category_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    and f_category_to=?2\n"
            + "    group by f_province,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?2\n"
            + "    and f_category_to=?3\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_province,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?3\n"
            + "    and f_category_to=?4\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_province,f_category_to\n"
            + ")t\n"
            + "    where f_category=?7\n"
            + "order by substring(substring_index(f_category,'_',1),3,10) asc,"
            + " category_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelCateCntByProvinceTo(
            String fCategoryFrom1, String fCategoryFrom2, String fCategoryFrom3, String fCategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId,String fCategoryFromA);




    /**
     * 获取漏斗图大类不去重结果（层级1234）分性别
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_sex,f_category,category_session,category_user,category_pv from(\n"
            + "            select f_sex,f_category_from as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    group by f_sex,f_category_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    and f_category_to=?2\n"
            + "    group by f_sex,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?2\n"
            + "    and f_category_to=?3\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_sex,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?3\n"
            + "    and f_category_to=?4\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_sex,f_category_to\n"
            + ")t\n"
            + "    where f_category=?7\n"
            + "order by substring(substring_index(f_category,'_',1),3,10) asc,"
            + " category_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelCateCntBySexTo(
            String fCategoryFrom1, String fCategoryFrom2, String fCategoryFrom3, String fCategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId,String fCategoryFromA);




    /**
     * 获取漏斗图大类不去重结果（层级1234）分年龄
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select age,f_category,category_session,category_user,category_pv from(\n"
            + "            select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_category_from as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    group by age,f_category_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?1\n"
            + "    and f_category_to=?2\n"
            + "    group by age,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?2\n"
            + "    and f_category_to=?3\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by age,f_category_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_category_to as f_category,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_category_from=?3\n"
            + "    and f_category_to=?4\n"
            + "    and f_category_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by age,f_category_to\n"
            + ")t\n"
            + "    where f_category=?7\n"
            + "order by substring(substring_index(f_category,'_',1),3,10) asc,"
            + " category_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelCateCntByAgeTo(
            String fCategoryFrom1, String fCategoryFrom2, String fCategoryFrom3, String fCategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId,String fCategoryFromA);



    /**
     * 获取漏斗图小类不去重结果（层级1234）
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_subcategory,subcategory_session,subcategory_user,subcategory_pv from(\n"
            + "            select f_subcategory_from as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    group by f_subcategory_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    and f_subcategory_to=?2\n"
            + "    group by f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?2\n"
            + "    and f_subcategory_to=?3\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "  select f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?3\n"
            + "    and f_subcategory_to=?4\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_subcategory_to\n"
            + ")t\n"
            + "order by substring(substring_index(f_subcategory,'_',1),3,10) asc,"
            + "subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionSubcategoryEntity(String fSubcategoryFrom1, String fSubcategoryFrom2,
            String fSubcategoryFrom3, String fSubcategoryFrom4, String fUserSceneVersionId, int fMakeVersionId);



    /**
     * 获取漏斗图小类不去重结果（层级1234）分渠道
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_channel,f_subcategory,subcategory_session,subcategory_user,subcategory_pv from(\n"
            + "            select f_channel,f_subcategory_from as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    group by f_channel,f_subcategory_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    and f_subcategory_to=?2\n"
            + "    group by f_channel,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_channel,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?2\n"
            + "    and f_subcategory_to=?3\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_channel,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "  select f_channel,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?3\n"
            + "    and f_subcategory_to=?4\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_channel,f_subcategory_to\n"
            + ")t\n"
            + "    where f_subcategory=?7\n"
            + "order by substring(substring_index(f_subcategory,'_',1),3,10) asc,"
            + "subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelSubCntByChannelTo(
            String fSubcategoryFrom1, String fSubcategoryFrom2, String fSubcategoryFrom3, String fSubcategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId, String fSubcategoryFromA);



    /**
     * 获取漏斗图小类不去重结果（层级1234）分省份
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,f_subcategory,subcategory_session,subcategory_user,subcategory_pv from(\n"
            + "            select f_province,f_subcategory_from as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    group by f_province,f_subcategory_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    and f_subcategory_to=?2\n"
            + "    group by f_province,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_province,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?2\n"
            + "    and f_subcategory_to=?3\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_province,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "  select f_province,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?3\n"
            + "    and f_subcategory_to=?4\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_province,f_subcategory_to\n"
            + ")t\n"
            + "    where f_subcategory=?7\n"
            + "order by substring(substring_index(f_subcategory,'_',1),3,10) asc,"
            + "subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelSubCntByProvinceTo(
            String fSubcategoryFrom1, String fSubcategoryFrom2, String fSubcategoryFrom3, String fSubcategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId, String fSubcategoryFromA);



    /**
     * 获取漏斗图小类不去重结果（层级1234）分性别
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_sex,f_subcategory,subcategory_session,subcategory_user,subcategory_pv from(\n"
            + "            select f_sex,f_subcategory_from as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    group by f_sex,f_subcategory_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    and f_subcategory_to=?2\n"
            + "    group by f_sex,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select f_sex,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?2\n"
            + "    and f_subcategory_to=?3\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by f_sex,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "  select f_sex,f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?3\n"
            + "    and f_subcategory_to=?4\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by f_sex,f_subcategory_to\n"
            + ")t\n"
            + "    where f_subcategory=?7\n"
            + "order by substring(substring_index(f_subcategory,'_',1),3,10) asc,"
            + "subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelSubCntBySexTo(
            String fSubcategoryFrom1, String fSubcategoryFrom2, String fSubcategoryFrom3, String fSubcategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId, String fSubcategoryFromA);




    /**
     * 获取漏斗图小类不去重结果（层级1234）分年龄
     *
     * @return
     */
    @Transactional
    @Query(value = "select age,f_subcategory,subcategory_session,subcategory_user,subcategory_pv from(\n"
            + "            select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_subcategory_from as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    group by age,f_subcategory_from\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?1\n"
            + "    and f_subcategory_to=?2\n"
            + "    group by age,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "    select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?2\n"
            + "    and f_subcategory_to=?3\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'%')\n"
            + "    group by age,f_subcategory_to\n"
            + "\n"
            + "    union all\n"
            + "\n"
            + "  select "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " f_subcategory_to as f_subcategory,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from  t_session_event_sankey\n"
            + "    where f_user_scene_version_id=?5 and f_make_version_id=?6 and f_subcategory_from=?3\n"
            + "    and f_subcategory_to=?4\n"
            + "    and f_subcategory_path like concat('%',?1,'/',?2,'/',?3,'/',?4,'%')\n"
            + "    group by age,f_subcategory_to\n"
            + ")t\n"
            + "    where f_subcategory=?7\n"
            + "order by substring(substring_index(f_subcategory,'_',1),3,10) asc,"
            + "subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getFunnelSubCntByAgeTo(
            String fSubcategoryFrom1, String fSubcategoryFrom2, String fSubcategoryFrom3, String fSubcategoryFrom4,
            String fUserSceneVersionId, int fMakeVersionId, String fSubcategoryFromA);



    /**
     * 获取session event 去重category list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_category_from from t_session_event_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
            + " and substring(substring_index(f_category_from,'_',1),3,10)=1", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionCategoryList1(String fUserSceneVersionId, int fMakeVersionId);

    /**
     * 获取session event 去重category list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_category_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_category_from,'_',1),3,10)=2"
            + " and f_category_path like concat(?3,'%') ", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionCategoryList2(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom1);


    /**
     * 获取session event 去重category list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_category_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_category_from,'_',1),3,10)=3"
            + " and f_category_path like concat('%',?3,'/',?4,'%') ", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionCategoryList3(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom1, String fCategoryFrom2);


    /**
     * 获取session event 去重category list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_category_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_category_from,'_',1),3,10)=4"
            + " and f_category_path like concat('%',?3,'/',?4,'/',?5,'%') ", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionCategoryList4(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom1, String fCategoryFrom2, String fCategoryFrom3);


    /**
     * 获取session event 去重subcategory list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_subcategory_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 "
            + " and substring(substring_index(f_subcategory_from,'_',1),3,10)=1", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionSubcategoryList1(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取session event 去重subcategory list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_subcategory_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_subcategory_from,'_',1),3,10)=2"
            + " and f_subcategory_path like concat(?3,'%') ", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionSubcategoryList2(String fUserSceneVersionId, int fMakeVersionId,
            String fSubcategoryFrom1);


    /**
     * 获取session event 去重subcategory list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_subcategory_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_subcategory_from,'_',1),3,10)=3"
            + " and f_subcategory_path like concat('%',?3,'/',?4,'%') ", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionSubcategoryList3(String fUserSceneVersionId, int fMakeVersionId,
            String fSubcategoryFrom1, String fSubcategoryFrom2);


    /**
     * 获取session event 去重subcategory list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_subcategory_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_subcategory_from,'_',1),3,10)=4"
            + " and f_subcategory_path like concat('%',?3,'/',?4,'/',?5,'%') ", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionSubcategoryList4(String fUserSceneVersionId, int fMakeVersionId,
            String fSubcategoryFrom1, String fSubcategoryFrom2, String fSubcategoryFrom3);


    /**
     * 获取session event 去重event list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_event_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_event_from,'_',1),3,10)=1", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionEventList1(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取session event 去重event list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_event_from from t_session_event_sankey where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_event_from,'_',1),3,10)=2"
            + " and f_event_path like concat(?3,'%')", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionEventList2(String fUserSceneVersionId,
            int fMakeVersionId, String fEventFrom1);


    /**
     * 获取session event 去重event list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_event_from from t_session_event_sankey "
            + " where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_event_from,'_',1),3,10)=3"
            + " and f_event_path like concat('%',?3,'/',?4,'%')", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionEventList3(String fUserSceneVersionId,
            int fMakeVersionId, String fEventFrom1, String fEventFrom2);


    /**
     * 获取session event 去重event list(层级1234)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct f_event_from from t_session_event_sankey "
            + " where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and substring(substring_index(f_event_from,'_',1),3,10)=4"
            + " and f_event_path like concat('%',?3,'/',?4,'/',?5,'%')", nativeQuery = true)
    List<Map<String, Object>> getFunnelSessionEventList4(String fUserSceneVersionId,
            int fMakeVersionId, String fEventFrom1, String fEventFrom2, String fEventFrom3);


}
