/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.LogEventEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: 年龄维度session分布
 *
 * @author author
 * @date 2021/11/23
 */
public interface LogEventAgeRepository extends JpaRepository<LogEventEntity, Integer> {


    /**
     * 获取年龄段列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select distinct "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 ", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgeList(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取年龄段session数分布
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select "
                    + " case \n"
                    + " when f_age > 0 and f_age < 20 then '0-20' \n"
                    + " when f_age >= 20 and f_age < 30 then '20-30' \n"
                    + " when f_age >= 30 and f_age < 40 then '30-40' \n"
                    + " when f_age >= 40 and f_age < 50 then '40-50' \n"
                    + " when f_age >= 50 and f_age < 60 then '50-60' \n"
                    + " when f_age >= 60 then '60+' \n"
                    + " else '未知' \n"
                    + " end as age, "
                    + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
                    + " from t_session_log_event "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
                    + " group by age order by session_num desc ", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByAgeSessionNUM(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取年龄段pv数分布
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select "
                    + " case \n"
                    + " when f_age > 0 and f_age < 20 then '0-20' \n"
                    + " when f_age >= 20 and f_age < 30 then '20-30' \n"
                    + " when f_age >= 30 and f_age < 40 then '30-40' \n"
                    + " when f_age >= 40 and f_age < 50 then '40-50' \n"
                    + " when f_age >= 50 and f_age < 60 then '50-60' \n"
                    + " when f_age >= 60 then '60+' \n"
                    + " else '未知' \n"
                    + " end as age, "
                    + " count(1) as pv_num "
                    + " from t_session_log_event "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
                    + " group by age order by pv_num desc ", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByAgePVNum(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取年龄段user数分布
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select "
                    + " case \n"
                    + " when f_age > 0 and f_age < 20 then '0-20' \n"
                    + " when f_age >= 20 and f_age < 30 then '20-30' \n"
                    + " when f_age >= 30 and f_age < 40 then '30-40' \n"
                    + " when f_age >= 40 and f_age < 50 then '40-50' \n"
                    + " when f_age >= 50 and f_age < 60 then '50-60' \n"
                    + " when f_age >= 60 then '60+' \n"
                    + " else '未知' \n"
                    + " end as age, "
                    + " count(distinct f_user_id) as user_num "
                    + " from t_session_log_event "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
                    + " group by age order by user_num desc", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByAgeUserNum(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取年龄段user数分布 时间趋势 （可筛选年龄段）
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,age,user_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " case \n"
            + " when f_age >0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " count(distinct f_user_id) as user_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date,age)t where age in (?3) "
            + " group by p_date,age order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgeUserCntTrend(String fUserSceneVersionId,
            int fMakeVersionId, List<String> age);


    /**
     * 获取年龄段user数分布 时间趋势（展示全部年龄段）
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,age,user_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " count(distinct f_user_id) as user_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date,age)t"
            + " group by p_date,age order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgeUserCntTrendALL(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取年龄段session数分布 时间趋势 （可筛选年龄段）
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,age,session_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date,age)t where age in (?3) "
            + " group by p_date,age order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgeSessionCntTrend(String fUserSceneVersionId,
            int fMakeVersionId, List<String> age);


    /**
     * 获取年龄段session数分布 时间趋势（展示全部年龄段）
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,age,session_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date,age)t "
            + " group by p_date,age order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgeSessionCntTrendALL(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取年龄段session数分布 全部时间趋势(折线)
     *
     * @return
     */
    @Transactional
    @Query(value = " select substring(f_client_time,1,10) as p_date,'all' as age,"
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date  "
            + " order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgeCntTrendALLZHE(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取年龄段session数分布 时间趋势(折线-可筛选年龄段)
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,'all' as age,count(distinct session_id) as session_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + " concat(f_user_id,'_&_',f_session_id) as session_id "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2)t where age in (?3) "
            + " group by p_date order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgeSessionCntTrendZHE(String fUserSceneVersionId,
            int fMakeVersionId, List<String> age);


    /**
     * 获取年龄段pv数分布 时间趋势
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,age,pv_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + "count(1) as pv_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date,age)t where age in (?3) "
            + " group by p_date,age order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgePvCntTrend(String fUserSceneVersionId,
            int fMakeVersionId, List<String> age);


    /**
     * 获取年龄段pv数分布 时间趋势（展示全部年龄段）
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,age,pv_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " case \n"
            + " when f_age > 0 and f_age < 20 then '0-20' \n"
            + " when f_age >= 20 and f_age < 30 then '20-30' \n"
            + " when f_age >= 30 and f_age < 40 then '30-40' \n"
            + " when f_age >= 40 and f_age < 50 then '40-50' \n"
            + " when f_age >= 50 and f_age < 60 then '50-60' \n"
            + " when f_age >= 60 then '60+' \n"
            + " else '未知' \n"
            + " end as age, "
            + "count(1) as pv_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date,age)t "
            + " group by p_date,age order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogAgePvCntTrendALL(String fUserSceneeVrsionId, int fMakeVersionId);



    /**
     * 获取年龄段分布(session、user、平均session、最大session、最小session、50分位session)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "age,\n"
            + "sum(session_num) as session_num,\n"
            + "sum(user_num) as user_num,\n"
            + "sum(session_avg) as session_avg,\n"
            + "sum(session_max) as session_max,\n"
            + "sum(session_min) as session_min,\n"
            + "sum(session_50p) as session_50p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "age,\n"
            + "sum(session_cnt) as session_num,\n"
            + "count(f_user_id) as user_num,\n"
            + "sum(session_cnt) div count(f_user_id) as session_avg,\n"
            + "max(session_cnt) as session_max,\n"
            + "min(session_cnt) as session_min,\n"
            + "0 as session_50p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age, f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by age,f_user_id\n"
            + ")t1\n"
            + "group by age\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "a3.age as age,\n"
            + "0 as session_num,\n"
            + "0 as user_num,\n"
            + "0 as session_avg,\n"
            + "0 as session_max,\n"
            + "0 as session_min,\n"
            + "session_cnt as session_50\n"
            + "from\n"
            + "(\n"
            + "(\n"
            + "select a1.f_user_id,a1.age,a1.session_cnt,sum(case when a2.age=a1.age then 1 else 0 end) "
            + "as group_rank \n"
            + "from \n"
            + "(\n"
            + "select f_user_id,case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id,age) a1 ,\n"
            + "(select f_user_id,case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id,age) a2\n"
            + "where a1.session_cnt > a2.session_cnt\n"
            + "or (a1.session_cnt = a2.session_cnt and a1.f_user_id <= a2.f_user_id)\n"
            + "group by a1.f_user_id,a1.age,a1.session_cnt\n"
            + "order by group_rank\n"
            + ") a3 \n"
            + "join\n"
            + "(select case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age,(count(distinct f_user_id) + 1) DIV 2 as group_rank_pct50\n"
            + "from t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by age\n"
            + ")a4 \n"
            + "on a3.age=a4.age and a3.group_rank=a4.group_rank_pct50\n"
            + ")\n"
            + "\n"
            + ")t\n"
            + "group by age", nativeQuery = true)
    List<Map<String, Object>> getLogSessionAgeDistribute(String fUserSceneVersionId, int fMakeVersionId);



    /**
     * 获取年龄段分布(session-pv、user-pv、平均session-pv、最大session-pv、最小session-pv、50分位session-pv)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "age,\n"
            + "sum(session_pv_num) as session_pv_num,\n"
            + "sum(user_pv_num) as user_pv_num,\n"
            + "sum(session_pv_avg) as session_pv_avg,\n"
            + "sum(session_pv_max) as session_pv_max,\n"
            + "sum(session_pv_min) as session_pv_min,\n"
            + "sum(session_pv_50p) as session_pv_50p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "age,\n"
            + "sum(session_pv) as session_pv_num,\n"
            + "count(f_userid_sessionid) as user_pv_num,\n"
            + "sum(session_pv) div count(f_userid_sessionid) as session_pv_avg,\n"
            + "max(session_pv) as session_pv_max,\n"
            + "min(session_pv) as session_pv_min,\n"
            + "0 as session_pv_50p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age, concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by age,f_userid_sessionid\n"
            + ")t1\n"
            + "group by age\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "a3.age as age,\n"
            + "0 as session_pv_num,\n"
            + "0 as user_pv_num,\n"
            + "0 as session_pv_avg,\n"
            + "0 as session_pv_max,\n"
            + "0 as session_pv_min,\n"
            + "session_pv as session_pv_50p\n"
            + "from\n"
            + "(\n"
            + "(\n"
            + "select a1.f_userid_sessionid,a1.age,a1.session_pv,sum(case when a2.age=a1.age then 1 else 0 end) "
            + "as group_rank \n"
            + "from \n"
            + "(\n"
            + "select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid,age) a1 ,\n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid,age) a2\n"
            + "where a1.session_pv > a2.session_pv\n"
            + "or (a1.session_pv = a2.session_pv and a1.f_userid_sessionid <= a2.f_userid_sessionid)\n"
            + "group by a1.f_userid_sessionid,a1.age,a1.session_pv\n"
            + "order by group_rank\n"
            + ") a3 \n"
            + "join\n"
            + "(select case \n"
            + "    when f_age > 0 and f_age < 20 then '0-20' \n"
            + "    when f_age >= 20 and f_age < 30 then '20-30' \n"
            + "     when f_age >= 30 and f_age < 40 then '30-40' \n"
            + "     when f_age >= 40 and f_age < 50 then '40-50' \n"
            + "     when f_age >= 50 and f_age < 60 then '50-60' \n"
            + "     when f_age >= 60 then '60+' \n"
            + "     else '未知' \n"
            + "     end as age,((count(distinct concat(f_user_id,'_&_',f_session_id)) + 1) DIV 2) as group_rank_pct50\n"
            + "from t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by age\n"
            + ")a4 \n"
            + "on a3.age=a4.age and a3.group_rank=a4.group_rank_pct50\n"
            + ")\n"
            + "\n"
            + ")t\n"
            + "group by age", nativeQuery = true)
    List<Map<String, Object>> getLogPVAgeDistribute(String fUserSceneVersionId, int fMakeVersionId);

}
