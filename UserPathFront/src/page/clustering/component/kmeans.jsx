/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect, useMemo } from "react";
import style from "./index.less";
import { Scatter, Pie } from "@ant-design/plots";
import { Table, EchartCard } from "@/components/compontents";
import LineComm from "./lineComm";
import {
  getUserLogClusterAnalysis,
  getUserLogClusterDistributeClusterAnalysis,
  getUserLogClusterDetail,
  getUserLogClusterDetailCategory,
} from "@/server/trend";
import { depend } from "@/utils/datamaps";
import FilterCard from "./FilterCard";

const Kmeans = (porps) => {
  const { t, params, username, i18n } = porps;
  const [scatterData, setScatterData] = useState([]);
  const [pieData, setPieData] = useState([]);
  const [tableData, setTableData] = useState([]);
  const [decodData, setDecodData] = useState([]);
  const [decodLoading, setDecodLoading] = useState(false);
  const [statisticsLoading, setStatisticsLoading] = useState(false);
  const [labelOption, setLabelOption] = useState([]);
  const [clumLength, setClumLength] = useState(null);

  // 聚类图
  const getScatterAnalysis = async () => {
    const { code, data } = await getUserLogClusterAnalysis({
      ...params,
      f_upload_user: username,
      f_cluster_type: "kmeans",
    });
    if (code === 0 && data && data.list) {
      const sortList = data.list?.sort((a, b) => a.f_label - b.f_label) ?? [];
      setScatterData(sortList?.map(item => ({
        ...item,
        f_x: Number(item.f_x),
        f_y: Number(item.f_y),
      })));
      setLabelOption([...new Set(sortList?.map(i => i?.f_label))]?.map(i => ({
        label: i,
        value: i,
      })));
    } else {
      setScatterData([]);
    }
  };

  // 分布比例
  const getPieAnalysis = async () => {
    const { code, data } = await getUserLogClusterDistributeClusterAnalysis({
      ...params,
      f_upload_user: username,
      f_cluster_type: "kmeans",
    });
    if (code === 0 && data && data.list) {
      setPieData(data.list);
    } else {
      setPieData([]);
    }
  };

  const getClusterDetail = async (label) => {
    setDecodLoading(true);
    const { code, data } = await getUserLogClusterDetailCategory({
      ...params,
      f_upload_user: username,
      f_cluster_type: "kmeans",
      f_label: label,
    });
    if (code === 0 && data && data.list) {
      setDecodData(data.list.map(i => ({
        ...i,
        f_label: label,
      })));
    } else {
      setDecodData([]);
    }
    setDecodLoading(false);
  };

  const categoryData = useMemo(
    () => decodData?.reduce((cur, pre) => {
      const idx = cur?.findIndex(i => i?.f_user_id === pre?.f_user_id);
      if (idx !== -1) {
        cur[idx] = {
          ...cur[idx],
          [pre.f_category]: pre.pv,
        };
        return cur;
      }
      return [
        ...cur,
        {
          [pre.f_category]: pre.pv,
          f_user_id: pre?.f_user_id,
          f_label: pre?.f_label,
        },
      ];
    }, []),
    [decodData],
  );

  // 分布统计
  const getTableData = async (label) => {
    setStatisticsLoading(true);
    const { code, data } = await getUserLogClusterDetail({
      ...params,
      f_upload_user: username,
      f_label: label,
    });
    if (code === 0 && data && data.list) {
      setTableData(data.list?.map(item => ({
        ...item,
        f_x: Number(item.f_x),
        f_y: Number(item.f_y),
      })));
    } else {
      setTableData([]);
    }
    setStatisticsLoading(false);
  };

  useEffect(() => {
    if (username && params.f_upload_name) {
      getScatterAnalysis();
      getPieAnalysis();
      getTableData("0");
      getClusterDetail("0");
    }
  }, [...depend(params)]);

  const scatterConfig = useMemo(() => {
    const data = scatterData?.map(item => ({
      [t("x")]: item?.f_x,
      [t("y")]: item?.f_y,
      [t("label")]: item?.f_label,
      [t("central_point")]: item?.f_is_center,
    }));
    return {
      data,
      appendPadding: 30,
      xField: t("x"),
      yField: t("y"),
      colorField: t("label"),
      sizeField: t("central_point"),
      size: [4, 8],
      shape: "circle",
      pointStyle: {
        fillOpacity: 1,
      },
      yAxis: {
        nice: true,
        line: {
          style: {
            stroke: "#aaa",
          },
        },
      },
      xAxis: {
        grid: {
          line: {
            style: {
              stroke: "#eee",
            },
          },
        },
        line: {
          style: {
            stroke: "#aaa",
          },
        },
      },
      label: {
        formatter: k => (k?.[t("central_point")] === 1
          ? `${t("central_point")} ${k?.[t("label")]}`
          : ""),
      },
      legend: {
        position: "bottom",
        itemName: {
          formatter: value => `${t("label")}：${value.toLocaleString()}`,
        },
      },
      interactions: [
        {
          type: "element-highlight-by-color",
        },
        {
          type: "element-range-highlight",
        },
      ],
    };
  }, [scatterData, i18n.language]);

  const pieConfig = useMemo(
    () => ({
      data: pieData,
      appendPadding: 10,
      angleField: "user_num",
      colorField: "f_label",
      radius: 0.8,
      label: {
        type: "outer",
        formatter: k => `${(k.percent * 100).toFixed(2)}%`,
      },
      legend: {
        position: "left",
        itemName: {
          formatter: value => `${t("label")}：${value}`,
        },
      },
      interactions: [
        {
          type: "element-highlight-by-color",
        },
        {
          type: "element-link",
        },
      ],
      tooltip: {
        showTitle: false,
        formatter: datum => ({
          name: `${t("label")}${datum.f_label}`,
          value: `${datum?.user_num?.toLocaleString()}`,
        }),
      },
    }),
    [pieData, i18n.language],
  );

  const columns = [
    {
      title: t("label"),
      dataIndex: "f_label",
    },
    {
      title: t("number_of_users"),
      dataIndex: "user_num",
      sorter: (a, b) => a.number_of_users - b.number_of_users,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("maximum_PV"),
      dataIndex: "max_pv",
      sorter: (a, b) => a.max_pv - b.max_pv,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("minimum_PV"),
      dataIndex: "min_pv",
      sorter: (a, b) => a.min_pv - b.min_pv,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("average PV"),
      dataIndex: "avg_pv",
      sorter: (a, b) => a.avg_pv - b.avg_pv,
      render: m => m?.toFixed(2)?.toLocaleString(),
    },
  ];

  const decodColumns = useMemo(() => {
    const column = [...new Set(decodData?.map(i => i.f_category))].map(i => ({
      width: 250,
      title: `${i}PV数`,
      dataIndex: i,
      ellipsis: true,
      render: m => (m ? m.toLocaleString() : 0),
    }));
    setClumLength(column?.length * 250 + 100);
    return [
      {
        title: t("label"),
        dataIndex: "f_label",
        width: 100,
      },
    ].concat(column);
  }, [decodData, i18n.language]);

  const onLabelChange = (label) => {
    getTableData(label);
    getClusterDetail(label);
  };

  return (
    <>
      <div className={style.clum}>
        <div className={style.clum_left}>
          <FilterCard
            process={[t("step1"), t("step2"), t("step3"), t("step4")]}
            explain={t("get_statistical")}
            onLabelChange={onLabelChange}
            options={labelOption}
          />
        </div>
        <div className={style.clum_center}>
          <EchartCard title={t("clustergram")}>
            <Scatter {...scatterConfig} height={320} />
          </EchartCard>
        </div>
        <div className={style.clum_right}>
          <EchartCard title={t("Distribution_Proportion")}>
            <Pie {...pieConfig} height={320} />
          </EchartCard>
        </div>
      </div>
      <div className={style.table}>
        <EchartCard title={t("Distribution_Statistics")}>
          <Table
            columns={columns}
            dataSource={tableData}
            pagination={false}
            loading={statisticsLoading}
          />
        </EchartCard>
        <EchartCard title={t("data_decoding")} height="auto">
          <Table
            scroll={{ x: clumLength }}
            columns={decodColumns}
            dataSource={categoryData}
            loading={decodLoading}
            pagination={false}
          />
        </EchartCard>
      </div>
      <LineComm {...porps} type="kmeans">
        <div>* {t("pca")}</div>
        <div className={style.row}>{t("extract_principal")}</div>
        <div className={style.row} style={{ marginTop: "0.2rem" }}>
          <span>{t("stp1")}：</span>
          <p>{t("input_matrix")}</p>
        </div>
        <div className={style.row}>
          <span>{t("stp2")}：</span>
          <p>{t("centralize")}</p>
        </div>
        <div className={style.row}>
          <span>{t("stp3")}：</span>
          <p>{t("decompose_x")}</p>
        </div>
        <div className={style.row}>
          <span>{t("stp4")}：</span>
          <p>{t("new_matrix")}</p>
        </div>
      </LineComm>
    </>
  );
};

export default Kmeans;
