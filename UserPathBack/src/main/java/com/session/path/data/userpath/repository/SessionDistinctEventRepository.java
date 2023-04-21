/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.SessionEventDistinctEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe:
 *
 * @author author
 * @date 2023/02/25
 */
public interface SessionDistinctEventRepository extends JpaRepository<SessionEventDistinctEntity, Integer> {

    /**
     * 获取去重路径结果（from to weight path）
     */
    @Transactional
    @Query(value = "select \n"
            + "t1.f_age\n"
            + ",t1.f_sex\n"
            + ",t1.f_province\n"
            + ",t1.f_city\n"
            + ",t1.f_channel \n"
            + ",convert(t1.f_event_from using utf8mb4) as f_event_from\n"
            + ",t1.f_event_to\n"
            + ",count(distinct concat(t1.f_user_id,t1.f_session_id)) as f_weight_session\n"
            + ",count(distinct t1.f_user_id) as f_weight_user\n"
            + ",count(*) as f_weight_pv\n"
            + ",t2.f_event_path\n"
            + ",convert(t1.f_category_from using utf8mb4) as f_category_from\n"
            + ",t1.f_category_to\n"
            + ",t2.f_category_path\n"
            + ",convert(t1.f_subcategory_from using utf8mb4) as f_subcategory_from\n"
            + ",t1.f_subcategory_to\n"
            + ",t2.f_subcategory_path\n"
            + ",t1.f_user_scene_version_id\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "f_user_id \n"
            + ",f_age\n"
            + ",f_sex\n"
            + ",f_province\n"
            + ",f_city\n"
            + ",f_channel\n"
            + ",f_session_id\n"
            + ",f_session_rank\n"
            + ",f_event_from\n"
            + ",f_event_to\n"
            + ",f_category_from\n"
            + ",f_category_to \n"
            + ",f_subcategory_from\n"
            + ",f_subcategory_to\n"
            + ",f_user_scene_version_id\n"
            + "from  \n"
            + "(\n"
            + "select \n"
            + "f_user_id \n"
            + ",f_age\n"
            + ",f_sex\n"
            + ",f_province\n"
            + ",f_city\n"
            + ",f_channel\n"
            + ",f_session_id\n"
            + ",f_session_rank\n"
            + ",@f_event \\:= if(@f_user_id = f_user_id and @f_session_id = f_session_id, "
            + "concat('层级',@f_rank,'_',if(@f_event is null or @f_event='','未知',@f_event)), null) as f_event_from\n"
            + ",concat('层级',f_session_rank,'_',if(f_event is null or f_event='','未知',f_event)) as f_event_to \n"
            + ",@f_category \\:= if(@f_user_id = f_user_id and @f_session_id = f_session_id, "
            + "concat('层级',@f_rank,'_',if(@f_category is null or @f_category='','未知',@f_category)), null) "
            + "as f_category_from\n"
            + ",concat('层级',f_session_rank,'_',if(f_category is null or f_category='','未知',f_category))  "
            + "as f_category_to \n"
            + ",@f_subcategory \\:= if(@f_user_id = f_user_id and @f_session_id = f_session_id, "
            + "concat('层级',@f_rank,'_',if(@f_subcategory is null or @f_subcategory='','未知',@f_subcategory)), null) "
            + "as f_subcategory_from\n"
            + ",concat('层级',f_session_rank,'_',if(f_subcategory is null or f_subcategory='','未知',f_subcategory))  "
            + "as f_subcategory_to\n"
            + ",@f_session_id \\:= f_session_id\n"
            + ",@f_user_id \\:= f_user_id\n"
            + ",@f_event \\:= f_event\n"
            + ",@f_category \\:= f_category\n"
            + ",@f_subcategory \\:= f_subcategory\n"
            + ",@f_rank \\:= f_session_rank\n"
            + ",f_user_scene_version_id\n"
            + "from \n"
            + "t_session_log_event_distinct a,(select @f_user_id \\:= null, @f_event \\:= null, @f_session_id \\:= 0, "
            + "@f_category \\:= null, @f_subcategory \\:= null, @f_rank \\:= 0, @f_event_path = null) b \n"
            + " where a.f_user_scene_version_id=?1 and a.f_make_version_id=?2\n"
            + "order by f_user_id asc, f_session_id asc ,f_session_rank asc\n"
            + ") t1 \n"
            + "\n"
            + "union all \n"
            + "\n"
            + "select \n"
            + "m1.f_user_id\n"
            + ",m1.f_age\n"
            + ",m1.f_sex\n"
            + ",m1.f_province\n"
            + ",m1.f_city\n"
            + ",m1.f_channel\n"
            + ",m1.f_session_id\n"
            + ",m1.f_session_rank\n"
            + ",m1.f_event_from\n"
            + ",m1.f_event_to\n"
            + ",m1.f_category_from\n"
            + ",m1.f_category_to \n"
            + ",m1.f_subcategory_from\n"
            + ",m1.f_subcategory_to\n"
            + ",f_user_scene_version_id\n"
            + "from \n"
            + "(select \n"
            + "f_user_id\n"
            + ",f_age\n"
            + ",f_sex\n"
            + ",f_province\n"
            + ",f_city\n"
            + ",f_channel\n"
            + ",f_session_id\n"
            + ",f_session_rank\n"
            + ",f_user_scene_version_id\n"
            + ",concat('层级',f_session_rank,'_',if(f_event is null or f_event='','未知',f_event)) as f_event_from \n"
            + ",concat('层级',f_session_rank+1,'_结束') as f_event_to \n"
            + ",concat('层级',f_session_rank,'_',if(f_category is null or f_category='','未知',f_category))"
            +
            " as f_category_from\n"
            + ",concat('层级',f_session_rank+1,'_生命周期') as f_category_to \n"
            + ",concat('层级',f_session_rank,'_',if(f_subcategory is null or f_subcategory='','未知',f_subcategory)) "
            +
            "as f_subcategory_from\n"
            + ",concat('层级',f_session_rank+1,'_其他') as f_subcategory_to \n"
            + "from \n"
            + "t_session_log_event_distinct\n"
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + ") m1 join\n"
            + "(select f_user_id,f_session_id,max(f_session_rank) as max_session_rank from t_session_log_event_distinct"
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + "group by f_user_id,f_session_id) m2 \n"
            + "on m1.f_user_id = m2.f_user_id and m1.f_session_id = m2.f_session_id and "
            + "m1.f_session_rank = m2.max_session_rank\n"
            + ") t1\n"
            + "left join \n"
            + "(\n"
            + "select \n"
            + "f_user_id\n"
            + ",f_session_id\n"
            + ",GROUP_CONCAT(concat('层级',f_session_rank,'_',if(f_event is null or f_event='','未知',f_event)) "
            + "order by  f_session_rank asc separator '/') as f_event_path\n"
            + ",GROUP_CONCAT(concat('层级',f_session_rank,'_',if(f_category is null or f_category='','未知',f_category))"
            + "order by  f_session_rank asc  separator '/') as f_category_path\n"
            + ",GROUP_CONCAT(concat('层级',f_session_rank,'_',if(f_subcategory is null or "
            + "f_subcategory='','未知',f_subcategory)) order by  f_session_rank asc  separator '/') "
            + "as f_subcategory_path\n"
            + "from \n"
            + "(\n"
            + "select \n"
            + "*\n"
            + "from \n"
            + "t_session_log_event_distinct\n"
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2\n"
            + ") t\n"
            + "group by \n"
            + "f_user_id\n"
            + ",f_session_id\n"
            + ") t2 \n"
            + "on t1.f_user_id = t2.f_user_id and t1.f_session_id = t2.f_session_id\n"
            + "where f_event_from is not null\n"
            + "group by \n"
            + "t1.f_age\n"
            + ",t1.f_sex\n"
            + ",t1.f_province\n"
            + ",t1.f_city\n"
            + ",t1.f_channel \n"
            + ",t1.f_event_from\n"
            + ",t1.f_event_to\n"
            + ",t2.f_event_path\n"
            + ",t1.f_category_from\n"
            + ",t1.f_category_to\n"
            + ",t2.f_category_path\n"
            + ",t1.f_subcategory_from\n"
            + ",t1.f_subcategory_to\n"
            + ",t2.f_subcategory_path\n"
            + ",t1.f_user_scene_version_id", nativeQuery = true)
    List<Map<String, Object>> getSankeyDistinct(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 获取event事件去重路径结果（进行筛选查询）
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select f_category_from,f_category_to,f_subcategory_from,f_subcategory_to,f_event_from,f_event_to,"
            + " sum(f_weight_session) as event_session, "
            + " sum(f_weight_user) as event_user, "
            + " sum(f_weight_pv) as event_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?17,'') !='',f_category_path like concat('%', ?17, '%'),1=1) "
            + " and if(IFNULL(?18,'') !='',f_subcategory_path like concat('%', ?18, '%'),1=1) "
            + " and if(IFNULL(?19,'') !='',f_event_path like concat('%', ?19, '%'),1=1) "
            + " and if(IFNULL(?20,'') !='',f_category_path like concat('%', ?20, '%'),1=1) "
            + " and if(IFNULL(?21,'') !='',f_subcategory_path like concat('%', ?21, '%'),1=1) "
            + " and if(IFNULL(?22,'') !='',f_event_path like concat('%', ?22, '%'),1=1) "
            + " and if(IFNULL(?23,'') !='',f_category_path like concat('%', ?23, '%'),1=1) "
            + " and if(IFNULL(?24,'') !='',f_subcategory_path like concat('%', ?24, '%'),1=1) "
            + " and if(IFNULL(?25,'') !='',f_event_path like concat('%', ?25, '%'),1=1) "
            + " and if(IFNULL(?26,'') !='',f_category_path like concat('%', ?26, '%'),1=1) "
            + " and if(IFNULL(?27,'') !='',f_subcategory_path like concat('%', ?27, '%'),1=1) "
            + " and if(IFNULL(?28,'') !='',f_event_path like concat('%', ?28, '%'),1=1) "
            + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
            + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
            + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',substring(substring_index(f_category_from,'_',1),3,10)>=?6,1=1) "
            + " and if(IFNULL(?7,'') !='',substring(substring_index(f_category_to,'_',1),3,10)<=?7,1=1)"
            + " group by f_category_from,f_category_to,f_subcategory_from,f_subcategory_to,f_event_from,f_event_to "
            + " having event_session>=?8 and event_session<=?12"
            + " and event_user>=?13 and event_user<=?14"
            + " and event_pv>=?15 and event_pv<=?16"
            + " order by CONVERT(substring(substring_index(f_category_from,'_',1),3,10),UNSIGNED) asc,"
            + " event_session desc", nativeQuery = true)
    List<Map<String, Object>> getSessionEventDistinctEntity(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFrom, String fEventFrom, Integer fromLayer, Integer toLayer,
            Integer fSessionNumStart, String fCategory, String fSubcategory, String fEvent, Integer fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd,
            String fCategoryFromD1,String fSubcategoryFromD1,String fEventFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,String fEventFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,String fEventFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,String fEventFromD4);



