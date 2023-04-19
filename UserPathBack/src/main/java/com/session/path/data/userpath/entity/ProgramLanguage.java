//package com.session.path.data.userpath.entity;
//
//import com.alibaba.excel.metadata.BaseRowModel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.neo4j.ogm.annotation.Id;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Property;
//
//
//@NodeEntity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class ProgramLanguage extends BaseRowModel {
//
//    /**
//     * 指定id后，就可将spring-data-neo4j自带的增删改查默认的id改为自己定义的id
//     */
//    @Id
//    private String uuid;
//
//    /**
//     * 名称
//     */
//    @Property(name = "name")
//    private String name;
//
//    /**
//     * 简介
//     */
//    private String introduction;
//
//    /**
//     * 图片文本文档
//     */
//    private String textDocument;
//
//    /**
//     * 视频文档
//     */
//    private String video;
//
//    /**
//     * 修改时间
//     */
//    private Long modifyTime;
//
//}
//
