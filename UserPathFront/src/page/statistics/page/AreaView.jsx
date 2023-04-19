/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo, useState } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import ReactECharts from "echarts-for-react";
import { useTranslation } from "react-i18next";
import { Radio } from "antd";

const AreaView = (props) => {
  const { areaData = [] } = props;
  const { t, i18n } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [value, setValue] = useState("session_num");

  const option = [
    {
      label: "Session",
      value: "session_num",
      data: areaData?.[1] ?? [],
    },
    {
      label: "PV",
      value: "pv_num",
      data: areaData?.[0] ?? [],
    },
    {
      label: t("user"),
      value: "user_num",
      data: areaData?.[2] ?? [],
    },
  ];

  const getOptionKey = key => option?.find(i => i?.value === value)?.[key];

  const areaOptions = useMemo(() => {
    setLoading(true);
    const max = getOptionKey("data")?.sort((a, b) => b?.[value] - a?.[value])?.[0];
    const data = getOptionKey("data")?.map(item => ({
      name: item?.f_province,
      value: item?.[value],
      session_num: areaData?.[1]?.find(i => i?.f_province === item?.f_province)?.session_num,
      pv_num: areaData?.[0]?.find(i => i?.f_province === item?.f_province)
        ?.pv_num,
      user_num: areaData?.[2]?.find(i => i?.f_province === item?.f_province)
        ?.user_num,
      avg_pv_num: areaData?.[3]?.find(i => i?.f_province === item?.f_province)
        ?.avg_pv_num,
      avg_session_num: areaData?.[4]?.find(i => i?.f_province === item?.f_province)?.avg_session_num,
    }));
    if (areaData.length && data?.length) {
      setLoading(false);
    }
    return {
      tooltip: {
        trigger: "item",
        formatter: ({ data }) => (data
          ? `<div>
							<div>${data?.name}</div>
              <div>${t("total_users")}：${
            data?.user_num?.toLocaleString() ?? 0
          }</div>
              <div>${t("total_sessions")}：${
            data?.session_num?.toLocaleString() ?? 0
          }</div>
              <div>${t("total_pv")}：${
            data?.pv_num?.toLocaleString() ?? 0
          }</div>
              <div>${t("avg_sessions")}：${
            data?.avg_session_num?.toFixed(2)?.toLocaleString() ?? 0
          }</div>
              <div>${t("avg_pv")}：${
            data?.avg_pv_num?.toFixed(2)?.toLocaleString() ?? 0
          }</div>
            </div>`
          : null),
      },
      series: [
        {
          name: "数据",
          type: "map",
          mapType: "china",
          zoom: 1.2,
          roam: true,
          label: {
            normal: {
              show: false, // 省份名称
            },
            emphasis: {
              show: false,
              color: "#fff",
            },
          },
          data, // 数据
          itemStyle: {
            emphasis: {
              borderWidth: 0.5,
              borderColor: "#59b3b4", // 悬浮边框颜色
              areaColor: "#cfebeb", // 悬浮颜色
            },
          },
          // nameProperty: i18n?.language === "cn" ? "name" : "ename",
        },
      ],
      visualMap: {
        type: "continuous",
        left: 40,
        bottom: "center",
        min: 0,
        max: max?.[value] ?? 5000,
        inRange: {
          color: [
            "#fff",
            "#c3e8ff",
            "#7ec4ff",
            "#5daeff",
            "#4197ff",
            "#6495ED",
            "#275cb9",
          ],
        },
        text: ["高", "低"],
        textStyle: {
          color: "#000", // visualMap 文字的颜色。
        },
      },
    };
  }, [areaData, value, i18n.language]);

  return (
    <EchartCard
      title={t("province_distribution")}
      rigthComp={
        <Radio.Group
          value={value}
          options={option}
          onChange={e => setValue(e?.target?.value)}
          style={{ fontWeight: 500 }}
        />
      }
    >
      <ReactECharts
        option={areaOptions}
        style={{ height: "9.05rem" }}
        showLoading={loading}
      />
    </EchartCard>
  );
};

export default AreaView;
