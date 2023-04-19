/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
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
public interface TreeSessionDistinctEventRepository extends JpaRepository<SessionEventDistinctEntity, Integer> {


    /**
     * 获取树图事件去重路径结果
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select concat(f_event_from,'_&_',md5(path_from)) as f_event_from,"
            + " concat(f_event_to,'_&_',md5(path_to)) as f_event_to,event_session,event_user,event_pv from "
            + " (select substring(substring_index(f_event_path,'/',"
            + " substring(substring_index(f_category_from,'_',1),3,10)),1,500) as path_from,"
            + " substring(substring_index(f_event_path,'/',"
            + " substring(substring_index(f_category_to,'_',1),3,10)),1,500) as path_to,"
            + " f_event_from,f_event_to,"
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
            + " group by path_from,path_to,f_event_from,f_event_to "
            + " having event_session>=?8 and event_session<=?12"
            + " and event_user>=?13 and event_user<=?14"
            + " and event_pv>=?15 and event_pv<=?16"
            + " order by CONVERT(substring(substring_index(f_category_from,'_',1),3,10),UNSIGNED) asc,"
            + "event_session desc)t", nativeQuery = true)
    List<Map<String, Object>> getTreeSessionEventDistinctEntity(
            String fUserSceneVersionId, int fMakeVersionId, String fCategoryFrom,
            String fSubcategoryFrom, String fEventFrom, int fromLayer, int toLayer, int fSessionNumStart,
            String fCategory, String fSubcategory, String fEvent, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd,
            String fCategoryFrom1,String fSubcategoryFrom1,String fEventFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,String fEventFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,String fEventFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4,String fEventFrom4);


    /**
     * 获取树图大类去重路径结果
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select concat(f_category_from,'_&_',md5(path_from)) as f_category_from,"
            + " concat(f_category_to,'_&_',md5(path_to)) as f_category_to,"
            + " category_session,category_user,category_pv from"
            + " (select substring(substring_index(f_category_path,'/',"
            + " substring(substring_index(f_category_from,'_',1),3,10)),1,500) as path_from,"
            + " substring(substring_index(f_category_path,'/',"
            + " substring(substring_index(f_category_to,'_',1),3,10)),1,500) as path_to,"
            + " f_category_from,f_category_to,"
            + " sum(f_weight_session) as category_session, "
            + " sum(f_weight_user) as category_user, "
            + " sum(f_weight_pv) as category_pv "
            + " from t_session_event_distinct_sankey"
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and if(IFNULL(?3,'') !='',f_category_path like concat('%', ?3, '%'),1=1) "
            + " and if(IFNULL(?13,'') !='',f_category_path like concat('%', ?13, '%'),1=1) "
            + " and if(IFNULL(?14,'') !='',f_category_path like concat('%', ?14, '%'),1=1) "
            + " and if(IFNULL(?15,'') !='',f_category_path like concat('%', ?15, '%'),1=1) "
            + " and if(IFNULL(?16,'') !='',f_category_path like concat('%', ?16, '%'),1=1) "
            + " and if(IFNULL(?7,'') !='',f_category_path like concat('%', ?7, '%'),1=1)"
            + " and if(IFNULL(?4,'') !='',substring(substring_index(f_category_from,'_',1),3,10)>=?4,1=1) "
            + " and if(IFNULL(?5,'') !='',substring(substring_index(f_category_to,'_',1),3,10)<=?5,1=1) "
            + " group by path_from,path_to,f_category_from,f_category_to "
            + " having category_session>=?6 and category_session<=?8 "
            + " and category_user>=?9 and category_user<=?10 "
            + " and category_pv>=?11 and category_pv<=?12 "
            + " order by CONVERT(substring(substring_index(f_category_from,'_',1),3,10),UNSIGNED) asc, "
            + " category_session desc)t", nativeQuery = true)
    List<Map<String, Object>> getTreeCateDisEntity(
            String fUserSceneVersionId, int fMakeVersionId,
            String fCategoryFrom, int fromLayer, int toLayer, int fSessionNumStart,
            String fCategory, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd,
            String fCategoryFrom1,String fCategoryFrom2,String fCategoryFrom3,String fCategoryFrom4);


    /**
     * 获取树图小类去重路径结果
     *
     * @return
     */
    @Transactional
    @Query(value = "select concat(f_subcategory_from,'_&_',md5(path_from)) as f_subcategory_from,"
            + " concat(f_subcategory_to,'_&_',md5(path_to)) as f_subcategory_to,"
            + " subcategory_session,subcategory_user,subcategory_pv from "
            + " (select substring(substring_index(f_subcategory_path,'/',"
            + " substring(substring_index(f_category_from,'_',1),3,10)),1,500) as path_from,"
            + " substring(substring_index(f_subcategory_path,'/',"
            + " substring(substring_index(f_category_to,'_',1),3,10)),1,500) as path_to,"
            + " f_subcategory_from,f_subcategory_to,"
            + " sum(f_weight_session) as subcategory_session,"
            + " sum(f_weight_user) as subcategory_user,"
            + " sum(f_weight_pv) as subcategory_pv"
            + " from t_session_event_distinct_sankey "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
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
            + " group by path_from,path_to,f_subcategory_from,f_subcategory_to "
            + " having subcategory_session>=?7 and subcategory_session<=?10 "
            + " and subcategory_user>=?11 and subcategory_user<=?12"
            + " and subcategory_pv>=?13 and subcategory_pv<=?14"
            + " order by CONVERT(substring(substring_index(f_category_from,'_',1),3,10),UNSIGNED) asc,"
            + " subcategory_session desc)t", nativeQuery = true)
    List<Map<String, Object>> getTreeSubDisEntity(
            String fUserSceneVersionId, int fMakeVersionId,
            String fCategoryFrom, String fSubcategoryFrom, int fromLayer,
            int toLayer, int fSessionNumStart, String fCategory, String fSubcategory, int fSessionNumEnd,
            int fUserNumStart,int fUserNumEnd,
            int fPVNumStart,int fPVNumEnd,
            String fCategoryFrom1,String fSubcategoryFrom1,
            String fCategoryFrom2,String fSubcategoryFrom2,
            String fCategoryFrom3,String fSubcategoryFrom3,
            String fCategoryFrom4,String fSubcategoryFrom4);
}
