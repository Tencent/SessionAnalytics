/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName SessionEventEntity
 * @Description session路径event 不去重汇总表
 * @Author author
 * @Date 2023/03/04 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_session_event_sankey")
@Data
@DynamicUpdate
public class SessionEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    // 自增ID
    private String fAge; //'用户年龄'
    private String fSex; //'用户性别'
    private String fProvince; //'用户省份'
    private String fCity; //'用户城市'
    private String fChannel; //'用户来源渠道'
    private String fEventFrom; //'f_event_from'
    private String fEventTo; //'f_event_to'
    private int fWeightSession; //'f_weight_session'
    private int fWeightUser; //'f_weight_user'
    private int fWeightPv; //'f_weight_pv'
    private String fEventPath; //'f_event_path'
    private String fCategoryFrom; //'f_category_from'
    private String fCategoryTo; //'f_category_to'
    private String fCategoryPath; //'f_category_path'
    private String fSubcategoryFrom; //'f_subcategory_from'
    private String fSubcategoryTo; //'f_subcategory_to'
    private String fSubcategoryPath; //'f_subcategory_path'
    private int fMakeVersionId; //'治理版本id'
    private String fUserSceneVersionId; //'f_user_scene_version_id'
}
