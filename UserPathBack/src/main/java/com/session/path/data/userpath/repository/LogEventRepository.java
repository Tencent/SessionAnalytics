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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: session不去重切分
 *
 * @author author
 * @date 2021/11/09
 */
public interface LogEventRepository extends JpaRepository<LogEventEntity, Integer> {

    /**
     * 获取session log数据（治理之后）
     *
     * @return
     */
    @Transactional
    @Query(value = "select \n"
            + "f_user_id\n"
            + ",f_age\n"
            + ",f_sex\n"
            + ",f_province\n"
            + ",f_city\n"
            + ",f_channel\n"
            + ",f_session_id  \n"
            + ",f_session_rank\n"
            + ",f_event\n"
            + ",f_event_detail\n"
            + ",f_client_time\n"
            + ",f_category\n"
            + ",f_subcategory\n"
            + ",f_user_scene_version_id\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "t6.*\n"
            + ",@rank\\:= if(@f_user_id3 = t6.f_user_id and @f_session_id=t6.f_session_id,@rank\\:= @rank+1,1) "
            + "as f_session_rank\n"
            + ",@f_user_id3\\:= t6.f_user_id\n"
            + ",@f_session_id\\:= t6.f_session_id\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "t4.*\n"
            + ",@sum\\:= if(@f_user_id2 = f_user_id,@sum\\:= @sum+f_session_index,f_session_index) as f_session_id\n"
            + ",@f_user_id2\\:= f_user_id\n"
            + "from\n"
            + "(\n"
            + "select \n"
            + "t3.*\n"
            + ",case when (f_interval>?3 * 60 or f_interval is null or f_event = ?4) then 1 else 0 end "
            + "as f_session_index\n"
            + "from\n"
            + "(\n"
            + "select \n"
            + "t1.*\n"
            + ",@client_time\\:= unix_timestamp(if(@f_user_id = f_user_id,@client_time,0)) last_client_time\n"
            + ",(unix_timestamp(t1.f_client_time) - @client_time) as f_interval\n"
            + ",@client_time\\:= f_client_time\n"
            + ",@f_user_id\\:= f_user_id\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "f_user_id\n"
            + ",f_age\n"
            + ",f_sex\n"
            + ",f_province\n"
            + ",f_city\n"
            + ",f_channel\n"
            + ",f_event\n"
            + ",f_event_detail\n"
            + ",f_category\n"
            + ",f_subcategory\n"
            + ",f_user_scene_version_id\n"
            + ",f_client_time\n"
            + ",@last \\:= if(@first = a.f_user_id, @last+1, 1) as rn\n"
            + ",@first\\:= a.f_user_id \n"
            + "from \n"
            + "t_user_log_orig_make a, (select @last\\:= 0, @first\\:= null) b\n"
            + " where a.f_user_scene_version_id=?1 and a.f_make_version_id=?2\n"
            + "order by f_user_id asc,unix_timestamp(f_client_time) asc\n"
            + ")t1, (select @client_time\\:=0, @f_user_id\\:= null) t2\n"
            + ") t3\n"
            + ") t4,(select @f_user_id2\\:= null, @sum\\:= 0)t5\n"
            + ")t6,(select @rank\\:= 0, @f_user_id3\\:= null, @f_session_id\\:= 0)t7\n"
            + ")t", nativeQuery = true)
    List<Map<String, Object>> getSessionMake(String fUserSceneVersionId,int fMakeVersionId, int fSessionSplitTime,
            String fSessionEvent);