    /**
     * 获取category大类去重路径结果（进行筛选查询）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_category_from,f_category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
            + " and if(IFNULL(?14,'') !='',f_category_path like concat('%', ?14, '%'),1=1) "
            + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
            + " and if(IFNULL(?16,'') !='',f_category_path like concat('%', ?16, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1)"
            + " and if(IFNULL(?4,'') !='',substring(substring_index(f_category_from,'_',1),3,10)>=?4,1=1) "
            + " and if(IFNULL(?5,'') !='',substring(substring_index(f_category_to,'_',1),3,10)<=?5,1=1) "
            + " group by f_category_from,f_category_to "
            + " having category_session>=?6 and category_session<=?8 "
            + " and category_user>=?9 and category_user<=?10"
            + " and category_pv>=?11 and category_pv<=?12 "
            + " order by CONVERT(substring(substring_index(f_category_from,'_',1),3,10),UNSIGNED) asc,"
            + " category_session desc", nativeQuery = true)
    List<Map<String, Object>> getSessionCategoryDistinctEntity(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            int fromLayer, int toLayer, int fSessionNumStart, String fCategory, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd,
            String fCategoryFromD1,String fCategoryFromD2,String fCategoryFromD3,String fCategoryFromD4);


    /**
     * 获取subcategory小类去重路径结果（进行筛选查询）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value =
            "select f_category_from,f_category_to,f_subcategory_from,f_subcategory_to,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_category_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_subcategory_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_category_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_subcategory_path like concat('%', ?20, '%'),1=1) "
                    + " and if(IFNULL(?21,'') !='',f_category_path like concat('%', ?21, '%'),1=1) "
                    + " and if(IFNULL(?22,'') !='',f_subcategory_path like concat('%', ?22, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_subcategory_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',substring(substring_index(f_category_from,'_',1),3,10)>=?5,1=1) "
                    + " and if(IFNULL(?6,'') !='',substring(substring_index(f_category_to,'_',1),3,10)<=?6,1=1)"
                    + " group by f_category_from,f_category_to,f_subcategory_from,f_subcategory_to"
                    + " having subcategory_session>=?7 and subcategory_session<=?10 "
                    + " and subcategory_user>=?11 and subcategory_user<=?12"
                    + " and subcategory_pv>=?13 and subcategory_pv<=?14"
                    + " order by CONVERT(substring(substring_index(f_category_from,'_',1),3,10),UNSIGNED) asc,"
                    + " subcategory_session desc", nativeQuery = true)
    List<Map<String, Object>> getSessionSubcategoryDistinctEntity(String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFrom, String fSubcategoryFrom, int fromLayer, int toLayer, int fSessionNumStart,
            String fCategory, String fSubcategory, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd,
            String fCategoryFromD1,String fSubcategoryFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4);




    /**
     * 获取分渠道大类去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_channel,f_category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 "
            + " group by f_channel,f_category_from"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCategorySankeyCntByChannelFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom, String fCategory,
            String fCategoryFromD1,String fCategoryFromD2,String fCategoryFromD3,String fCategoryFromD4,
            String fCategoryFromDA);




    /**
     * 获取分渠道大类去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_channel,f_category_from,f_category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 and f_category_to=?10 "
            + " group by f_channel,f_category_from,f_category_to"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCategorySankeyCntByChannelTo(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom, String fCategory,
            String fCategoryFromD1,String fCategoryFromD2,String fCategoryFromD3,String fCategoryFromD4,
            String fCategoryFromDA,String fCategoryToDA);


    /**
     * 获取分渠道小类去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value =
            "select f_channel,f_category_from,f_subcategory_from,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15"
                    + " group by f_channel,f_category_from,f_subcategory_from"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubSankeyCntByChannelFrom(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFromD, String fSubcategoryFromD,
            String fCategoryD, String fSubcategoryD,
            String fCategoryFromD1,String fSubcategoryFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,
            String fSubcategoryFromDA);



    /**
     * 获取分渠道小类去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value =
            "select f_channel,f_category_from,f_category_to,f_subcategory_from,f_subcategory_to,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15 and f_subcategory_to=?16"
                    + " group by f_channel,f_category_from,"
                    + " f_category_to,f_subcategory_from,f_subcategory_to"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubCategorySankeyCntByChannelTo(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFromD, String fSubcategoryFromD,
            String fCategoryD, String fSubcategoryD,
            String fCategoryFromD1,String fSubcategoryFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,
            String fSubcategoryFromDA,String fSubcategoryToDA);



    /**
     * 获取分渠道事件去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select f_channel,f_category_from,f_subcategory_from,f_event_from,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?20, '%'),1=1) "
                    + " and f_event_from=?21"
                    + " group by f_channel,f_category_from,f_subcategory_from,f_event_from"
                    + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntByChannelFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFromD,
            String fSubcategoryFromD, String fEventFromD,
            String fCategoryD, String fSubcategoryD, String fEventD,
            String fCategoryFromD1,String fSubcategoryFromD1,String fEventFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,String fEventFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,String fEventFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,String fEventFromD4,
            String fEventFromA);



    /**
     * 获取分渠道事件去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select f_channel,f_category_from,f_category_to,f_subcategory_from,"
                    + " f_subcategory_to,f_event_from,f_event_to,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?20, '%'),1=1) "
                    + " and f_event_from=?21 and f_event_to=?22 "
                    + " group by f_channel,f_category_from,f_category_to,"
                    + " f_subcategory_from,f_subcategory_to,f_event_from,f_event_to"
                    + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntByChannelTo(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFromD,
            String fSubcategoryFromD, String fEventFromD,
             String fCategoryD, String fSubcategoryD, String fEventD,
            String fCategoryFromD1,String fSubcategoryFromD1,String fEventFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,String fEventFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,String fEventFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,String fEventFromD4,
            String fEventFromDA, String fEventToDA);





    /**
     * 获取分省份大类去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_province,f_category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 "
            + " group by f_province,f_category_from"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCateSankeyCntByProFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFromD, String fCategoryD,
            String fCategoryFromD1,String fCategoryFromD2,String fCategoryFromD3,String fCategoryFromD4,
            String fCategoryFromDA);




    /**
     * 获取分省份大类去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_province,f_category_from,f_category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 and f_category_to=?10 "
            + " group by f_province,f_category_from,f_category_to"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCateSankeyCntByProTo(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFromD, String fCategoryD,
            String fCategoryFromD1,String fCategoryFromD2,String fCategoryFromD3,String fCategoryFromD4,
            String fCategoryFromDA,String fCategoryToDA);



    /**
     * 获取分省份小类去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value =
            "select f_province,f_category_from,f_subcategory_from,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15"
                    + " group by f_province,f_category_from,f_subcategory_from"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubSankeyCntByProFrom(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFromD, String fSubcategoryFromD,
            String fCategoryD, String fSubcategoryD,
            String fCategoryFromD1,String fSubcategoryFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,
            String fSubcategoryFromDA);



    /**
     * 获取分省份小类去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value =
            "select f_province,f_category_from,f_category_to,f_subcategory_from,f_subcategory_to,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15 and f_subcategory_to=?16 "
                    + " group by f_province,f_category_from,"
                    + " f_category_to,f_subcategory_from,f_subcategory_to"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubSankeyCntByProTo(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFromD, String fSubcategoryFromD,
            String fCategoryD, String fSubcategoryD,
            String fCategoryFromD1,String fSubcategoryFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,
            String fSubcategoryFromDA,String fSubcategoryToDA);




    /**
     * 获取分省份事件去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select f_province,f_category_from,f_subcategory_from,f_event_from,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?20, '%'),1=1) "
                    + " and f_event_from=?21"
                    + " group by f_province,f_category_from,f_subcategory_from,f_event_from"
                    + " order by event_session desc", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntByProFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFromD, String fEventFromD,String fCategoryD, String fSubcategoryD, String fEventD,
            String fCategoryFromD1,String fSubcategoryFromD1,String fEventFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,String fEventFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,String fEventFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,String fEventFromD4,
            String fEventFromDA);



    /**
     * 获取分省份事件去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select f_province,f_category_from,f_category_to,f_subcategory_from,"
                    + " f_subcategory_to,f_event_from,f_event_to,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and f_event_from=?21 and f_event_to=?22 "
                    + " group by f_province,f_category_from,f_category_to,"
                    + " f_subcategory_from,f_subcategory_to,f_event_from,f_event_to"
                    + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntByProTo(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFromD, String fEventFromD, String fCategoryD, String fSubcategoryD, String fEventD,
            String fCategoryFromD1,String fSubcategoryFromD1,String fEventFromD1,
            String fCategoryFromD2,String fSubcategoryFromD2,String fEventFromD2,
            String fCategoryFromD3,String fSubcategoryFromD3,String fEventFromD3,
            String fCategoryFromD4,String fSubcategoryFromD4,String fEventFromD4,
            String fEventFromDA, String fEventToDA);




    /**
     * 获取分性别大类去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_sex,f_category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 "
            + " group by f_sex,f_category_from"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCateSankeyCntBySexFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFromD, String fCategoryD,
            String fCategoryFromD1,String fCategoryFromD2,String fCategoryFromD3,String fCategoryFromD4,
            String fCategoryFromDA);




    /**
     * 获取分性别大类去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_sex,f_category_from,f_category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 and f_category_to=?10 "
            + " group by f_sex,f_category_from,f_category_to"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCateSankeyCntBySexTo(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom, String fCategory,
            String fCategoryFrom1,String fCategoryFrom2,String fCategoryFrom3,String fCategoryFrom4,
            String fCategoryFromA,String fCategoryToA);



    /**
     * 获取分性别小类去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value =
            "select f_sex,f_category_from,f_subcategory_from,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15"
                    + " group by f_sex,f_category_from,f_subcategory_from"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubSankeyCntBySexFrom(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFrom, String fSubcategoryFrom,
            String fCategory, String fSubcategory,
            String fCategoryFrom1,String fSubcategoryFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,
            String fSubcategoryFromA);



    /**
     * 获取分性别小类去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value =
            "select f_sex,f_category_from,f_category_to,f_subcategory_from,f_subcategory_to,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15 and f_subcategory_to=?16"
                    + " group by f_sex,f_category_from,"
                    + " f_category_to,f_subcategory_from,f_subcategory_to"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubSankeyCntBySexTo(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFrom, String fSubcategoryFrom,
            String fCategory, String fSubcategory,
            String fCategoryFrom1,String fSubcategoryFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,
            String fSubcategoryFromA,String fSubcategoryToA);



    /**
     * 获取分性别事件去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select f_sex,f_category_from,f_subcategory_from,f_event_from,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?20, '%'),1=1) "
                    + " and f_event_from=?21"
                    + " group by f_sex,f_category_from,f_subcategory_from,f_event_from"
                    + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntBySexFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFrom, String fEventFrom, String fCategory, String fSubcategory, String fEvent,
            String fCategoryFrom1,String fSubcategoryFrom1,String fEventFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,String fEventFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,String fEventFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,String fEventFrom4,
            String fEventFromA);



    /**
     * 获取分性别事件去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select f_sex,f_category_from,f_category_to,f_subcategory_from,"
                    + " f_subcategory_to,f_event_from,f_event_to,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?20, '%'),1=1) "
                    + " and f_event_from=?21 and f_event_to=?22 "
                    + " group by f_sex,f_category_from,f_category_to,"
                    + " f_subcategory_from,f_subcategory_to,f_event_from,f_event_to"
                    + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntBySexTo(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFrom, String fEventFrom, String fCategory, String fSubcategory, String fEvent,
            String fCategoryFrom1,String fSubcategoryFrom1,String fEventFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,String fEventFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,String fEventFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,String fEventFrom4,
            String fEventFromA, String fEventToA);




    /**
     * 获取分年龄段大类去重路径结果（进行筛选查询from）
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
            + " f_category_from,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 "
            + " group by age,f_category_from"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCateSankeyCntByAgeFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom, String fCategory,
            String fCategoryFrom1,String fCategoryFrom2,String fCategoryFrom3,String fCategoryFrom4,
            String fCategoryFromA);




    /**
     * 获取分年龄段大类去重路径结果（进行筛选查询to）
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
            + " f_category_from,f_category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?4,'') !='',f_category_path like concat('%', ?4, '%'),1=1) "
            + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
            + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
            + " and if(IFNULL(?8,'') !='',f_category_path like concat('%', ?8, '%'),1=1)"
            + " and f_category_from=?9 and f_category_to=?10 "
            + " group by age,f_category_from,f_category_to"
            + " order by category_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDCateSankeyCntByAgeTo(
            String fUserSceneVersionId, int fMakeVersionId, String fCategoryFrom, String fCategory,
            String fCategoryFrom1,String fCategoryFrom2,String fCategoryFrom3,String fCategoryFrom4,
            String fCategoryFromA,String fCategoryToA);



    /**
     * 获取分年龄段小类去重路径结果（进行筛选查询from）
     *
     * @return
     */
    @Modifying
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
                    + " f_category_from,f_subcategory_from,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15"
                    + " group by age,f_category_from,f_subcategory_from"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubSankeyCntByAgeFrom(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFrom, String fSubcategoryFrom,
            String fCategory, String fSubcategory,
            String fCategoryFrom1,String fSubcategoryFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,
            String fSubcategoryFromA);



