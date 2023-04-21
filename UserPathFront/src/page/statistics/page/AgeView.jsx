/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import { AgeSession } from "../component";
import style from "@/style/comm.less";
import { sortAge } from "@/utils";
import { useTranslation } from "react-i18next";
import ReactECharts from "echarts-for-react";

const color = [
  { key: "0-20", color: "#c3e8ff" },
  { key: "20-30", color: "#7ec4ff" },
  { key: "30-40", color: "#5daeff" },
  { key: "40-50", color: "#4197ff" },
  { key: "50-60", color: "#6495ED" },
  { key: "60+", color: "#275cb9" },
  { key: "未知", color: "#87CEEB" },
];

const AgeView = (props) => {
  const { t } = useTranslation();
  const { ageData = [] } = props;

  const pieOption = useMemo(() => {
    const echartData = ageData?.[2]?.map(item => ({
      name: item?.age,
      value: item?.user_num,
      itemStyle: {
        color: color?.find(i => i?.key === item?.age)?.color,
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
          top: "25%",
          data: echartData,
          percentPrecision: 1,
          label: {
            formatter: "{b},{d}%",
          },
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
  }, [ageData]);

  const roseOption = useMemo(() => {
    const echartData = ageData?.[0]?.map(item => ({
      name: item?.age,
      value: item?.pv_num,
      itemStyle: {
        color: color?.find(i => i?.key === item?.age)?.color,
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
          top: "25%",
          roseType: "radius",
          itemStyle: {
            borderRadius: 5,
          },
          percentPrecision: 1,
          label: {
            formatter: "{b},{d}%",
          },
          emphasis: {
            label: {
              show: true,
            },
          },
          data: echartData,
        },
      ],
    };
  }, [ageData]);

  return (
    <>
      <div className={style.pie}>
        <div className={style.pie_item}>
          <EchartCard title={t("user_distribution")}>
            <ReactECharts
              option={pieOption}
              style={{ width: "100%", height: 480 }}
            />
          </EchartCard>
        </div>
        <div className={style.pie_item}>
          <AgeSession
            data={ageData?.[1] ? sortAge(ageData?.[1]) : []}
            title={t("session_distribution")}
            legendPosition="top"
          />
        </div>
        <div className={style.pie_item}>
          <EchartCard title={t("pv_distribution")}>
            <ReactECharts
              option={roseOption}
              style={{ width: "100%", height: 480 }}
            />
          </EchartCard>
        </div>
      </div>
    </>
  );
};

export default AgeView;
