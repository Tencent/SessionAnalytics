CREATE TABLE `t_user_log_upload_split_status` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_upload_user` varchar(128) DEFAULT NULL COMMENT '上传人',
  `f_upload_name` varchar(128) DEFAULT NULL COMMENT '上传场景标题',
  `f_scene_desc` varchar(1024) DEFAULT NULL COMMENT '场景描述',
  `f_scene_id` varchar(128) DEFAULT NULL COMMENT '场景id',
  `f_version_id` int(10) DEFAULT 0 COMMENT '版本id',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_session_event` varchar(128) DEFAULT NULL COMMENT '切分session的事件',
  `f_session_split_time` int(10) DEFAULT NULL COMMENT '切分session间隔时间分钟',
  `f_upload_status` int(10) DEFAULT '0' COMMENT '是否上传完成',
  `f_upload_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_upload_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_make_status` int(10) DEFAULT '0' COMMENT '页面是否治理完成',
  `f_make_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_make_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_session_split_status` int(10) DEFAULT '0' COMMENT '是否seesion切分完成',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='excel上传切分状态'


alter table t_user_log_upload_split_status add index idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id);


0:等待中 1：成功  2：进行中  3：失败


CREATE TABLE `t_user_log_orig` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `f_age` varchar(10) DEFAULT '0' COMMENT '用户年龄',
  `f_sex` varchar(10) DEFAULT '未知' COMMENT '用户性别',
  `f_province` varchar(100) DEFAULT '未知' COMMENT '用户省份',
  `f_city` varchar(100) DEFAULT '未知' COMMENT '用户城市',
  `f_channel` varchar(128) DEFAULT '未知' COMMENT '用户来源渠道',
  `f_event` varchar(128) DEFAULT NULL COMMENT 'event页面曝光点击',
  `f_event_detail` varchar(128) DEFAULT NULL COMMENT 'event path',
  `f_client_time` varchar(128) DEFAULT NULL COMMENT '客户端时间',
  `f_category` varchar(128) DEFAULT NULL COMMENT '大类',
  `f_subcategory` varchar(128) DEFAULT NULL COMMENT '小类',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户原始上传日志'





CREATE TABLE `t_session_log_event` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `f_age` varchar(10) DEFAULT '0' COMMENT '用户年龄',
  `f_sex` varchar(10) DEFAULT '未知' COMMENT '用户性别',
  `f_province` varchar(100) DEFAULT '未知' COMMENT '用户省份',
  `f_city` varchar(100) DEFAULT '未知' COMMENT '用户城市',
  `f_channel` varchar(128) DEFAULT '未知' COMMENT '用户来源渠道',
  `f_session_id` int(20) DEFAULT NULL COMMENT 'session_id',
  `f_session_rank` int(10) DEFAULT NULL COMMENT 'session_rank',
  `f_event` varchar(128) DEFAULT NULL COMMENT 'event页面曝光点击',
  `f_event_detail` varchar(128) DEFAULT NULL COMMENT 'event path',
  `f_client_time` varchar(128) DEFAULT NULL COMMENT '客户端时间',
  `f_category` varchar(128) DEFAULT NULL COMMENT '大类',
  `f_subcategory` varchar(128) DEFAULT NULL COMMENT '小类',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户日志session event'


alter table t_session_log_event add index idx_user_version_make(f_user_scene_version_id,f_make_version_id,f_user_id);


alter table t_session_log_event add index idx_user_version_make(f_user_scene_version_id,f_make_version_id,f_user_id);

