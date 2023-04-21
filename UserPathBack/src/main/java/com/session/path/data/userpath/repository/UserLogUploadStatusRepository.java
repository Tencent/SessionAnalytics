/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.UserLogUploadStatusEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: excel上传切分状态
 *
 * @author author
 * @date 2021/10/27
 */
public interface UserLogUploadStatusRepository extends JpaRepository<UserLogUploadStatusEntity, Integer> {

    /**
     * 获取上传版本列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select distinct f_version_id from t_user_log_upload_split_status where "
            + "f_session_split_status=1 and f_upload_name=?1 and f_upload_user=?2 order by f_create_time desc",
            nativeQuery = true)
    List<Map<String, Object>> getVersionIdList(String fUploadName, String fUploadUser);


    /**
     * 获取最大版本号
     *
     * @return
     */
    @Transactional
    @Query(value = "select ifnull(max(f_version_id),0) from t_user_log_upload_split_status where"
            + " f_upload_name=?1 and f_upload_user=?2", nativeQuery = true)
    int getMaxVersionId(String fUploadName, String fUploadUser);


    /**
     * 获取数据治理列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_upload_split_status where f_upload_user=?1 and "
            + "if(IFNULL(?2,'') !='',f_upload_name like concat('%', ?2, '%'),1=1) and "
            + "f_upload_status=1 and f_make_version_id!=0 "
            + " and if(IFNULL(?3,'') !='',f_make_status=?3,1=1)", nativeQuery = true)
    Page<UserLogUploadStatusEntity> getUserLogMakeStatusEntity(String fUploadUser, String fUploadName,
             Integer fMakeStatus, Pageable pageable);


    /**
     * 判断上传人是否存在
     *
     * @return
     */
    @Transactional
    @Query(value = "select id from t_user_log_upload_split_status where"
            + " f_upload_status=1 and f_upload_user=?1 limit 1", nativeQuery = true)
    String getUploadUserExist(String fUploadUser);


    /**
     * 获取原始数据集切分列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_upload_split_status where f_upload_user=?1 and "
            + "if(IFNULL(?2,'') !='',f_upload_name like concat('%', ?2, '%'),1=1) and "
            + "f_make_version_id=0 and if(IFNULL(?3,'') !='',f_session_split_status=?3,1=1)", nativeQuery = true)
    Page<UserLogUploadStatusEntity> getUserLogSplitStatusOldfEntity(String fUploadUser, String fUploadName,
            Integer fSessionSplitStatus, Pageable pageable);


    /**
     * 获取治理数据集切分列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_upload_split_status where f_upload_user=?1 and "
            + "if(IFNULL(?2,'') !='',f_upload_name like concat('%', ?2, '%'),1=1) and "
            + "f_make_version_id!=0 and  if(IFNULL(?3,'') !='',f_session_split_status=?3,1=1)", nativeQuery = true)
    Page<UserLogUploadStatusEntity> getUserLogSplitStatusNewfEntity(String fUploadUser, String fUploadName,
            Integer fSessionSplitStatus, Pageable pageable);


    /**
     * 获取治理版本号数据信息
     *
     * @return
     */
    @Transactional
    @Query(value = "select distinct f_make_version_id from t_user_log_upload_split_status "
            + " where f_user_scene_version_id=?1 "
            + " and f_session_split_status=1", nativeQuery = true)
    List<Map<String, Object>> getUserLogMakeVersionList(String fUserSceneVersionId);


    /**
     * 获取上传、切分、治理、全部状态信息
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_upload_split_status "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 limit 1", nativeQuery = true)
    UserLogUploadStatusEntity getUserLogStatusEntity(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 获取上传列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_upload_split_status where f_upload_user=?1 and if(IFNULL(?2,'') !='',"
            + "f_upload_name like concat('%', ?2, '%'),1=1) and if(IFNULL(?3,'') !='',"
            + "f_upload_status=?3,1=1) and f_make_version_id=0", nativeQuery = true)
    Page<UserLogUploadStatusEntity> getUserLogUploadStatusEntity(String fUploadUser, String fUploadName,
            Integer fUploadStatus, Pageable pageable);

    /**
     * 获取上传标题列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select distinct f_upload_name from t_user_log_upload_split_status where f_upload_user=?1"
            + " order by f_create_time desc", nativeQuery = true)
    List<Map<String, Object>> getUploadNameList(String fUploadUser);


    /**
     * 获取上传标题列表(已完成切分)
     *
     * @return
     */
    @Transactional
    @Query(value = "select distinct f_upload_name from t_user_log_upload_split_status where f_upload_user=?1"
            + " and f_session_split_status=1 order by f_create_time desc", nativeQuery = true)
    List<Map<String, Object>> getUploadNameDoneList(String fUploadUser);


    /**
     * 获取上传人列表
     *
     * @return
     */
    @Transactional
    @Query(value = "select distinct f_upload_user from t_user_log_upload_split_status where"
            + " f_upload_status=1 order by f_update_time desc", nativeQuery = true)
    List<Map<String, Object>> getUploadUserList();


    /**
     * 获取数据上传任务执行状态汇总
     */
    @Transactional
    @Query(value = "select  \n"
            + "case \n"
            + "  when t.f_upload_status=1 then '上传完成'\n"
            + "  when t.f_upload_status=0 then '上传等待中' \n"
            + "  when t.f_upload_status=2 then '上传进行中'\n"
            + "  when t.f_upload_status=3 then '上传失败'\n"
            + "  end as f_deal_status,\n"
            + "  sum(cnt) as f_cnt\n"
            + "  from\n"
            + "(select f_upload_status,count(*) as cnt from t_user_log_upload_split_status "
            + "where f_upload_user=?1 and f_make_version_id=0"
            + " group by f_upload_status\n"
            + ")t\n"
            + "group by f_deal_status", nativeQuery = true)
    List<Map<String, Object>> getUserLogUploadStatusCnt(String fUploadUser);



