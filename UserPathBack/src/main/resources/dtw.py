# Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.

# Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
# The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
# All Tencent Modifications are Copyright (C) THL A29 Limited.
# SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.

import sys
import datetime
import pandas as pd
import pymysql
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from tslearn.clustering import TimeSeriesKMeans
from sklearn.metrics import silhouette_score

# 计算数据挖掘-dtw时序聚类结果

userpath_conn = pymysql.connect(
    host='',
    port=,
    user='',
    passwd='',
    db='',
    charset='utf8'
)


def func(user_scene_version_id, make_version_id):

    sql = """select DATE_FORMAT(f_client_time, '%Y%m%d') as day, f_user_id, count(distinct f_session_id) from
          t_session_log_event where f_user_scene_version_id = '{}' and f_make_version_id = '{}'
          group by f_user_id, day""".format(
        user_scene_version_id, make_version_id)
    df_hco = pd.read_sql(con=userpath_conn, sql=sql)
    data = list(df_hco.values)
    date = set()
    data_dic = dict()
    for i in data:
        if i[0]:
            date.add(i[0])
            if data_dic.get(i[1]):
                data_dic.get(i[1])[i[0]] = i[2]
            else:
                data_dic.setdefault(i[1], {}).setdefault(i[0], i[2])
    end = max(date)
    start = min(date)
    num = start
    day_lst = [start]

    while num < end:
        t = datetime.datetime.strptime(num, "%Y%m%d") + datetime.timedelta(days=1)
        next_day = t.strftime("%Y%m%d")
        day_lst.append(next_day)
        num = next_day

    result = list()
    for k, v in data_dic.items():
        lst = []
        lst.append(k)
        for i in day_lst:
            if v.get(i):
                lst.append(v.get(i))
            else:
                lst.append(0)
        result.append(lst)

    df = pd.DataFrame(result)
    df.columns = ['user_id'] + day_lst
    df2 = df.dropna(axis=0, how='any')
    user_id = [i[0] for i in df2.values]
    x_train = df2.drop(['user_id'], axis=1).to_numpy()

    sil_score = {}
    for k in range(2, 6):
        km = KMeans(n_clusters=k, random_state=0).fit(x_train)
        sil_score.update({k: silhouette_score(x_train, km.labels_)})
    k = max(sil_score, key=lambda x: sil_score[x])

    cluster_type, cluster_status = 'dtw', 1

    cluster_k_data = []
    for x in sil_score:
        f_x, f_y = x, sil_score[x]
        center_point = 1 if x == k else 0
        cluster_k_data.append([cluster_type, f_x, f_y, center_point, cluster_status, make_version_id, user_scene_version_id])
    insert_cluster_k_data_sql = """insert into t_user_log_cluster_k (f_cluster_type,f_x,f_y,f_is_turnpoint,
                                    f_cluster_status,f_make_version_id,
                                    f_user_scene_version_id)values (%s,%s,%s,%s,%s,%s,%s)"""

    sdtw_km = TimeSeriesKMeans(n_clusters=k, metric="softdtw", metric_params={"gamma": .01}, verbose=True,
                               random_state=0)
    y_pred_middel5 = sdtw_km.fit_predict(x_train)
    type_lst = list(y_pred_middel5)
    center = sdtw_km.cluster_centers_
    res_dict = {}
    for i in range(k):
        lst = []
        for j in center[i]:
            lst.append(j[0])
        res_dict[i] = lst
    plt.figure()
    res = []
    num = 0
    # for yi in range(k):
    #     plt.figure(yi)
    #     for xx in x_train[y_pred_middel5 == yi][: 100, : ]:
    #         res.append((list(xx), type_lst[num]))
    #         num += 1
    for xx in x_train:
        res.append((list(xx), type_lst[num]))
        num += 1

    # 整理label数据
    insert_data = []
    i_num = 0
    for i in res:
        j_num = 0
        for j in i[0]:
            insert_data.append([user_id[i_num], cluster_type, i[1], day_lst[j_num], j, 0, make_version_id, user_scene_version_id])
            j_num += 1
        i_num += 1


    # 整理中心点
    for k, v in res_dict.items():
        num = 0
        for i in v:
            insert_data.append(["", cluster_type, k, day_lst[num], i, 1, make_version_id, user_scene_version_id])
            num += 1
    cursor = userpath_conn.cursor()
    sql = """INSERT INTO t_user_log_cluster_analysis (f_user_id, f_cluster_type,
          f_label, f_x, f_y, f_is_center, f_make_version_id, 
          f_user_scene_version_id) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"""

    cursor.executemany(sql, insert_data)

    cursor.execute(
        "update t_user_log_cluster_analysis set f_cluster_status=1 "
        " where f_user_scene_version_id='{}' and f_make_version_id='{}' ".format(
        user_scene_version_id, make_version_id))
    cursor.executemany(insert_cluster_k_data_sql, cluster_k_data)

    # 提交数据
    userpath_conn.commit()
    # 关闭光标对象
    cursor.close()
    # 关闭数据库连接
    userpath_conn.close()
    print('---dtw写入完成---')


if __name__ == "__main__":
    # user_scene_version_id = sys.argv[1]
    # func("demo79b4ab5542557618eba616db38dee2c41")
    func(sys.argv[1], sys.argv[2])
    # print(sys.argv[1])