alter table t_session_log_event add index idx_user_version_make_province(f_user_scene_version_id,f_make_version_id,f_user_id,f_province);
alter table t_session_log_event add index idx_user_version_make_age(f_user_scene_version_id,f_make_version_id,f_user_id,f_age);
alter table t_session_log_event add index idx_user_version_make_sex(f_user_scene_version_id,f_make_version_id,f_user_id,f_sex);
alter table t_session_log_event add index idx_user_version_make_channel(f_user_scene_version_id,f_make_version_id,f_user_id,f_channel);



 alter table t_session_log_event add index idx_user_version_make_province_session(f_user_scene_version_id,f_make_version_id,f_user_id,f_session_id,f_province);

 alter table t_session_log_event add index idx_user_version_make_age_session(f_user_scene_version_id,f_make_version_id,f_user_id,f_session_id,f_age);

 alter table t_session_log_event add index idx_user_version_make_sex_session(f_user_scene_version_id,f_make_version_id,f_user_id,f_session_id,f_sex);

 alter table t_session_log_event add index idx_user_version_make_category(f_user_scene_version_id,f_make_version_id,f_user_id,f_category);



CREATE TABLE `t_session_log_event_distinct` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `f_age` varchar(10) DEFAULT '0' COMMENT '用户年龄',
  `f_sex` varchar(10) DEFAULT '未知' COMMENT '用户性别',
  `f_province` varchar(100) DEFAULT '未知' COMMENT '用户省份',
  `f_city` varchar(100) DEFAULT '未知' COMMENT '用户城市',
  `f_channel` varchar(128) DEFAULT '未知' COMMENT '用户来源渠道',
  `f_session_id` int(20) DEFAULT NULL COMMENT 'session_id',
  `f_session_rank` int(10) DEFAULT NULL COMMENT 'session_rank',
  `f_event` varchar(128) DEFAULT NULL COMMENT 'event页面曝光点击',
  `f_event_detail` varchar(128) DEFAULT NULL COMMENT 'event path',
  `f_client_time` varchar(128) DEFAULT NULL COMMENT '客户端时间',
  `f_category` varchar(128) DEFAULT NULL COMMENT '大类',
  `f_subcategory` varchar(128) DEFAULT NULL COMMENT '小类',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户日志session event去重'









CREATE TABLE `t_session_event_sankey` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_age` varchar(10) DEFAULT '0' COMMENT '用户年龄',
  `f_sex` varchar(10) DEFAULT '未知' COMMENT '用户性别',
  `f_province` varchar(100) DEFAULT '未知' COMMENT '用户省份',
  `f_city` varchar(100) DEFAULT '未知' COMMENT '用户城市',
  `f_channel` varchar(128) DEFAULT '未知' COMMENT '用户来源渠道',
  `f_event_from` varchar(128) DEFAULT NULL COMMENT 'f_event_from',
  `f_event_to` varchar(128) DEFAULT NULL COMMENT 'f_event_to',
  `f_weight_session` int(10) DEFAULT NULL COMMENT 'f_weight_session',
  `f_weight_user` int(10) DEFAULT NULL COMMENT 'f_weight_user',
  `f_weight_pv` int(10) DEFAULT NULL COMMENT 'f_weight_pv',
  `f_event_path` varchar(1024) DEFAULT NULL COMMENT 'f_event_path',
  `f_category_from` varchar(128) DEFAULT NULL COMMENT 'f_category_from',
  `f_category_to` varchar(128) DEFAULT NULL COMMENT 'f_category_to',
  `f_category_path` varchar(1024) DEFAULT NULL COMMENT 'f_category_path',
  `f_subcategory_from` varchar(128) DEFAULT NULL COMMENT 'f_subcategory_from',
  `f_subcategory_to` varchar(128) DEFAULT NULL COMMENT 'f_subcategory_to',
  `f_subcategory_path` varchar(1024) DEFAULT NULL COMMENT 'f_subcategory_path',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='session路径event sankey汇总表'




CREATE TABLE `t_session_event_distinct_sankey` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_age` varchar(10) DEFAULT '0' COMMENT '用户年龄',
  `f_sex` varchar(10) DEFAULT '未知' COMMENT '用户性别',
  `f_province` varchar(100) DEFAULT '未知' COMMENT '用户省份',
  `f_city` varchar(100) DEFAULT '未知' COMMENT '用户城市',
  `f_channel` varchar(128) DEFAULT '未知' COMMENT '用户来源渠道',
  `f_event_from` varchar(128) DEFAULT NULL COMMENT 'f_event_from',
  `f_event_to` varchar(128) DEFAULT NULL COMMENT 'f_event_to',
  `f_weight_session` int(10) DEFAULT NULL COMMENT 'f_weight_session',
  `f_weight_user` int(10) DEFAULT NULL COMMENT 'f_weight_user',
  `f_weight_pv` int(10) DEFAULT NULL COMMENT 'f_weight_pv',
  `f_event_path` varchar(1024) DEFAULT NULL COMMENT 'f_event_path',
  `f_category_from` varchar(128) DEFAULT NULL COMMENT 'f_category_from',
  `f_category_to` varchar(128) DEFAULT NULL COMMENT 'f_category_to',
  `f_category_path` varchar(1024) DEFAULT NULL COMMENT 'f_category_path',
  `f_subcategory_from` varchar(128) DEFAULT NULL COMMENT 'f_subcategory_from',
  `f_subcategory_to` varchar(128) DEFAULT NULL COMMENT 'f_subcategory_to',
  `f_subcategory_path` varchar(1024) DEFAULT NULL COMMENT 'f_subcategory_path',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='session路径event去重sankey汇总表'





