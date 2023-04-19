/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GsonUtil
 * @Description json工具类
 * @Author author
 * @Date 2023/02/18 14:50
 * @Version 1.0
 **/

public class GsonUtil {

    /**
     * json string转对象
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> convertJsonToListObject(String json) {
        List<Map<String, Object>> resList = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .serializeNulls()            //展示为空字段
                .setDateFormat(DateUtil.DATETIME_LONG).create();    //格式化时间
        resList = gson.fromJson(json, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        return resList;
    }

    /**
     * json string转对象
     *
     * @param json
     * @return
     */
    public static List<HashMap<String, String>> convertJsonToList(String json) {
        List<HashMap<String, String>> resList = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .serializeNulls()            //展示为空字段
                .setDateFormat(DateUtil.DATETIME_LONG).create();    //格式化时间
        resList = gson.fromJson(json, new TypeToken<List<HashMap<String, String>>>() {
        }.getType());
        return resList;
    }

    /**
     * 对象转json
     */
    public static String toJson(Object object) {
        Gson gson = new GsonBuilder()
                .serializeNulls()            //展示为空字段
                .setDateFormat(DateUtil.DATETIME_LONG).create();    //格式化时间

        return gson.toJson(object).replaceAll("null", "\"\"");    //替换null值
    }

}
