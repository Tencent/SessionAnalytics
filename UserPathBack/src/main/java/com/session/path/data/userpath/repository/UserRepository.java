/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;


/**
 * describe: 权限管理
 *
 * @author author
 * @date 2023/03/04
 */


import com.session.path.data.userpath.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 插入用户权限表
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert into t_user(username,password) values(?1,?2)", nativeQuery = true)
    int insertUsers(String username,String password);



    /**
     * 获取用户权限表
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user where username=?1", nativeQuery = true)
    User getUserInfo(String username);


    /**
     * 判断注册用户是否存在
     *
     * @return
     */
    @Transactional
    @Query(value = "select id from t_user where username=?1", nativeQuery = true)
    String getUserNameExist(String username);

}