    /**
     * 获取session log数据（原始日志）
     *
     * @return
     */
    @Transactional
    @Query(value = "select \n"
            + "f_user_id\n"
            + ",f_age\n"
            + ",f_sex\n"
            + ",f_province\n"
            + ",f_city\n"
            + ",f_channel\n"
            + ",f_session_id  \n"
            + ",f_session_rank\n"
            + ",f_event\n"
            + ",f_event_detail\n"
            + ",f_client_time\n"
            + ",f_category\n"
            + ",f_subcategory\n"
            + ",f_user_scene_version_id\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "t6.*\n"
            + ",@rank\\:= if(@f_user_id3 = t6.f_user_id and @f_session_id=t6.f_session_id,@rank\\:= @rank+1,1) "
            + "as f_session_rank\n"
            + ",@f_user_id3\\:= t6.f_user_id\n"
            + ",@f_session_id\\:= t6.f_session_id\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "t4.*\n"
            + ",@sum\\:= if(@f_user_id2 = f_user_id,@sum\\:= @sum+f_session_index,f_session_index) as f_session_id\n"
            + ",@f_user_id2\\:= f_user_id\n"
            + "from\n"
            + "(\n"
            + "select \n"
            + "t3.*\n"
            + ",case when (f_interval>?2 * 60 or f_interval is null or f_event = ?3) then 1 else 0 end "
            + "as f_session_index\n"
            + "from\n"
            + "(\n"
            + "select \n"
            + "t1.*\n"
            + ",@client_time\\:= unix_timestamp(if(@f_user_id = f_user_id,@client_time,0)) last_client_time\n"
            + ",(unix_timestamp(t1.f_client_time) - @client_time) as f_interval\n"
            + ",@client_time\\:= f_client_time\n"
            + ",@f_user_id\\:= f_user_id\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "f_user_id\n"
            + ",f_age\n"
            + ",f_sex\n"
            + ",f_province\n"
            + ",f_city\n"
            + ",f_channel\n"
            + ",f_event\n"
            + ",f_event_detail\n"
            + ",f_category\n"
            + ",f_subcategory\n"
            + ",f_user_scene_version_id\n"
            + ",f_client_time\n"
            + ",@last \\:= if(@first = a.f_user_id, @last+1, 1) as rn\n"
            + ",@first\\:= a.f_user_id \n"
            + "from \n"
            + "t_user_log_orig a, (select @last\\:= 0, @first\\:= null) b\n"
            + " where a.f_user_scene_version_id=?1\n"
            + "order by f_user_id asc,unix_timestamp(f_client_time) asc\n"
            + ")t1, (select @client_time\\:=0, @f_user_id\\:= null) t2\n"
            + ") t3\n"
            + ") t4,(select @f_user_id2\\:= null, @sum\\:= 0)t5\n"
            + ")t6,(select @rank\\:= 0, @f_user_id3\\:= null, @f_session_id\\:= 0)t7\n"
            + ")t", nativeQuery = true)
    List<Map<String, Object>> getSessionOrig(String fUserSceneVersionId, int fSessionSplitTime,
            String fSessionEvent);