    /**
     * 获取分年龄段小类去重路径结果（进行筛选查询to）
     *
     * @return
     */
    @Modifying
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
                    + " f_category_from,f_category_to,f_subcategory_from,f_subcategory_to,"
                    + " sum(f_weight_session) as subcategory_session,"
                    + " sum(f_weight_user) as subcategory_user,"
                    + " sum(f_weight_pv) as subcategory_pv"
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_category_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_subcategory_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_subcategory_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_category_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_subcategory_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_subcategory_path like concat('%', ?14, '%'),1=1) "
                    + " and f_subcategory_from=?15 and f_subcategory_to=?16"
                    + " group by age,f_category_from,"
                    + " f_category_to,f_subcategory_from,f_subcategory_to"
                    + " order by subcategory_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDSubSankeyCntByAgeTo(
            String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryFrom, String fSubcategoryFrom,
            String fCategory, String fSubcategory,
            String fCategoryFrom1,String fSubcategoryFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,
            String fSubcategoryFromA,String fSubcategoryToA);



    /**
     * 获取分年龄段事件去重路径结果（进行筛选查询from）
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
                    + " f_category_from,f_subcategory_from,f_event_from,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?20, '%'),1=1) "
                    + " and f_event_from=?21"
                    + " group by age,f_category_from,f_subcategory_from,f_event_from"
                    + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntByAgeFrom(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFrom, String fEventFrom,  String fCategory, String fSubcategory, String fEvent,
            String fCategoryFrom1,String fSubcategoryFrom1,String fEventFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,String fEventFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,String fEventFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,String fEventFrom4,
            String fEventFromA);



    /**
     * 获取分年龄段事件去重路径结果（进行筛选查询to）
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
                    + " f_category_from,f_category_to,f_subcategory_from,"
                    + " f_subcategory_to,f_event_from,f_event_to,"
                    + " sum(f_weight_session) as event_session, "
                    + " sum(f_weight_user) as event_user, "
                    + " sum(f_weight_pv) as event_pv "
                    + " from t_session_event_distinct_sankey "
                    + " where f_user_scene_version_id=?1 and f_make_version_id=?2"
                    + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
                    + " and if(IFNULL(?4,'') !='',f_subcategory_path like concat('%', ?4, '%'),1=1) "
                    + " and if(IFNULL(?5,'') !='',f_event_path like concat('%', ?5, '%'),1=1) "
                    + " and if(IFNULL(?6,'') !='',f_category_path like concat('%', ?6, '%'),1=1) "
                    + " and if(IFNULL(?7,'') !='',f_subcategory_path like concat('%', ?7, '%'),1=1) "
                    + " and if(IFNULL(?8,'') !='',f_event_path like concat('%', ?8, '%'),1=1) "
                    + " and if(IFNULL(?9,'') !='',f_category_path like concat('%', ?9, '%'),1=1) "
                    + " and if(IFNULL(?10,'') !='',f_subcategory_path like concat('%', ?10, '%'),1=1) "
                    + " and if(IFNULL(?11,'') !='',f_event_path like concat('%', ?11, '%'),1=1) "
                    + " and if(IFNULL(?12,'') !='',f_category_path like concat('%', ?12, '%'),1=1) "
                    + " and if(IFNULL(?13,'') !='',f_subcategory_path like concat('%', ?13, '%'),1=1) "
                    + " and if(IFNULL(?14,'') !='',f_event_path like concat('%', ?14, '%'),1=1) "
                    + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
                    + " and if(IFNULL(?16,'') !='',f_subcategory_path like concat('%', ?16, '%'),1=1) "
                    + " and if(IFNULL(?17,'') !='',f_event_path like concat('%', ?17, '%'),1=1) "
                    + " and if(IFNULL(?18,'') !='',f_category_path like concat('%', ?18, '%'),1=1) "
                    + " and if(IFNULL(?19,'') !='',f_subcategory_path like concat('%', ?19, '%'),1=1) "
                    + " and if(IFNULL(?20,'') !='',f_event_path like concat('%', ?20, '%'),1=1) "
                    + " and f_event_from=?21 and f_event_to=?22 "
                    + " group by f_channel,f_category_from,f_category_to,"
                    + " f_subcategory_from,f_subcategory_to,f_event_from,f_event_to"
                    + " order by event_session desc ", nativeQuery = true)
    List<Map<String, Object>> getDEventSankeyCntByAgeTo(String fUserSceneVersionId,
            int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFrom, String fEventFrom, String fCategory, String fSubcategory, String fEvent,
            String fCategoryFrom1,String fSubcategoryFrom1,String fEventFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,String fEventFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,String fEventFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,String fEventFrom4,
            String fEventFromA, String fEventToA);


}