CREATE TABLE `t_role_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_user` varchar(128) DEFAULT NULL COMMENT '用户',
  `f_admin` int(10) DEFAULT 0 COMMENT '是否是管理员',
  `f_business` varchar(128) DEFAULT NULL COMMENT 'ALL/业务',
  `f_version` varchar(128) DEFAULT NULL COMMENT 'ALL/中英文',
  `f_mod` varchar(128) DEFAULT NULL COMMENT 'ALL/模块:home,upload,data_governance,data_select,statistics,sankey,funnel,chord,tree,trend,clustering,management,help',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='权限管理表'


alter table t_role_info add index idx_user(f_user);


CREATE TABLE `t_category_color` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_category` varchar(128) DEFAULT NULL COMMENT '大类',
  `f_color` varchar(128) DEFAULT NULL COMMENT '颜色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='大类颜色记录表'



CREATE TABLE `t_recent_access_records` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_analysis_name_first` varchar(128) DEFAULT NULL COMMENT '桑基图分析/漏斗图分析',
  `f_analysis_name_second` varchar(128) DEFAULT NULL COMMENT '大类分析/小类分析',
  `f_upload_user` varchar(128) DEFAULT NULL COMMENT '上传人',
  `f_admin_user` varchar(128) DEFAULT NULL COMMENT '管理员',
  `f_upload_name` varchar(128) DEFAULT NULL COMMENT '上传场景标题',
  `f_scene_id` varchar(128) DEFAULT NULL COMMENT '场景id',
  `f_version_id` int(10) DEFAULT 0 COMMENT '版本id',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_access_time` varchar(128) DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='访问记录'






CREATE TABLE `t_user_log_cluster_analysis` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `f_user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `f_cluster_type` varchar(128) DEFAULT NULL COMMENT '聚类类型kmeans/dtw',
  `f_label` varchar(10) DEFAULT NULL COMMENT '聚类类别',
  `f_x` varchar(128) DEFAULT NULL COMMENT 'x坐标',
  `f_y` varchar(128) DEFAULT NULL COMMENT 'y坐标',
  `f_is_center` int(10) DEFAULT '0' COMMENT '是否中心点，0否1是',
  `f_cluster_status` int(10) DEFAULT '0' COMMENT '是否聚类完成',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户聚类分析'


alter table t_user_log_cluster_analysis add index idx_user_version_make(f_user_scene_version_id,f_make_version_id,f_user_id);