    /**
     * 获取session log数据查询
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',"
            + "f_user_id=?3,1=1) and if(IFNULL(?4,'') !='',f_channel=?4,1=1)"
            + "and if(IFNULL(?5,'') !='',f_province=?5,1=1) and if(IFNULL(?6,'') !='',"
            + "f_city=?6,1=1) and if(IFNULL(?7,'') !='',f_category=?7,1=1) and if(IFNULL(?8,'') !='',"
            + "f_subcategory=?8,1=1) and if(IFNULL(?11,'') !='',f_event=?11,1=1)"
            + " and if(IFNULL(?9,'') !='',substring(f_client_time,1,10)>=?9,1=1) "
            + "and if(IFNULL(?10,'') !='',substring(f_client_time,1,10)<=?10,1=1)", nativeQuery = true)
    Page<LogEventEntity> getSessionLogQuery(String fUserSceneVersionId,
            int fMakeVersionId, String fUserId, String fChannel,
            String fProvince, String fCity, String fCategory, String fSubcategory, String fStartTime,
            String fEndTime, String fEvent, Pageable pageable);

    /**
     * 获取log总览查询(总用户数   总session数   总PV数)
     *
     * @return
     */
    @Transactional
    @Query(value = "select count(distinct f_user_id) as user_num,"
            + "count( distinct concat(f_user_id,'_&_',f_session_id) ) "
            + "as session_num,count(1) as pv_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2", nativeQuery = true)
    List<Map<String, Object>> getSessionLogCnt(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取log总览查询(总用户数 趋势)
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, count(distinct f_user_id) as user_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2 group by "
            + "substring(f_client_time,1,10) order by substring(f_client_time,1,10)", nativeQuery = true)
    List<Map<String, Object>> getSessionLogUserCntTrend(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取log总览查询(总session数 趋势)
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date,"
            + "count( distinct concat(f_user_id,'_&_',f_session_id) ) "
            + "as session_num  from t_session_log_event "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 group by "
            + "substring(f_client_time,1,10) order by substring(f_client_time,1,10)", nativeQuery = true)
    List<Map<String, Object>> getSessionLogSessionCntTrend(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取log总览查询(总PV数 趋势)
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(f_client_time,1,10) as p_date, count(1) as pv_num "
            + " from t_session_log_event where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 group by substring(f_client_time,1,10) "
            + "order by substring(f_client_time,1,10)", nativeQuery = true)
    List<Map<String, Object>> getSessionLogPvCntTrend(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取session分布(平均session、最大session、25分位session、50分位session、75分位session)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "sum(session_avg) as session_avg,\n"
            + "sum(session_max) as session_max,\n"
            + "sum(session_25p) as session_25p,\n"
            + "sum(session_50p) as session_50p,\n"
            + "sum(session_75p) as session_75p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "sum(session_cnt) div count(f_user_id) as session_avg,\n"
            + "max(session_cnt) as session_max,\n"
            + "0 as session_25p,\n"
            + "0 as session_50p,\n"
            + "0 as session_75p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id\n"
            + ")t1\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "0 as session_avg,\n"
            + "0 as session_max,\n"
            + "session_cnt as session_25p,\n"
            + "0 as session_50p,\n"
            + "0 as session_75p\n"
            + "from\n"
            + "(\n"
            + "select a1.f_user_id,a1.session_cnt,count(*) as rank \n"
            + "from \n"
            + "(select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id) a1 ,\n"
            + "(select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id) a2\n"
            + "where a1.session_cnt < a2.session_cnt\n"
            + "or (a1.session_cnt = a2.session_cnt and a1.f_user_id <= a2.f_user_id)\n"
            + "group by a1.f_user_id,a1.session_cnt\n"
            + "order by rank\n"
            + ") a3 \n"
            + "where rank = (\n"
            + "select (count(*) + 1) DIV 4 FROM (select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id)t1\n"
            + ")\n"
            + "\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "\n"
            + "select \n"
            + "0 as session_avg,\n"
            + "0 as session_max,\n"
            + "0 as session_25p,\n"
            + "session_cnt as session_50p,\n"
            + "0 as session_75p\n"
            + "from\n"
            + "(\n"
            + "select a1.f_user_id,a1.session_cnt,count(*) as rank \n"
            + "from \n"
            + "(select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id) a1 ,\n"
            + "(select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id) a2\n"
            + "where a1.session_cnt > a2.session_cnt\n"
            + "or (a1.session_cnt = a2.session_cnt and a1.f_user_id <= a2.f_user_id)\n"
            + "group by a1.f_user_id,a1.session_cnt\n"
            + "order by rank\n"
            + ") a3 \n"
            + "where rank = (\n"
            + "select (count(*) + 1) DIV 2 FROM (select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id)t1\n"
            + ")\n"
            + "\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "\n"
            + "\n"
            + "select \n"
            + "0 as session_avg,\n"
            + "0 as session_max,\n"
            + "0 as session_25p,\n"
            + "0 as session_50p,\n"
            + "session_cnt as session_75p\n"
            + "from\n"
            + "(\n"
            + "select a1.f_user_id,a1.session_cnt,count(*) as rank \n"
            + "from \n"
            + "(select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id) a1 ,\n"
            + "(select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id) a2\n"
            + "where a1.session_cnt > a2.session_cnt\n"
            + "or (a1.session_cnt = a2.session_cnt and a1.f_user_id <= a2.f_user_id)\n"
            + "group by a1.f_user_id,a1.session_cnt\n"
            + "order by rank\n"
            + ") a3 \n"
            + "where rank = (\n"
            + "select (count(*) + 1) DIV 1.33 FROM (select f_user_id,count(distinct f_session_id) as session_cnt\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id)t1\n"
            + ")\n"
            + ")t ", nativeQuery = true)
    List<Map<String, Object>> getLogSessionDistribute(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取session-pv分布(平均session-pv、最大session-pv、25分位session-pv、50分位session-pv、75分位session-pv)
     *
     * @return
     */
    @Transactional
    @Query(value = "SELECT\n"
            + "sum(session_pv_avg) as session_pv_avg,\n"
            + "sum(session_pv_max) as session_pv_max,\n"
            + "sum(session_pv_25p) as session_pv_25p,\n"
            + "sum(session_pv_50p) as session_pv_50p,\n"
            + "sum(session_pv_75p) as session_pv_75p\n"
            + "from\n"
            + "(\n"
            + "select\n"
            + "sum(session_pv) div count(f_userid_sessionid) as session_pv_avg,\n"
            + "max(session_pv) as session_pv_max,\n"
            + "0 as session_pv_25p,\n"
            + "0 as session_pv_50p,\n"
            + "0 as session_pv_75p\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid\n"
            + ")t1\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "select \n"
            + "0 as session_pv_avg,\n"
            + "0 as session_pv_max,\n"
            + "session_pv as session_pv_25p,\n"
            + "0 as session_pv_50p,\n"
            + "0 as session_pv_75p\n"
            + "from\n"
            + "(\n"
            + "select a1.f_userid_sessionid,a1.session_pv,count(*) as rank \n"
            + "from \n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid) a1 ,\n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid) a2\n"
            + "where a1.session_pv > a2.session_pv\n"
            + "or (a1.session_pv = a2.session_pv and a1.f_userid_sessionid <= a2.f_userid_sessionid)\n"
            + "group by a1.f_userid_sessionid,a1.session_pv\n"
            + "order by rank\n"
            + ") a3 \n"
            + "where rank = (\n"
            + "select (count(*) + 1) DIV 4 FROM (select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,"
            + "count(1) as session_pv\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid)t1\n"
            + ")\n"
            + "\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "\n"
            + "select \n"
            + "0 as session_pv_avg,\n"
            + "0 as session_pv_max,\n"
            + "0 as session_pv_25p,\n"
            + "session_pv as session_pv_50p,\n"
            + "0 as session_pv_75p\n"
            + "from\n"
            + "(\n"
            + "select a1.f_userid_sessionid,a1.session_pv,count(*) as rank \n"
            + "from \n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid) a1 ,\n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid) a2\n"
            + "where a1.session_pv > a2.session_pv\n"
            + "or (a1.session_pv = a2.session_pv and a1.f_userid_sessionid <= a2.f_userid_sessionid)\n"
            + "group by a1.f_userid_sessionid,a1.session_pv\n"
            + "order by rank\n"
            + ") a3 \n"
            + "where rank = (\n"
            + "select (count(*) + 1) DIV 2 FROM (select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,"
            + "count(1) as session_pv\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid)t1\n"
            + ")\n"
            + "\n"
            + "\n"
            + "union ALL\n"
            + "\n"
            + "\n"
            + "\n"
            + "select \n"
            + "0 as session_pv_avg,\n"
            + "0 as session_pv_max,\n"
            + "0 as session_pv_25p,\n"
            + "0 as session_pv_50p,\n"
            + "session_pv as session_pv_75p\n"
            + "from\n"
            + "(\n"
            + "select a1.f_userid_sessionid,a1.session_pv,count(*) as rank \n"
            + "from \n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid) a1 ,\n"
            + "(select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,count(1) as session_pv\n"
            + "from \n"
            + "t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid) a2\n"
            + "where a1.session_pv > a2.session_pv\n"
            + "or (a1.session_pv = a2.session_pv and a1.f_userid_sessionid <= a2.f_userid_sessionid)\n"
            + "group by a1.f_userid_sessionid,a1.session_pv\n"
            + "order by rank\n"
            + ") a3 \n"
            + "where rank = (\n"
            + "select (count(*) + 1) DIV 1.33 FROM (select concat(f_user_id,'_&_',f_session_id) as f_userid_sessionid,"
            + "count(1) as session_pv\n"
            + "from t_session_log_event where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_userid_sessionid)t1\n"
            + ")\n"
            + ")t ", nativeQuery = true)
    List<Map<String, Object>> getLogPVDistribute(String fUserSceneVersionId,int fMakeVersionId);






    /**
     * getSessionNodeDf
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_category,f_event,count(*) as f_weight_pv  from t_session_log_event "
            + " where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 group by f_category,f_event order by pv desc ", nativeQuery = true)
    List<Map<String, Object>> getSessionNodeDf(String fUserSceneVersionId,int fMakeVersionId);



//    /**
//     * getSessionSingleDf
//     *
//     * @return
//     */
//    @Transactional
//    @Query(value = "select b.id_from,c.id_to,a.f_weight_session from\n"
//            + "(select substring(substring_index(f_event_from,'_',-1),1,20) as event_from,"
//            + "substring(substring_index(f_event_to,'_',-1),1,20) as event_to, "
//            + "sum(f_weight_session) as f_weight_session "
//            + " from t_session_event_sankey where f_user_scene_version_id=?1 and f_make_version_id=?2 "
//            + " group by substring(substring_index(f_event_from,'_',-1),1,20), "
//            + "substring(substring_index(f_event_to,'_',-1),1,20))a\n"
//            + "left join \n"
//            + "(select id as id_from, f_event as event_from from t_session_node "
//            + " where f_user_scene_version_id=?1 and f_make_version_id=?2)b\n"
//            + "on a.event_from=b.event_from\n"
//            + "left join \n"
//            + "(select id as id_to,f_event as event_to from t_session_node "
//            + "where f_user_scene_version_id=?1 and f_make_version_id=?2 )c\n"
//            + "on a.event_to=c.event_to", nativeQuery = true)
//    List<Map<String, Object>> getSessionSingleDf(String fUserSceneVersionId,int fMakeVersionId);



    /**
     * getSessionSingleDf
     *
     * @return
     */
    @Transactional
    @Query(value = "select substring(substring_index(f_event_from,'_',-1),1,20) as event_from,"
            + " substring(substring_index(f_event_to,'_',-1),1,20) as event_to, "
            + " sum(f_weight_session) as f_weight_session "
            + " from t_session_event_sankey where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " group by substring(substring_index(f_event_from,'_',-1),1,20), "
            + " substring(substring_index(f_event_to,'_',-1),1,20)"
            , nativeQuery = true)
    List<Map<String, Object>> getSessionSingleDf(String fUserSceneVersionId,int fMakeVersionId);


}
