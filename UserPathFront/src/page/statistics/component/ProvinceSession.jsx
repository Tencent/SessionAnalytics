/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import { Column } from "@ant-design/plots";

const ProvinceSession = (props) => {
  const {
    title,
    xKey = "session_num",
    data = [],
    height,
    lableFontSize = 14,
    labelPosition = "middle",
  } = props;
  const columnConfig = useMemo(
    () => ({
      data,
      xField: "f_province",
      yField: xKey,
      color: "#6ca3e2",
      xAxis: {
        label: {
          autoHide: true,
          autoRotate: false,
        },
      },
      label: {
        position: labelPosition,
        style: {
          // fill: "#FFFFFF",
          // opacity: 0.6,
          fontSize: lableFontSize,
        },
        formatter: k => k?.[xKey]?.toLocaleString(),
      },
      tooltip: {
        showTitle: false,
        formatter: d => ({
          name: d?.f_province,
          value: d?.[xKey]?.toLocaleString(),
        }),
      },
      slider: {
        start: 0,
        end: data?.length > 10 ? 10 / data?.length : 1,
      },
    }),
    [data, xKey],
  );

  return (
    <EchartCard title={title}>
      <Column
        {...columnConfig}
        style={{ width: "100%", height: height ? `${height / 100}rem` : 480 }}
      />
    </EchartCard>
  );
};

export default ProvinceSession;
