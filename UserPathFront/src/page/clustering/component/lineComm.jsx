/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useState } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import { getUserLogClusterK } from "@/server/trend";
import { depend } from "@/utils/datamaps";
import { Line } from "@ant-design/plots";
import coefficient_png from "@/images/coefficient.png";
import style from "./index.less";

const LineComm = (props) => {
  const { t, params, username, type, children } = props;
  const [data, setData] = useState([]);
  const config = {
    padding: "auto",
    xField: "f_x",
    yField: "f_y",
    xAxis: {
      // type: 'timeCat',
      tickCount: 5,
    },
    point: {
      size: 3,
      shape: "custom-point",
      style: {
        stroke: "#6893fc",
        fill: "#6893fc",
        lineWidth: 2,
      },
      state: {
        active: {
          style: {
            shadowBlur: 20,
            stroke: "white",
            fill: "#6893fc",
            lineWidth: 1,
          },
        },
      },
    },
    smooth: true,
    tooltip: {
      showTitle: false,
      formatter: datum => ({
        name: `${t("label")}${datum.f_x}`,
        value: `${datum?.f_y?.toLocaleString()}`,
      }),
    },
  };

  const getData = async () => {
    const { code, data } = await getUserLogClusterK({
      ...params,
      f_upload_user: username,
      f_cluster_type: type,
    });
    if (code === 0 && data && data.list) {
      setData(data.list.map(item => ({
        ...item,
        f_y: Math.round(item.f_y * 10000) / 10000,
      })));
    } else {
      setData([]);
    }
  };

  useEffect(() => {
    if (username && params.f_upload_name) {
      getData();
    }
  }, [...depend(params), type]);

  return (
    <EchartCard title={t("algorithm_interpretation")}>
      <div className={style.lineComm}>
        <div className={style.explain}>
          {children}
          <div style={{ marginTop: "0.3rem" }}>
            * {t("silhouette_coefficient")}
          </div>
          <div className={style.row}>
            <img src={coefficient_png} />
          </div>
          <div className={style.row}>
            <span>{t("param_a")}：</span>
            <p>{t("average_distance")}</p>
          </div>
          <div className={style.row}>
            <span>{t("param_b")}：</span>
            <p>{t("average_next")}</p>
          </div>
          <div className={style.row} style={{ marginTop: "0.15rem" }}>
            {t("value_range")}
          </div>
        </div>
        <div>
          <div
            className={style.explain}
            style={{ marginTop: "-0.25rem", marginBottom: "0.25rem" }}
          >
            * {t("Profile_Factor")}
          </div>
          <Line {...config} data={data} height={360} />
        </div>
      </div>
    </EchartCard>
  );
};

export default LineComm;