    /**
     * 获取数据治理任务执行状态汇总
     */
    @Transactional
    @Query(value = "select  \n"
            + "case \n"
            + "  when t.f_upload_status=1 and t.f_session_split_status=1 then '切分完成'\n"
            + "  when t.f_upload_status=1 and t.f_session_split_status=0 then '上传完成待切分' \n"
            + "  when t.f_upload_status=1 and t.f_session_split_status=2 then '切分处理中'\n"
            + "  when t.f_upload_status=1 and t.f_session_split_status=3 then '切分失败'\n"
            + "  end as f_deal_status,\n"
            + "  sum(cnt) as f_cnt\n"
            + "  from\n"
            + "(select f_upload_status,f_session_split_status,count(*) as cnt from t_user_log_upload_split_status "
            + "where f_upload_user=?1 "
            + " group by f_upload_status,f_session_split_status\n"
            + ")t\n"
            + "group by f_deal_status", nativeQuery = true)
    List<Map<String, Object>> getUserLogMakeStatusCnt(String fUploadUser);



    /**
     * 更新excel上传状态
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_upload_status=?3 where f_upload_name=?1 and"
            + " f_upload_user=?2", nativeQuery = true)
    void updateUploadStatus(String fUploadName, String fUploadUser, int fStatus);



    /**
     * 更新上传日志结束时间
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_upload_update_time=now() where f_upload_name=?1 and"
            + " f_upload_user=?2", nativeQuery = true)
    void updateUploadEndTime(String fUploadName, String fUploadUser);



    /**
     * 更新治理开始时间
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_make_create_time=now() where f_upload_name=?1 and"
            + " f_upload_user=?2 and f_make_version_id=?3 ", nativeQuery = true)
    void updateMakeCreateTime(String fUploadName, String fUploadUser, int fMakeVersionId);


    /**
     * 更新治理结束时间
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_make_update_time=now() where f_upload_name=?1 and"
            + " f_upload_user=?2 and f_make_version_id=?3", nativeQuery = true)
    void updateMakeEndTime(String fUploadName, String fUploadUser, int fMakeVersionId);



    /**
     * 更新切分开始时间
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_create_time=now() where f_upload_name=?1 and"
            + " f_upload_user=?2 and f_make_version_id=?3 ", nativeQuery = true)
    void updateSplitCreateTime(String fUploadName, String fUploadUser, int fMakeVersionId);




    /**
     * 更新数据治理状态
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_make_status=?3 where "
            + "f_user_scene_version_id=?1 and f_make_version_id=?2", nativeQuery = true)
    void updateMakeStatus(String fUserSceneVersionId, int fMakeVersionId, int fStatus);


    /**
     * 更新数据切分状态
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_session_split_status=?3 where "
            + "f_user_scene_version_id=?1 and f_make_version_id=?2", nativeQuery = true)
    void updateSplitStatus(String fUserSceneVersionId, int fMakeVersionId, int fStatus);


    /**
     * 删除上传重复数据
     */
    @Modifying
    @Transactional
    @Query(value = "delete from t_user_log_orig "
            + "where f_user_scene_version_id=?1", nativeQuery = true)
    void deleteExcelUpload(String fUserSceneVersionId);


    /**
     * 获取上传完成但是未进行切分的列表
     */
    @Transactional
    @Query(value = "select * from t_user_log_upload_split_status where f_upload_status=1 and f_make_status=1 "
            + "and f_session_split_status=0 "
            + "order by f_create_time asc limit 1", nativeQuery = true)
    List<UserLogUploadStatusEntity> getUndoSessionSplit();


    /**
     * 切分上传完成、数据治理完成但是未进行切分的entity
     */
    @Transactional
    @Query(value = "select * from t_user_log_upload_split_status "
            + " where f_upload_status=1"
            + " and f_session_split_status=0 and f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 limit 1", nativeQuery = true)
    UserLogUploadStatusEntity getSessionSplit(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 更新治理数据集名称,描述
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_upload_split_status set f_upload_name=?3,f_scene_desc=?4 where "
            + "f_user_scene_version_id=?1 and f_make_version_id=?2", nativeQuery = true)
    void updateMakeLogName(String fUserSceneVersionId, int fMakeVersionId, String fUploadName, String fSceneDesc);


    /**
     * 根据用户名和数据集获取UserSceneVersionId
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_user_scene_version_id from"
            + " t_user_log_upload_split_status where "
            + "f_upload_name=?1 and f_upload_user=?2 limit 1", nativeQuery = true)
    Map<String, String> getUserSceneVersionId(String fUploadName, String fUploadUser);


    /**
     * 根据用户名和数据集获取SceneId
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_scene_id from"
            + " t_user_log_upload_split_status where "
            + "f_upload_name=?1 and f_upload_user=?2 limit 1", nativeQuery = true)
    Map<String, String> getSceneId(String fUploadName, String fUploadUser);


    /**
     * 根据用户名和数据集获取治理版本id
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_make_version_id from"
            + " t_user_log_upload_split_status where "
            + "f_upload_name=?1 and f_upload_user=?2 limit 1", nativeQuery = true)
    Map<String, Integer> getMakeVersionId(String fUploadName, String fUploadUser);


}
