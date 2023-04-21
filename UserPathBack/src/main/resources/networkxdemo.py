# Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.

# Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
# The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
# All Tencent Modifications are Copyright (C) THL A29 Limited.
# SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.

# -*- coding: UTF-8 -*-

import pandas as pd
import pymysql
import sys
import pymysql
import numpy as np
import networkx as nx
import matplotlib.pyplot as plt
import pylab

import warnings
import pandas as pd

# 计算数据挖掘-中心性计算结果

fusion = pymysql.connect(
    host='',
    port=,
    user='',
    passwd='',
    db='',
    charset='utf8',
    cursorclass=pymysql.cursors.DictCursor
)


def main(scene_version_id, make_version_id):

    sql = """select f_event_from,f_event_to,f_weight_session from t_session_single where f_user_scene_version_id ='{}' and f_make_version_id ='{}'
            order by id""".format(
        scene_version_id, make_version_id)
    fusion_conn = fusion.cursor()
    fusion_conn.execute(sql)
    df = fusion_conn.fetchall()

    vertices_s1 = np.array([])
    vertices_e1 = np.array([])
    value1 = np.array([])

    for data in df:
        f_event_from = data['f_event_from']
        f_event_to = data['f_event_to']
        f_weight_session = data['f_weight_session']
        vertices_s1 = np.append(vertices_s1, f_event_from)
        vertices_e1 = np.append(vertices_e1, f_event_to)
        value1 = np.append(value1, f_weight_session)

    G1 = nx.DiGraph()

    for i in range(np.size(vertices_s1)):
        G1.add_weighted_edges_from([(vertices_s1[i], vertices_e1[i], value1[i])])

    #特征向量中心度
    eigenvector1 = nx.eigenvector_centrality(G1, max_iter=5000)
    print("输出特征向量中心度的计算值：")
    for item in eigenvector1:
        print(item, "\t", eigenvector1[item])
        fusion_conn.execute(
            "insert into t_session_single_networkx(f_event_name,f_event_value,f_make_version_id,f_user_scene_version_id)values"
            "('{}','{}','{}','{}')".format(item, eigenvector1[item], make_version_id, scene_version_id))
    fusion.commit()

    print('t_session_single_networkx---写入完成---')


if __name__ == '__main__':
    main(sys.argv[1], sys.argv[2])
    # main(11, 0)
