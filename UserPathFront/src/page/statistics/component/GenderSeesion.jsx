/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo } from "react";
import { EchartCard } from "@/components/compontents";
import ReactECharts from "echarts-for-react";

const GenderSession = (props) => {
  const { data = [], title, xKey = "session_num", height } = props;
  const color = [
    { key: "男", color: "#3264bd" },
    { key: "女", color: "#4b9cff" },
    { key: "Male", color: "#3264bd" },
    { key: "Female", color: "#4b9cff" },
    { key: "未知", color: "#65b2ff" },
  ];

  const option = useMemo(() => {
    const echartData = data?.map(item => ({
      name: item?.f_sex,
      value: item[xKey],
      itemStyle: {
        color: color?.find(i => i?.key === item?.f_sex)?.color,
      },
    }));
    return {
      tooltip: {
        trigger: "item",
      },
      legend: {
        width: "100%",
        left: "center",
        icon: "circle",
      },
      series: [
        {
          type: "pie",
          top: "25%",
          data: echartData,
          percentPrecision: 1,
          label: {
            formatter: "{b},{d}%",
          },
          sort: (a, b) => b.value - a.value,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
        },
      ],
    };
  }, [data, xKey]);

  return (
    <EchartCard title={title}>
      <ReactECharts
        option={option}
        style={{ width: "100%", height: height ? height : 480 }}
      />
    </EchartCard>
  );
};

export default GenderSession;
