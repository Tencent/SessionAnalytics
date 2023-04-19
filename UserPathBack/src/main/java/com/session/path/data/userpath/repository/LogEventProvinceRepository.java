/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
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
 * describe: 分省份维度session分布
 *
 * @author author
 * @date 2022/11/23
 */
public interface LogEventProvinceRepository extends JpaRepository<LogEventEntity, Integer> {


    /**
     * 获取省份列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province from (select  f_province,count(*) as cnt from t_session_log_event "
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2 group by f_province order by cnt desc)a ",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogProvinceList(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份user数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,count(distinct f_user_id) as user_num"
            + " from t_session_log_event where f_user_scene_version_id=?1"
            + " and f_make_version_id=?2 group by f_province order by user_num desc", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByProvinceUserNum(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份session数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,"
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num"
            + " from t_session_log_event where f_user_scene_version_id=?1"
            + " and f_make_version_id=?2 group by f_province order by session_num desc", nativeQuery = true)
    List<Map<String, Object>> getLogCntByProvinceSessionNum(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份平均session数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,"
            + " count( distinct concat(f_user_id,'_&_',f_session_id) )/count(distinct f_user_id) as avg_session_num "
            + " from t_session_log_event where f_user_scene_version_id=?1"
            + " and f_make_version_id=?2 group by f_province order by avg_session_num desc", nativeQuery = true)
    List<Map<String, Object>> getLogCntByProvinceAvgSessionNum(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份平均pv数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,"
            + " count(1)/count(distinct f_user_id) as avg_pv_num"
            + " from t_session_log_event where f_user_scene_version_id=?1"
            + " and f_make_version_id=?2 group by f_province order by avg_pv_num desc", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByProvinceAvgPVNum(String fUserSceneVersionId,int fMakeVersionId);




    /**
     * 获取省份pv数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_province,count(1) as pv_num "
            + " from t_session_log_event where f_user_scene_version_id=?1"
            + " and f_make_version_id=?2 group by f_province order by pv_num desc", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByProvincePVNum(String fUserSceneVersionId,int fMakeVersionId);



    /**
     * 获取省份user数分布 时间趋势 （可筛选省份）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_province, "
            + " count(distinct f_user_id) as user_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_province in (?3) "
            + " group by substring(f_client_time,1,10),f_province order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogProvinceUserCntTrend(String fUserSceneVersionId,int fMakeVersionId,
            List<String> fProvince);


    /**
     * 获取省份user数分布 时间趋势 （展示全部省份）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_province, "
            + " count(distinct f_user_id) as user_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2"
            + " group by substring(f_client_time,1,10),f_province order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getLogProvinceUserCntTrendALL(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份session数分布 时间趋势 （可筛选省份）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_province, "
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_province in (?3) "
            + " group by substring(f_client_time,1,10),f_province order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getLogProvinceSessionCntTrend(String fUserSceneVersionId,int fMakeVersionId,
            List<String> fProvince);


    /**
     * 获取省份session数分布 时间趋势(折线-可筛选省份)
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,'all' as f_province,count(distinct session_id) as session_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " f_province, "
            + " concat(f_user_id,'_&_',f_session_id) as session_id "
            + " from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2)t "
            + " where f_province in (?3) "
            + " group by p_date order by p_date", nativeQuery = true)
    List<Map<String, Object>> getLogProvinceSessionCntTrendZHE(String fUserSceneVersionId,int fMakeVersionId,
            List<String> fProvince);


    /**
     * 获取省份session数分布 时间趋势 （展示全部省份）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_province, "
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2"
            + " group by substring(f_client_time,1,10),f_province order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getLogProvinceSessionCntTrendALL(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份session数分布 全部时间趋势
     *
     * @return
     */
    @Transactional
    @Query(value = " select substring(f_client_time,1,10) as p_date,'all' as f_province,"
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date  "
            + " order by p_date", nativeQuery = true)
    List<Map<String, Object>> getLogProvinceCntTrendALLZHE(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份pv数分布 时间趋势(可筛选省份)
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_province, "
            + "count(1) as pv_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_province in (?3) "
            + " group by substring(f_client_time,1,10),f_province "
            + " order by substring(f_client_time,1,10)", nativeQuery = true)
    List<Map<String, Object>> getSessionLogProvincePvCntTrend(String fUserSceneVersionId,
            int fMakeVersionId, List<String> fProvince);


