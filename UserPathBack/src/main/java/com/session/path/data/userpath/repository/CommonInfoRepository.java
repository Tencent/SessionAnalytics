/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: 保存  更新  插入操作
 *
 * @author author
 * @date 2023/02/25
 */

@Component
public class CommonInfoRepository {

    @PersistenceContext
    EntityManager entityManager;
    static final Logger logger = LoggerFactory
            .getLogger(CommonInfoRepository.class);

    @Transactional
    @Modifying
    public int updateInfo(String updateSql) {

        return entityManager.createNativeQuery(updateSql).executeUpdate();

    }


    @Transactional
    @Modifying
    public void saveInfo(String addSql) {
        logger.info("开始插入");
        entityManager.createNativeQuery(addSql).executeUpdate();
        logger.info("插入结束");
    }

    @Transactional
    public int selectInfo(String sqlStr) {
        int result = entityManager.createNativeQuery(sqlStr).getFirstResult();
        return result;
    }

}
