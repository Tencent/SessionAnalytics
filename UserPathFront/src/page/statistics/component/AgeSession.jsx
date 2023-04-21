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
import { useTranslation } from "react-i18next";
import ReactECharts from "echarts-for-react";

const AgeSession = (props) => {
  const { data = [], title, xKey = "session_num", height } = props;
  const { t, i18n } = useTranslation();

  const color = [
    { key: "0-20", color: "#c3e8ff" },
    { key: "20-30", color: "#7ec4ff" },
    { key: "30-40", color: "#5daeff" },
    { key: "40-50", color: "#4197ff" },
    { key: "50-60", color: "#6495ED" },
    { key: "60+", color: "#275cb9" },
    { key: "未知", color: "#87CEEB" },
  ];

  const option = useMemo(() => {
    const echartData = data
      ?.map(item => ({
        name: item?.age,
        value: item[xKey],
        itemStyle: {
          color: color?.find(i => i.key === item?.age)?.color,
        },
      }))
      .sort((a, b) => b?.value - a?.value);
    return {
      title: {
        text: t("total"),
        subtext: data
          .reduce((cur, pre) => cur + pre?.[xKey], 0)
          ?.toLocaleString(),
        left: "center",
        top: "55%",
        subtextStyle: {
          fontSize: 20,
          fontFamily: "微软雅黑",
        },
      },
      tooltip: {
        trigger: "item",
      },
      legend: {
        width: "80%",
        left: "center",
        icon: "circle",
        data: color?.map(item => ({
          name: item?.key,
          itemStyle: {
            color: item?.color,
          },
        })),
      },
      series: [
        {
          type: "pie",
          radius: ["40%", "70%"],
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
  }, [data, xKey, i18n.language]);

  return (
    <EchartCard title={title}>
      <ReactECharts
        option={option}
        style={{ width: "100%", height: height ? height : 480 }}
      />
    </EchartCard>
  );
};

export default AgeSession;
