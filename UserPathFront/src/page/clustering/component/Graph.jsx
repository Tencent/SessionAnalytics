/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useMemo, useState } from "react";
import style from "./index.less";
import ReactECharts from "echarts-for-react";
import { getUserLogSingleNetwork } from "@/server/trend";
import { useStore } from "@royjs/core";
import { useTranslation } from "react-i18next";
import { EchartCard, Table } from "@/components/compontents";
import FilterCard from "./FilterCard";
import { getPagenation } from "@/utils";
import BigNumber from "bignumber.js";
import centrality from "@/images/Centrality.png";

const Graph = () => {
  const { t } = useTranslation();
  const { username, uploadName } = useStore(state => state.user);
  const [chart, setChart] = useState({
    data: [],
    links: [],
  });
  const [top, setTop] = useState(1);
  const [pagination, setPagination] = useState({
    page: 0,
    page_size: 5,
  });

  const [labelOption, setLabelOption] = useState([]);

  const getCraphData = async () => {
    const { code, data } = await getUserLogSingleNetwork({
      f_upload_user: username,
      f_upload_name: uploadName,
    });
    if (code === 0 && data && data?.list && data?.list?.length === 2) {
      setChart({
        data: data?.list?.[1]
          ?.map((i) => {
            const includeE = i?.value?.includes("e");
            return {
              ...i,
              id: i?.id?.toString(),
              value: includeE ? BigNumber(i?.value)?.toFixed() : i?.value,
            };
          })
          .sort((a, b) => b.value - a.value),
        links: data?.list?.[0]?.map(item => ({
          target: data?.list?.[1]
            ?.find(i => item.target === i.name)
            ?.id?.toString(),
          source: data?.list?.[1]
            ?.find(i => item.source === i.name)
            ?.id.toString(),
        })),
      });
      setLabelOption(data?.list?.[1]?.map((_, idx) => ({
        label: (idx + 1).toString(),
        value: idx + 1,
      })));
    } else {
      setChart({
        data: [],
        links: [],
      });
    }
  };

  const onLabelChange = (e) => {
    setTop(e);
  };

  useEffect(() => {
    if (username && uploadName) {
      getCraphData();
    }
  }, [uploadName]);

  const option = useMemo(
    () => ({
      // animationDurationUpdate: 1500, //数据更新动画的时长。
      tooltip: {
        show: true, // 默认显示
        formatter: (params) => {
          const getName = id => chart?.data?.find(i => id === i?.id)?.name;
          return params.dataType === "node"
            ? `${params.name}<br>${t("center_number")}：${params?.value}`
            : `${getName(params?.data?.source)}->${getName(params?.data?.target)}`;
        },
      },
      series: [
        {
          type: "graph",
          layout: "force",
          focusNodeAdjacency: true,
          draggable: true, // 指示节点是否可以拖动
          symbolSize: 20, // 调整节点的大小
          data: chart?.data,
          links: chart?.links,
          roam: true,
          label: {
            show: false,
            position: "right",
          },
          lineStyle: {
            color: "source",
            curveness: 0.3,
          },
          itemStyle: {
            color: params => (top && params?.dataIndex < top ? "red" : "#3b93b4"),
          },
          emphasis: {
            focus: "adjacency",
            lineStyle: {
              width: 10,
            },
          },
          force: {
            // 力引导图基本配置
            layoutAnimation: true,
            friction: 0.6,
            gravity: 0.1, // 节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
            edgeLength: [100, 100], // 边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
            repulsion: 2000, // 节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
          },
        },
      ],
    }),
    [chart, top],
  );

  const columns = [
    {
      title: t("node_name"),
      dataIndex: "name",
      key: "name",
    },
    {
      title: t("center_number"),
      dataIndex: "value",
      key: "value",
      sorter: (a, b) => b.value - a.value,
    },
  ];

  const dataSource = useMemo(
    () => getPagenation(chart?.data, pagination?.page, pagination?.page_size),
    [pagination?.page, chart?.data],
  );

  return (
    <div className={style.graph}>
      <div className={style.graph_left}>
        <FilterCard
          process={[t("step1_graph1"), t("step1_graph2"), t("step1_graph3")]}
          explain={t("Obtain_various")}
          onLabelChange={onLabelChange}
          options={labelOption}
          filterLable={t("node_filtering")}
          height="auto"
          initValue={1}
        />
        <EchartCard
          setHeight={false}
          style={{ marginTop: "0.1rem" }}
          title={t("detailed_data")}
        >
          <Table
            columns={columns}
            dataSource={dataSource}
            sourceLength={chart?.data?.length}
            pagination={pagination}
            onChange={(page) => {
              setPagination(s => ({
                ...s,
                page: page - 1,
              }));
            }}
          />
        </EchartCard>
        <EchartCard
          title={t("algorithm_interpretation")}
          nodestyle={{ marginTop: "0.1rem", height: "260px", flex: "none" }}
        >
          <div className={style.explain}>
            <div>* {t("Eigenvector centrality algorithm")}</div>
            <div className={style.row} style={{ marginTop: "0.05rem" }}>
              {t("its_importance")}
            </div>
            <div className={style.row} style={{ marginTop: "0.05rem" }}>
              {t("assuming_that_xi")}
            </div>
            <div className={style.row}>
              <img
                src={centrality}
                style={{ width: "170px", height: "40px" }}
              />
            </div>
            <div className={style.row}>{t("aij")}</div>
          </div>
        </EchartCard>
      </div>
      <div className={style.graph_right}>
        <EchartCard>
          <ReactECharts
            option={option}
            style={{ height: "100%", width: "100%" }}
          />
        </EchartCard>
        <div className={style.nodes}>
          <div className={style.nodes_item}>
            <div>Nodes</div>
            <div>{chart?.data?.length.toLocaleString() ?? 0}</div>
          </div>
          <div className={style.nodes_item}>
            <div>Edges</div>
            <div>{chart?.links?.length?.toLocaleString() ?? 0}</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Graph;
