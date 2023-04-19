/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import { Gauge, G2 } from "@ant-design/plots";
import { GenderSession } from "../component";
import style from "@/style/comm.less";
import { useTranslation } from "react-i18next";
import ReactECharts from "echarts-for-react";

const SexView = (props) => {
  const { genderData = [] } = props;
  const { registerShape, Util } = G2; // 自定义 Shape 部分
  const { t } = useTranslation();

  const color = [
    { key: "男", color: "#3264bd" },
    { key: "女", color: "#4b9cff" },
    { key: "未知", color: "#65b2ff" },
  ];

  registerShape("point", "custom-gauge-indicator", {
    draw(cfg, container) {
      // 使用 customInfo 传递参数
      const { indicator, defaultColor } = cfg.customInfo;
      const { pointer } = indicator;
      const group = container.addGroup(); // 获取极坐标系下画布中心点

      const center = this.parsePoint({
        x: 0,
        y: 0,
      }); // 绘制指针

      if (pointer) {
        const { startAngle, endAngle } = Util.getAngle(cfg, this.coordinate);
        const radius = this.coordinate.getRadius();
        const midAngle = (startAngle + endAngle) / 2;
        const { x: x1, y: y1 } = Util.polarToCartesian(
          center.x,
          center.y,
          radius / 15,
          midAngle + 1 / Math.PI,
        );
        const { x: x2, y: y2 } = Util.polarToCartesian(
          center.x,
          center.y,
          radius / 15,
          midAngle - 1 / Math.PI,
        );
        const { x, y } = Util.polarToCartesian(
          center.x,
          center.y,
          radius * 0.65,
          midAngle,
        );
        const path = [
          ["M", center.x],
          ["L", x1, y1],
          ["L", x, y],
          ["L", x2, y2],
          ["Z"],
        ]; // pointer

        group.addShape("path", {
          name: "pointer",
          attrs: {
            path,
            fill: defaultColor,
            ...pointer.style,
          },
        });
      }

      return group;
    },
  });

  const getConfig = content => ({
    range: {
      color: "#3b91ff",
    },
    axis: {
      tickLine: null,
      label: null,
      subTickLine: {
        count: 3,
      },
    },
    indicator: {
      shape: "custom-gauge-indicator",
      pointer: {
        style: {
          stroke: "#D0D0D0",
          lineWidth: 1,
          fill: "#D0D0D0",
        },
      },
      pin: {
        style: {
          stroke: "#D0D0D0",
        },
      },
    },
    statistic: {
      title: {
        content: t(content),
        offsetY: -37,
        style: {
          fontSize: "0.15rem",
          lineHeight: 2,
          color: "#afafaf",
        },
      },
      content: {
        style: {
          fontSize: "0.24rem",
          color: "#000000",
          lineHeight: "0.35rem",
        },
        formatter: v => `${(v?.percent * 100).toFixed(2)}%`,
      },
    },
  });

  const roseOption = useMemo(() => {
    const echartData = genderData?.[0]?.map(item => ({
      name: item?.f_sex,
      value: item?.pv_num,
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
  }, [genderData]);

  const getPercent = (data, key) => {
    const percentItem = data.find(item => item?.f_sex === key);
    const sum = data.reduce((cur, pre) => cur + pre?.user_num, 0) ?? 1;

    return percentItem
      ? Math.round((percentItem?.user_num / sum) * 10000) / 10000
      : 0;
  };

  return (
    <>
      <EchartCard title={t("user_distribution")}>
        <div className={style.gaugeview}>
          <div className={style.gaugeview_item}>
            <Gauge
              {...getConfig("male")}
              percent={getPercent(genderData?.[2] ?? [], "男")}
            />
          </div>
          <div className={style.gaugeview_item}>
            <Gauge
              {...getConfig("female")}
              percent={getPercent(genderData?.[2] ?? [], "女")}
            />
          </div>
          <div className={style.gaugeview_item}>
            <Gauge
              {...getConfig("unKnow")}
              percent={getPercent(genderData?.[2] ?? [], "未知")}
            />
          </div>
        </div>
      </EchartCard>
      <div className={style.pieview}>
        <div className={style.pieview_item}>
          <GenderSession
            data={genderData?.[1] ?? []}
            title={t("session_distribution")}
          />
        </div>
        <div className={style.pieview_item}>
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

export default SexView;
