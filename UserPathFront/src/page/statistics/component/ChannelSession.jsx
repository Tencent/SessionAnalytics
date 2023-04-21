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
import { Bar } from "@ant-design/plots";

const ChannelSession = (props) => {
  const {
    data = [],
    title,
    xKey = "session_num",
    height,
    textLength = 20,
    lableFontSize = 14,
    labelPosition = "middle",
  } = props;
  const config = useMemo(
    () => ({
      data,
      xField: xKey,
      yField: "f_channel",
      color: "#6ca3e2",
      xAxis: {
        label: {
          autoHide: true,
          autoRotate: false,
        },
      },
      yAxis: {
        label: {
          formatter: text => (text?.length > textLength
            ? `${text.substring(0, textLength)}...`
            : text),
        },
      },
      tooltip: {
        showTitle: false,
        formatter: datum => ({
          name: datum.f_channel,
          value: `${datum?.[xKey]?.toLocaleString()}`,
        }),
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
      slider: {
        start: 0,
        end: data?.length > 10 ? 10 / data?.length : 1,
      },
    }),
    [data, xKey, textLength],
  );

  return (
    <EchartCard title={title}>
      <Bar
        {...config}
        style={{ width: "100%", height: height ? `${height / 100}rem` : 480 }}
      />
    </EchartCard>
  );
};

export default ChannelSession;
