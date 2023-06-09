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
 * describe: 分渠道维度session分布
 *
 * @author author
 * @date 2021/11/23
 */
public interface LogEventChannelRepository extends JpaRepository<LogEventEntity, Integer> {


    /**
     * 获取渠道列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_channel from (select  f_channel,count(*) as cnt from t_session_log_event "
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2 group by f_channel order by cnt desc)a ",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelList(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道user数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_channel,count(distinct f_user_id) as user_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 group by f_channel order by user_num desc",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByChannelUserNum(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道session数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_channel,count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 group by f_channel order by session_num desc", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByChannelSessionNum(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道pv数分布
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_channel,count(1) as pv_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 group by f_channel order by pv_num desc", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCntByChannelPVNum(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道user数分布 时间趋势 （可筛选渠道）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_channel, count(distinct f_user_id) as user_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_channel in (?3) "
            + " group by substring(f_client_time,1,10),f_channel order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelUserCntTrend(String fUserSceneVersionId,
            int fMakeVersionId, List<String> fChannel);


    /**
     * 获取渠道user数分布 时间趋势 （展示全部渠道）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_channel, "
            + " count(distinct f_user_id) as user_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2"
            + " group by substring(f_client_time,1,10),f_channel order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelUserCntTrendALL(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道session数分布 时间趋势 （可筛选渠道）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_channel, "
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_channel in (?3) "
            + " group by substring(f_client_time,1,10),f_channel order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelSessionCntTrend(String fUserSceneVersionId, int fMakeVersionId,
            List<String> fChannel);


    /**
     * 获取渠道session数分布 时间趋势(折线-可筛选渠道)
     *
     * @return
     */
    @Transactional
    @Query(value = "select p_date,'all' as f_channel,count(distinct session_id) as session_num from "
            + " (select substring(f_client_time,1,10) as p_date, "
            + " f_channel, "
            + " concat(f_user_id,'_&_',f_session_id) as session_id "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2)t "
            + " where f_channel in (?3) "
            + " group by p_date order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelCntTrendZHE(String fUserSceneVersionId, int fMakeVersionId,
            List<String> fChannel);


    /**
     * 获取渠道session数分布 时间趋势 （展示全部渠道）
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_channel, "
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
            + " group by substring(f_client_time,1,10),f_channel order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelCntTrendALL(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道session数分布 全部时间趋势
     *
     * @return
     */
    @Transactional
    @Query(value = " select substring(f_client_time,1,10) as p_date,'all' as f_channel,"
            + " count( distinct concat(f_user_id,'_&_',f_session_id) ) as session_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by p_date  "
            + " order by p_date", nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelCntTrendALLZHE(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道pv数分布 时间趋势(可筛选渠道)
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_channel, "
            + " count(1) as pv_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 and f_channel in (?3) "
            + " group by substring(f_client_time,1,10),f_channel order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelPvCntTrend(String fUserSceneVersionId,
            int fMakeVersionId, List<String> fChannel);


    /**
     * 获取渠道pv数分布 时间趋势(展示全部渠道)
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, "
            + " f_channel, "
            + " count(1) as pv_num "
            + " from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " group by substring(f_client_time,1,10),f_channel order by substring(f_client_time,1,10)",
            nativeQuery = true)
    List<Map<String, Object>> getSessionLogChannelPvCntTrendALL(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道分布(session、user、平均session、最大session、最小session、50分位session)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "f_channel,\n"
            + "sum(session_num) as session_num,\n"
            + "sum(user_num) as user_num,\n"
            + "sum(session_avg) as session_avg,\n"
            + "sum(session_max) as session_max,\n"
            + "sum(session_min) as session_min,\n"
            + "sum(session_50p) as session_50p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "f_channel,\n"
            + "sum(session_cnt) as session_num,\n"
            + "count(f_user_id) as user_num,\n"
            + "sum(session_cnt) div count(f_user_id) as session_avg,\n"
            + "max(session_cnt) as session_max,\n"
            + "min(session_cnt) as session_min,\n"
            + "0 as session_50p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "f_channel, f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_channel,f_user_id\n"
            + ")t1\n"
            + "group by f_channel\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "a3.f_channel as f_channel,\n"
            + "0 as session_num,\n"
            + "0 as user_num,\n"
            + "0 as session_avg,\n"
            + "0 as session_max,\n"
            + "0 as session_min,\n"
            + "session_cnt as session_50\n"
            + "from\n"
            + "(\n"
            + "(\n"
            + "select a1.f_user_id,a1.f_channel,a1.session_cnt,sum(case when a2.f_channel=a1.f_channel "
            + "then 1 else 0 end) as group_rank \n"
            + "from \n"
            + "(\n"
            + "select f_user_id, \n"
            + " f_channel,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2 \n"
            + "group by f_user_id,f_channel) a1 ,\n"
            + "(select f_user_id,f_channel,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id,f_channel) a2\n"
            + "where a1.session_cnt > a2.session_cnt\n"
            + "or (a1.session_cnt = a2.session_cnt and a1.f_user_id <= a2.f_user_id)\n"
            + "group by a1.f_user_id,a1.f_channel,a1.session_cnt\n"
            + "order by group_rank\n"
            + ") a3 \n"
            + "join\n"
            + "(select f_channel,(count(distinct f_user_id) + 1) DIV 2 as group_rank_pct50\n"
            + "from t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_channel\n"
            + ")a4 \n"
            + "on a3.f_channel=a4.f_channel and a3.group_rank=a4.group_rank_pct50\n"
            + ")\n"
            + "\n"
            + ")t\n"
            + "group by f_channel", nativeQuery = true)
    List<Map<String, Object>> getLogSessionChannelDistribute(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取渠道分布(session-pv、user-pv、平均session-pv、最大session-pv、最小session-pv、50分位session-pv)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "f_channel,\n"
            + "sum(session_pv_num) as session_pv_num,\n"
            + "sum(user_pv_num) as user_pv_num,\n"
            + "sum(session_pv_avg) as session_pv_avg,\n"
            + "sum(session_pv_max) as session_pv_max,\n"
            + "sum(session_pv_min) as session_pv_min,\n"
            + "sum(session_pv_50p) as session_pv_50p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "f_channel,\n"
            + "sum(session_pv) as session_pv_num,\n"
            + "count(f_userid_sessionid) as user_pv_num,\n"
            + "sum(session_pv) div count(f_userid_sessionid) as session_pv_avg,\n"
            + "max(session_pv) as session_pv_max,\n"
            + "min(session_pv) as session_pv_min,\n"
            + "0 as session_pv_50p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "f_channel, concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_channel,f_userid_sessionid\n"
            + ")t1\n"
            + "group by f_channel\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "a3.f_channel as f_channel,\n"
            + "0 as session_pv_num,\n"
            + "0 as user_pv_num,\n"
            + "0 as session_pv_avg,\n"
            + "0 as session_pv_max,\n"
            + "0 as session_pv_min,\n"
            + "session_pv as session_pv_50p\n"
            + "from\n"
            + "(\n"
            + "(\n"
            + "select a1.f_userid_sessionid,a1.f_channel,a1.session_pv,"
            + "sum(case when a2.f_channel=a1.f_channel then 1 else 0 end) as group_rank \n"
            + "from \n"
            + "(\n"
            + "select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,f_channel,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid,f_channel) a1 ,\n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,f_channel,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid,f_channel) a2\n"
            + "where a1.session_pv > a2.session_pv\n"
            + "or (a1.session_pv = a2.session_pv and a1.f_userid_sessionid <= a2.f_userid_sessionid)\n"
            + "group by a1.f_userid_sessionid,a1.f_channel,a1.session_pv\n"
            + "order by group_rank\n"
            + ") a3 \n"
            + "join\n"
            + "(select f_channel,((count(distinct concat(f_user_id,'_&_',f_session_id)) + 1) DIV 2) "
            + "as group_rank_pct50\n"
            + "from t_session_log_event\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_channel\n"
            + ")a4 \n"
            + "on a3.f_channel=a4.f_channel and a3.group_rank=a4.group_rank_pct50\n"
            + ")\n"
            + "\n"
            + ")t\n"
            + "group by f_channel", nativeQuery = true)
    List<Map<String, Object>> getLogPVChannelDistribute(String fUserSceneVersionId, int fMakeVersionId);

}