CREATE TABLE `t_user_log_cluster_k` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `f_cluster_type` varchar(128) DEFAULT NULL COMMENT '聚类类型kmeans/dtw',
  `f_x` varchar(128) DEFAULT NULL COMMENT 'x坐标',
  `f_y` varchar(128) DEFAULT NULL COMMENT 'y坐标',
  `f_is_turnpoint` int(10) DEFAULT '0' COMMENT '是否拐点，0否1是',
  `f_cluster_status` int(10) DEFAULT '0' COMMENT '是否聚类完成',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户聚类分析拐点'



CREATE TABLE `t_user_log_make` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `f_type` varchar(128) DEFAULT NULL COMMENT 'event/category/subcategory',
  `f_pv` int(10) DEFAULT '0' COMMENT 'pv数',
  `f_old` varchar(128) DEFAULT NULL COMMENT 'old值',
  `f_new` varchar(128) DEFAULT NULL COMMENT 'new值',
  `f_make_status` int(10) DEFAULT '0' COMMENT '是否make完成',
  `f_make_version_id` int(10) DEFAULT '0' COMMENT '治理版本号',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户日志数据治理'



CREATE TABLE `t_user_log_orig_make` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `f_age` varchar(10) DEFAULT '0' COMMENT '用户年龄',
  `f_sex` varchar(10) DEFAULT '未知' COMMENT '用户性别',
  `f_province` varchar(100) DEFAULT '未知' COMMENT '用户省份',
  `f_city` varchar(100) DEFAULT '未知' COMMENT '用户城市',
  `f_channel` varchar(128) DEFAULT '未知' COMMENT '用户来源渠道',
  `f_event` varchar(128) DEFAULT NULL COMMENT 'event页面曝光点击',
  `f_event_detail` varchar(128) DEFAULT NULL COMMENT 'event path',
  `f_client_time` varchar(128) DEFAULT NULL COMMENT '客户端时间',
  `f_category` varchar(128) DEFAULT NULL COMMENT '大类',
  `f_subcategory` varchar(128) DEFAULT NULL COMMENT '小类',
  `f_make_version_id` int(10) DEFAULT '0' COMMENT '治理版本号',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY `idx_user_scene_version_make_id` (`f_user_scene_version_id`,`f_make_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='原始上传日志治理结果'


alter table t_user_log_orig_make add index idx_user_scene_version_category(f_user_scene_version_id,f_make_version_id,f_category);

alter table t_user_log_orig_make add index idx_user_scene_version_subcategory(f_user_scene_version_id,f_make_version_id,f_subcategory);

alter table t_user_log_orig_make add index idx_user_scene_version_event(f_user_scene_version_id,f_make_version_id,f_event);



CREATE TABLE `t_user_log_event_pv_distribute` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `f_type` varchar(128) DEFAULT NULL COMMENT 'all/channel/age等',
  `f_is_distinct` int(10) DEFAULT '0' COMMENT '是否去重',
  `f_session_pv_avg` int(10) DEFAULT '0' COMMENT 'avg',
  `f_session_pv_max` int(10) DEFAULT '0' COMMENT 'max',
  `f_session_pv_25p` int(10) DEFAULT '0' COMMENT '25p',
  `f_session_pv_50p` int(10) DEFAULT '0' COMMENT '50p',
  `f_session_pv_75p` int(10) DEFAULT '0' COMMENT '75p',
  `f_pv_status` int(10) DEFAULT '0' COMMENT '是否计算pv完成',
  `f_make_version_id` int(10) DEFAULT '0' COMMENT '治理版本号',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='pv分布计算'


CREATE TABLE `t_user_log_event_dim_pv_distribute` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `f_type` varchar(128) DEFAULT NULL COMMENT 'all/channel/age等',
  `f_is_distinct` int(10) DEFAULT '0' COMMENT '是否去重',
  `f_dim` varchar(128) DEFAULT '0' COMMENT '维度',
  `f_session_pv_num` int(10) DEFAULT '0' COMMENT 'session_pv_num',
  `f_user_pv_num` int(10) DEFAULT '0' COMMENT 'user_pv_num',
  `f_session_pv_avg` int(10) DEFAULT '0' COMMENT 'avg',
  `f_session_pv_max` int(10) DEFAULT '0' COMMENT 'max',
  `f_session_pv_min` int(10) DEFAULT '0' COMMENT 'min',
  `f_session_pv_50p` int(10) DEFAULT '0' COMMENT '50p',
  `f_pv_status` int(10) DEFAULT '0' COMMENT '是否计算pv完成',
  `f_make_version_id` int(10) DEFAULT '0' COMMENT '治理版本号',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='pv分布计算分维度'








CREATE TABLE `t_user_log_event_session_distribute` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `f_type` varchar(128) DEFAULT NULL COMMENT 'all/channel/age等',
  `f_is_distinct` int(10) DEFAULT '0' COMMENT '是否去重',
  `f_session_avg` int(10) DEFAULT '0' COMMENT 'avg',
  `f_session_max` int(10) DEFAULT '0' COMMENT 'max',
  `f_session_25p` int(10) DEFAULT '0' COMMENT '25p',
  `f_session_50p` int(10) DEFAULT '0' COMMENT '50p',
  `f_session_75p` int(10) DEFAULT '0' COMMENT '75p',
  `f_session_status` int(10) DEFAULT '0' COMMENT '是否计算pv完成',
  `f_make_version_id` int(10) DEFAULT '0' COMMENT '治理版本号',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='session分布计算'


CREATE TABLE `t_user_log_event_dim_session_distribute` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `f_type` varchar(128) DEFAULT NULL COMMENT 'all/channel/age等',
  `f_is_distinct` int(10) DEFAULT '0' COMMENT '是否去重',
  `f_dim` varchar(128) DEFAULT '0' COMMENT '维度',
  `f_session_num` int(10) DEFAULT '0' COMMENT 'session_pv_num',
  `f_user_num` int(10) DEFAULT '0' COMMENT 'user_pv_num',
  `f_session_avg` int(10) DEFAULT '0' COMMENT 'avg',
  `f_session_max` int(10) DEFAULT '0' COMMENT 'max',
  `f_session_min` int(10) DEFAULT '0' COMMENT 'min',
  `f_session_50p` int(10) DEFAULT '0' COMMENT '50p',
  `f_session_status` int(10) DEFAULT '0' COMMENT '是否计算session完成',
  `f_make_version_id` int(10) DEFAULT '0' COMMENT '治理版本号',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_scene_version_id` (`f_user_scene_version_id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='session分布计算分维度'



CREATE TABLE `t_session_node` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_category` varchar(128) DEFAULT NULL COMMENT '大类',
  `f_event` varchar(128) DEFAULT NULL COMMENT 'event页面曝光点击',
  `f_weight_pv` int(10) DEFAULT NULL COMMENT 'f_weight_pv',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='数据挖掘node表'




CREATE TABLE `t_session_single` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_event_from` varchar(128) DEFAULT NULL COMMENT 'f_event_from',
  `f_event_to` varchar(128) DEFAULT NULL COMMENT 'f_event_to',
  `f_weight_session` int(10) DEFAULT NULL COMMENT 'f_weight_session',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='数据挖掘single表'


CREATE TABLE `t_session_single_networkx` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `f_event_name` varchar(128) DEFAULT NULL COMMENT '节点名称',
  `f_event_value` varchar(128) DEFAULT NULL COMMENT '中心度值',
  `f_make_version_id` int(10) DEFAULT 0 COMMENT '治理版本id',
  `f_user_scene_version_id` varchar(128) DEFAULT NULL COMMENT '用户场景版本id',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY idx_user_scene_version_make(f_user_scene_version_id,f_make_version_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='特征向量中心度的计算值'



