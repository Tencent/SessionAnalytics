/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo, useState } from "react";
import { Pie } from "@ant-design/plots";
import EchartCard from "@/components/compontents/EchartCard";
import { ChannelSession } from "../component";
import { useTranslation } from "react-i18next";
import style from "@/style/comm.less";
import { Radio } from "antd";

const ChannelView = (props) => {
  const { t, i18n } = useTranslation();
  const { channelData = [] } = props;
  const [value, setValue] = useState(3);

  const data = useMemo(
    () => channelData?.[2]?.filter((_, idx) => idx < value) ?? [],
    [channelData, value],
  );

  const config = useMemo(
    () => ({
      data,
      height: 500,
      appendPadding: 10,
      angleField: "user_num",
      colorField: "f_channel",
      radius: 1,
      innerRadius: 0.6,
      label: {
        type: "outer",
        offset: 15,
        content: params => `${params?.f_channel},${(params?.percent * 100)?.toFixed(2)}%`,
        style: {
          fontSize: value < 10 ? 14 : 12,
        },
      },
      legend: {
        position: "left",
        height: "5rem",
      },
      interactions: [
        {
          type: "element-selected",
        },
        {
          type: "element-active",
        },
      ],
      tooltip: {
        showTitle: false,
        formatter: datum => ({
          name: datum.f_channel,
          value: `${datum?.user_num?.toLocaleString()}`,
        }),
      },
      statistic: {
        title: {
          content: t("total"),
          style: {
            fontSize: "0.2rem",
            color: "#787878",
          },
        },
        content: {
          style: {
            whiteSpace: "pre-wrap",
            overflow: "hidden",
            textOverflow: "ellipsis",
            fontSize: "0.25rem",
            marginTop: "0.05rem",
            color: "#5e5e5e",
          },
          customHtml: () => data
            ?.reduce((cur, pre) => cur + pre?.user_num, 0)
            ?.toLocaleString(),
        },
      },
    }),
    [data, i18n.language, value],
  );

  return (
    <>
      <EchartCard
        title={t("user_distribution")}
        subTitle={t("default_top5")}
        rigthComp={
          <Radio.Group
            value={value}
            onChange={e => setValue(e?.target?.value)}
            style={{ fontWeight: 500 }}
          >
            <Radio value={3}>Top3</Radio>
            <Radio value={5}>Top5</Radio>
            <Radio value={10}>Top10</Radio>
            <Radio value={20}>Top20</Radio>
            <Radio value={channelData?.[2]?.length}>{t("all")}</Radio>
          </Radio.Group>
        }
      >
        <Pie {...config} data={data} />
      </EchartCard>
      <div className={style.pieview}>
        <div className={style.pieview_item}>
          <ChannelSession
            data={channelData?.[1] ?? []}
            title={t("session_distribution")}
          />
        </div>
        <div className={style.pieview_item}>
          <ChannelSession
            data={channelData?.[0] ?? []}
            title={t("pv_distribution")}
            xKey="pv_num"
          />
        </div>
      </div>
    </>
  );
};

export default ChannelView;