    /**
     * 获取省份pv数分布 时间趋势(展示全部省份)
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_province, "
            + "count(1) as pv_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2"
            + " group by substring(f_client_time,1,10),f_province order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogProvincePvCntTrendALL(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份分布(session、user、平均session、最大session、最小session、50分位session)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "f_province,\n"
            + "sum(session_num) as session_num,\n"
            + "sum(user_num) as user_num,\n"
            + "sum(session_avg) as session_avg,\n"
            + "sum(session_max) as session_max,\n"
            + "sum(session_min) as session_min,\n"
            + "sum(session_50p) as session_50p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "f_province,\n"
            + "sum(session_cnt) as session_num,\n"
            + "count(f_user_id) as user_num,\n"
            + "sum(session_cnt) div count(f_user_id) as session_avg,\n"
            + "max(session_cnt) as session_max,\n"
            + "min(session_cnt) as session_min,\n"
            + "0 as session_50p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "f_province, f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_province,f_user_id\n"
            + ")t1\n"
            + "group by f_province\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "a3.f_province as f_province,\n"
            + "0 as session_num,\n"
            + "0 as user_num,\n"
            + "0 as session_avg,\n"
            + "0 as session_max,\n"
            + "0 as session_min,\n"
            + "session_cnt as session_50\n"
            + "from\n"
            + "(\n"
            + "(\n"
            + "select a1.f_user_id,a1.f_province,a1.session_cnt,"
            + "sum(case when a2.f_province=a1.f_province then 1 else 0 end) as group_rank \n"
            + "from \n"
            + "(\n"
            + "select f_user_id, \n"
            + " f_province,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id,f_province) a1 ,\n"
            + "(select f_user_id,f_province,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id,f_province) a2\n"
            + "where a1.session_cnt > a2.session_cnt\n"
            + "or (a1.session_cnt = a2.session_cnt and a1.f_user_id <= a2.f_user_id)\n"
            + "group by a1.f_user_id,a1.f_province,a1.session_cnt\n"
            + "order by group_rank\n"
            + ") a3 \n"
            + "join\n"
            + "(select f_province,(count(distinct f_user_id) + 1) DIV 2 as group_rank_pct50\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_province\n"
            + ")a4 \n"
            + "on a3.f_province=a4.f_province and a3.group_rank=a4.group_rank_pct50\n"
            + ")\n"
            + "\n"
            + ")t\n"
            + "group by f_province", nativeQuery = true)
    List<Map<String, Object>> getLogSessionProvinceDistribute(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取省份分布(session-pv、user-pv、平均session-pv、最大session-pv、最小session-pv、50分位session-pv)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "f_province,\n"
            + "sum(session_pv_num) as session_pv_num,\n"
            + "sum(user_pv_num) as user_pv_num,\n"
            + "sum(session_pv_avg) as session_pv_avg,\n"
            + "sum(session_pv_max) as session_pv_max,\n"
            + "sum(session_pv_min) as session_pv_min,\n"
            + "sum(session_pv_50p) as session_pv_50p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "f_province,\n"
            + "sum(session_pv) as session_pv_num,\n"
            + "count(f_userid_sessionid) as user_pv_num,\n"
            + "sum(session_pv) div count(f_userid_sessionid) as session_pv_avg,\n"
            + "max(session_pv) as session_pv_max,\n"
            + "min(session_pv) as session_pv_min,\n"
            + "0 as session_pv_50p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "f_province, concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_province,f_userid_sessionid\n"
            + ")t1\n"
            + "group by f_province\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "a3.f_province as f_province,\n"
            + "0 as session_pv_num,\n"
            + "0 as user_pv_num,\n"
            + "0 as session_pv_avg,\n"
            + "0 as session_pv_max,\n"
            + "0 as session_pv_min,\n"
            + "session_pv as session_pv_50p\n"
            + "from\n"
            + "(\n"
            + "(\n"
            + "select a1.f_userid_sessionid,a1.f_province,a1.session_pv,"
            + "sum(case when a2.f_province=a1.f_province then 1 else 0 end) as group_rank \n"
            + "from \n"
            + "(\n"
            + "select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,f_province,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid,f_province) a1 ,\n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,f_province,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid,f_province) a2\n"
            + "where a1.session_pv > a2.session_pv\n"
            + "or (a1.session_pv = a2.session_pv and a1.f_userid_sessionid <= a2.f_userid_sessionid)\n"
            + "group by a1.f_userid_sessionid,a1.f_province,a1.session_pv\n"
            + "order by group_rank\n"
            + ") a3 \n"
            + "join\n"
            + "(select f_province,((count(distinct concat(f_user_id,'_&_',f_session_id)) + 1) DIV 2) "
            + "as group_rank_pct50\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_province\n"
            + ")a4 \n"
            + "on a3.f_province=a4.f_province and a3.group_rank=a4.group_rank_pct50\n"
            + ")\n"
            + "\n"
            + ")t\n"
            + "group by f_province", nativeQuery = true)
    List<Map<String, Object>> getLogPVProvinceDistribute(String fUserSceneVersionId,int fMakeVersionId);

}
