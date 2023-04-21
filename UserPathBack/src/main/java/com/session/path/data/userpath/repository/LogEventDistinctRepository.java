/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.LogEventDistinctEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: session去重切分
 *
 * @author author
 * @date 2021/11/09
 */
public interface LogEventDistinctRepository extends JpaRepository<LogEventDistinctEntity, Integer> {


    /**
     * 获取治理数据session切分数据集（去重）
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
            + ",@rank \\:= if(@f_user_id3 = t6.f_user_id and "
            + "@f_session_id=t6.f_session_id,@rank \\:= @rank+1,1) as f_session_rank\n"
            + ",@f_user_id3 \\:= t6.f_user_id\n"
            + ",@f_session_id \\:= t6.f_session_id\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "t4.*\n"
            + ",@sum \\:= if(@f_user_id2 = f_user_id,@sum \\:= @sum+f_session_index,f_session_index) as f_session_id\n"
            + ",@f_user_id2 \\:= f_user_id\n"
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
            + ",@client_time \\:= unix_timestamp(if(@f_user_id = f_user_id,@client_time,0)) last_client_time\n"
            + ",(unix_timestamp(t1.f_client_time) - @client_time) as f_interval\n"
            + ",@client_time \\:= f_client_time\n"
            + ",@f_user_id \\:= f_user_id\n"
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
            + ",@f_event \\:= if(@first = a.f_user_id, @f_event, null) as last_event\n"
            + ",@first \\:= a.f_user_id\n"
            + ",@f_event \\:= f_event\n"
            + "from \n"
            + "(\n"
            + "select * from (\n"
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
            + ",@f_event \\:= if(@f_user_id = f_user_id, @f_event, null) as last_event\n"
            + ",@f_user_id \\:= f_user_id\n"
            + ",@f_event \\:= f_event\n"
            + "from \n"
            + "t_user_log_orig_make m1, (select @f_user_id \\:= null, @f_event \\:= null) m2\n"
            + " where m1.f_user_scene_version_id=?1 and m1.f_make_version_id=?2\n"
            + "order by f_user_id asc,unix_timestamp(f_client_time) asc \n"
            + ") m \n"
            + "where (f_event <> last_event or last_event is null)\n"
            + ")a, (select @last \\:= 0, @first \\:= null, @f_event \\:= null) b\n"
            + "order by f_user_id asc,unix_timestamp(f_client_time) asc\n"
            + "  )t1, (select @client_time\\:=0, @f_user_id \\:= null) t2\n"
            + "  ) t3\n"
            + "  ) t4,(select @f_user_id2 \\:= null, @sum \\:= 0)t5\n"
            + ")t6,(select @rank \\:= 0, @f_user_id3 \\:= null, @f_session_id \\:= 0)t7\n"
            + ")t", nativeQuery = true)
    List<Map<String, Object>> getDistinctSessionMake(String fUserSceneVersionId,
            int fMakeVersionId, int fSessionSplitTime, String fSessionEvent);

    /**
     * 获取原始数据session切分数据集（去重）
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
            + ",@rank \\:= if(@f_user_id3 = t6.f_user_id and "
            + "@f_session_id=t6.f_session_id,@rank \\:= @rank+1,1) as f_session_rank\n"
            + ",@f_user_id3 \\:= t6.f_user_id\n"
            + ",@f_session_id \\:= t6.f_session_id\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "t4.*\n"
            + ",@sum \\:= if(@f_user_id2 = f_user_id,@sum \\:= @sum+f_session_index,f_session_index) as f_session_id\n"
            + ",@f_user_id2 \\:= f_user_id\n"
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
            + ",@client_time \\:= unix_timestamp(if(@f_user_id = f_user_id,@client_time,0)) last_client_time\n"
            + ",(unix_timestamp(t1.f_client_time) - @client_time) as f_interval\n"
            + ",@client_time \\:= f_client_time\n"
            + ",@f_user_id \\:= f_user_id\n"
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
            + ",@f_event \\:= if(@first = a.f_user_id, @f_event, null) as last_event\n"
            + ",@first \\:= a.f_user_id\n"
            + ",@f_event \\:= f_event\n"
            + "from \n"
            + "(\n"
            + "select * from (\n"
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
            + ",@f_event \\:= if(@f_user_id = f_user_id, @f_event, null) as last_event\n"
            + ",@f_user_id \\:= f_user_id\n"
            + ",@f_event \\:= f_event\n"
            + "from \n"
            + "t_user_log_orig m1, (select @f_user_id \\:= null, @f_event \\:= null) m2\n"
            + " where m1.f_user_scene_version_id=?1\n"
            + "order by f_user_id asc,unix_timestamp(f_client_time) asc \n"
            + ") m \n"
            + "where (f_event <> last_event or last_event is null)\n"
            + ")a, (select @last \\:= 0, @first \\:= null, @f_event \\:= null) b\n"
            + "order by f_user_id asc,unix_timestamp(f_client_time) asc\n"
            + "  )t1, (select @client_time\\:=0, @f_user_id \\:= null) t2\n"
            + "  ) t3\n"
            + "  ) t4,(select @f_user_id2 \\:= null, @sum \\:= 0)t5\n"
            + ")t6,(select @rank \\:= 0, @f_user_id3 \\:= null, @f_session_id \\:= 0)t7\n"
            + ")t", nativeQuery = true)
    List<Map<String, Object>> getDistinctSessionOrig(String fUserSceneVersionId, int fSessionSplitTime,
            String fSessionEvent);

}
