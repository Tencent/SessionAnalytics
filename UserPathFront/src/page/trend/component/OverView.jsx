/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useState } from "react";
import { EchartCard } from "@/components/compontents";
import { Line, Area } from "@ant-design/plots";
import {
  getSessionLogUserCntTrend,
  getSessionLogSessionCntTrend,
  getSessionLogPvCntTrend,
} from "@/server/trend";

const OverView = ({ params, username, t }) => {
  const [userLine, setUserLine] = useState([]);
  const [sessionLine, SetSessionLine] = useState([]);
  const [pvLine, setPvLine] = useState([]);

  const userLineConfig = {
    xField: "p_date",
    yField: "user_num",
    color: "#abc2fd",
    label: false,
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
    tooltip: {
      showTitle: false,
      formatter: datum => ({
        name: datum.p_date,
        value: `${datum?.user_num?.toLocaleString()}`,
      }),
    },
    smooth: true,
    interactions: [
      {
        type: "marker-active",
      },
    ],
  };

  const sessionLineConfig = {
    xField: "p_date",
    yField: "session_num",
    color: "#5c99e0",
    lineStyle: {
      lineWidth: 6,
    },
    smooth: true,
    point: {
      size: 5,
      shape: "custom-point",
      style: {
        fill: "white",
        stroke: "#5c99e0",
        lineWidth: 4,
      },
      state: {
        active: {
          style: {
            shadowBlur: 8,
            stroke: "#65a8f6",
            fill: "#65a8f6",
            lineWidth: 8,
          },
        },
      },
    },
    tooltip: {
      showTitle: false,
      formatter: datum => ({
        name: datum.p_date,
        value: `${datum?.session_num?.toLocaleString()}`,
      }),
    },
    state: {
      active: {
        style: {
          shadowBlur: 4,
          stroke: "#000",
          fill: "red",
        },
      },
    },

    interactions: [
      {
        type: "marker-active",
      },
    ],
  };

  const pvLineConfig = {
    xField: "p_date",
    yField: "pv_num",
    point: {
      style: {
        fill: "628df6",
        stroke: "#628df6",
        lineWidth: 1,
      },
      size: 1,
      shape: "custom-point",
      state: {
        active: {
          style: {
            stroke: "#628df6",
            fill: "#628df6",
            lineWidth: 3,
          },
        },
      },
    },
    tooltip: {
      showTitle: false,
      formatter: datum => ({
        name: datum.p_date,
        value: `${datum?.pv_num?.toLocaleString()}`,
      }),
    },
    smooth: true,
    state: {
      active: {
        style: {
          shadowBlur: 4,
          stroke: "#000",
          fill: "red",
        },
      },
    },
    interactions: [
      {
        type: "marker-active",
      },
    ],
  };

  useEffect(() => {
    if (params?.f_upload_name) {
      const param = {
        ...params,
        f_upload_user: username,
      };
      Promise.all([
        getSessionLogUserCntTrend(param),
        getSessionLogSessionCntTrend(param),
        getSessionLogPvCntTrend(param),
      ]).then((res) => {
        const [d1, d2, d3] = res;
        if (d1 && d1?.code === 0 && d1?.data && d1?.data?.list) {
          setUserLine(d1?.data?.list ?? []);
        } else {
          setUserLine([]);
        }
        if (d2 && d2?.code === 0 && d2?.data && d2?.data?.list) {
          SetSessionLine(d2?.data?.list ?? []);
        } else {
          SetSessionLine([]);
        }
        if (d3 && d3?.code === 0 && d3?.data && d3?.data?.list) {
          setPvLine(d3?.data?.list ?? []);
        } else {
          setPvLine([]);
        }
      });
    }
  }, [params.f_upload_name]);

  return (
    <>
      <EchartCard title={t("user_daily_trend")}>
        <Line data={userLine} {...userLineConfig} height={487} />
      </EchartCard>
      <EchartCard title={t("session_daily_trend")}>
        <Line data={sessionLine} {...sessionLineConfig} height={487} />
      </EchartCard>
      <EchartCard title={t("pv_daily_trend")}>
        <Area data={pvLine} {...pvLineConfig} height={487} />
      </EchartCard>
    </>
  );
};

export default OverView;
