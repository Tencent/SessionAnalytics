# Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.

# Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
# The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
# All Tencent Modifications are Copyright (C) THL A29 Limited.
# SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.

# -*- coding: UTF-8 -*-

import pandas as pd
import pymysql
import sys
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.metrics import silhouette_score

# 计算数据挖掘-kmeans聚类结果
path = pymysql.connect(
    host='',
    port=,
    user='',
    passwd='',
    db='',
    charset='utf8'
)


def main(scene_version_id, make_version_id):

    sql = """select f_user_id,f_category,count(*) as category_count 
            from t_session_log_event where f_user_scene_version_id ='{}' and f_make_version_id ='{}'
            group by f_category,f_user_id""".format(
        scene_version_id, make_version_id)
    df = pd.read_sql(con=path, sql=sql)

    # 对数据进行重塑->行列转换
    df1 = df.pivot(index='f_user_id', columns='f_category',
                   values='category_count')
    df2 = df1.reset_index()
    df2.fillna(1.0, inplace=True)
    df3 = df2.iloc[:, 1:]

    # pca降维
    pca = PCA(n_components='mle')
    session_value_pca = pd.DataFrame(pca.fit_transform(df3)).iloc[:, :2]
    df_user_id = df2['f_user_id']
    cluster_type = 'kmeans'
    cluster_status = 1
    # 求k
    # category最大值，默认为5
    sil_score = {}
    for k in range(2, 6):
        km = KMeans(n_clusters=k, random_state=0).fit(df3)
        sil_score.update({k: silhouette_score(df3, km.labels_)})

    k = max(sil_score, key=lambda x: sil_score[x])

    # 准备写入t_user_cluster_k_data表中的数据
    cluster_k_data = []
    for x in sil_score:
        f_x, f_y = x, sil_score[x]
        center_point = 1 if x == k else 0
        cluster_k_data.append([cluster_type, f_x, f_y, center_point, cluster_status,
                               make_version_id, scene_version_id])
    insert_cluster_k_data_sql = """insert into t_user_log_cluster_k 
                                  (f_cluster_type,f_x,f_y,f_is_turnpoint,f_cluster_status,f_make_version_id,
                                  f_user_scene_version_id)
                                  values (%s,%s,%s,%s,%s,%s,%s)"""
    # 求label
    model_k = KMeans(n_clusters=k).fit(session_value_pca)
    c_label = model_k.predict(session_value_pca)

    # label转df
    df_label = pd.DataFrame(data=c_label, columns=['f_label'])

    # 还需要知道聚类中心的坐标
    muk = model_k.cluster_centers_

    # 将数据合并成新的df
    df_new = pd.concat([df_user_id, session_value_pca, df_label], axis=1,
                       join='outer')
    df_new = df_new.rename(
        columns={df_new.columns[1]: 'f_x', df_new.columns[2]: 'f_y'})

    # data写入mysql
    data = []
    for row in df_new.index:
        data.append(
            [df_new.loc[row]['f_user_id'], 'kmeans', df_new.loc[row]['f_label'],
             round(df_new.loc[row]['f_x'], 6),
             round(df_new.loc[row]['f_y'], 6), '0', '1', make_version_id, scene_version_id])

    # 中心化数据
    label = 0
    for centre in muk:
        data.append(
            ['', cluster_type, label, round(centre[0], 6), round(centre[1], 6), '1',
             cluster_status, make_version_id, scene_version_id])
        label += 1
    insert_sql = """INSERT INTO t_user_log_cluster_analysis 
                   (f_user_id, f_cluster_type, f_label, f_x, f_y, 
                   f_is_center,f_cluster_status ,f_make_version_id,f_user_scene_version_id)
                   VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s) """
    cursor = path.cursor()
    cursor.executemany(insert_sql, data)
    cursor.executemany(insert_cluster_k_data_sql, cluster_k_data)

    # 提交数据
    path.commit()
    cursor.close()
    path.close()
    print('t_user_log_cluster_analysis---写入完成---')


if __name__ == '__main__':
  # user_scene_version_id = sys.argv[1]
    main(sys.argv[1], sys.argv[2])
  # main(user_scene_version_id)
