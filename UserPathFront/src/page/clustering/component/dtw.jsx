/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect, useMemo } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import { Line, Pie } from "@ant-design/plots";
import Table from "@/components/compontents/Table";
import { Pagination } from "antd";
import LineComm from "./lineComm";
import {
  getUserLogClusterDistributeClusterAnalysis,
  getUserLogClusterDetailSample,
  getUserLogClusterAnalysis,
} from "@/server/trend";
import { depend } from "@/utils/datamaps";
import FilterCard from "./FilterCard";
import { useTranslation } from "react-i18next";
import { getPagenation } from "@/utils";
import { v4 as uuidv4 } from "uuid";
import style from "./index.less";

const LineTooltip = ({ data = [], label }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState(0);
  const tableData = useMemo(() => getPagenation(data, page), [page, data]);

  useEffect(() => {
    setPage(0);
  }, [data]);

  return (
    <div className={style.tooltipDom}>
      <div className={style.contentDom}>
        {tableData?.map(item => (
          <div className={style.divStyle} key={uuidv4()}>
            <div style={{ backgroundColor: item.color }} />
            <div>
              {item?.data?.f_user_id === t("center")
                ? `${t("label")}${label}${item?.data?.f_user_id}`
                : item?.data?.f_user_id}
              ：
            </div>
            <div>{item?.data?.f_y}</div>
          </div>
        ))}
      </div>
      <Pagination
        showSizeChanger={false}
        defaultCurrent={1}
        current={page + 1}
        pageSize={10}
        total={data?.length}
        onChange={(page) => {
          setPage(page - 1);
        }}
      />
    </div>
  );
};

const Dtw = (props) => {
  const { params, username } = props;
  const { t, i18n } = useTranslation();
  const [pieData, setPieData] = useState([]);
  const [tableData, setTableData] = useState([]);
  const [seleced, setSelected] = useState("");
  const [scatterData, setScatterData] = useState([]);
  const [centerData, setCenterData] = useState([]);
  const [labelData, setLabelData] = useState({
    data: [],
    color: "",
  });
  const [label, setLabel] = useState("0");
  const [loading, setLoading] = useState(false);
  const [labelOption, setLabelOption] = useState([]);

  const labelColor = {
    0: "#FFC0CB",
    1: "#5AD8A6",
    2: "#6495ED",
    3: "#F6BD16",
    4: "#6F5EF9",
  };

  const getPieAnalysis = async () => {
    const { code, data } = await getUserLogClusterDistributeClusterAnalysis({
      ...params,
      f_upload_user: username,
      f_cluster_type: "dtw",
    });
    if (code === 0 && data && data.list) {
      setPieData(data.list);
      const sortLabel = data?.list?.sort((a, b) => a?.f_label - b?.f_label);
      if (sortLabel?.length) {
        setSelected(sortLabel?.[0]?.f_label);
        getSample(sortLabel?.[0]?.f_label);
        setLabelOption([...new Set(sortLabel?.map(i => i?.f_label))]?.map(i => ({
          label: i,
          value: i,
        })));
      } else {
        setTableData([]);
      }
    } else {
      setPieData([]);
      setTableData([]);
    }
  };
  const getScatterAnalysis = async () => {
    const { code, data } = await getUserLogClusterAnalysis({
      ...params,
      f_upload_user: username,
      f_cluster_type: "dtw",
    });
    if (code === 0 && data && data.list) {
      const list = data.list?.map(i => ({
        ...i,
        f_y: Math.ceil(i.f_y * 10000) / 10000,
        f_user_id: i.f_is_center === 1 ? t("center") : i.f_user_id,
      }));
      setScatterData(list);
      setCenterData(list.filter(item => item.f_is_center === 1));
      setLabelData({
        data: list
          ?.filter(i => i.f_label === "0")
          .sort((b, a) => b?.f_is_center - a?.f_is_center),
        color: labelColor?.["0"],
      });
    } else {
      setScatterData([]);
      setCenterData([]);
      setLabelData({
        data: [],
        color: "",
      });
    }
  };

  const getSample = async (label) => {
    setLoading(true);
    const { code, data } = await getUserLogClusterDetailSample({
      ...params,
      f_upload_user: username,
      f_cluster_type: "dtw",
      f_label: label,
    });
    if (code === 0 && data && data.list) {
      setTableData(data?.list
        ?.filter((_, idx) => idx < 10)
        .map(i => ({ ...i, f_label: label })));
    } else {
      setTableData([]);
    }
    setLoading(false);
  };

  useEffect(() => {
    if (username && params.f_upload_name) {
      getPieAnalysis();
      getScatterAnalysis();
    }
  }, [...depend(params)]);

  const config = useMemo(
    () => ({
      data: labelData?.data ?? [],
      xField: "f_x",
      yField: "f_y",
      seriesField: "f_user_id",
      color: ({ f_user_id }) => {
        if (f_user_id === t("center")) {
          return labelData?.color;
        }
        return "#858483";
      },
      legend: false,
      tooltip: {
        showTitle: false,
        enterable: true,
        customContent: (_, data) => {
          const sortData = data?.sort((a, b) => b?.data?.f_is_center - a?.data?.f_is_center);
          return <LineTooltip data={sortData} label={label} />;
        },
      },
    }),
    [labelData, i18n.language, label],
  );

  const centerConfig = useMemo(
    () => ({
      data: centerData,
      xField: "f_x",
      yField: "f_y",
      seriesField: "f_label",
      color: ({ f_label }) => labelColor?.[f_label],
      tooltip: {
        title: t("session_num"),
        formatter: datum => ({
          name: `${t("label")} ${datum.f_label}`,
          value: `${datum?.f_y?.toLocaleString()}`,
        }),
      },
    }),
    [centerData, i18n.language],
  );

  const pieConfig = useMemo(
    () => ({
      data: pieData,
      appendPadding: 10,
      angleField: "user_num",
      colorField: "f_label",
      color: ({ f_label }) => labelColor?.[f_label],
      radius: 0.8,
      label: {
        type: "outer",
        formatter: k => `${(k.percent * 100).toFixed(2)}%`,
      },
      tooltip: {
        showTitle: true,
        title: t("user_num"),
        formatter: datum => ({
          name: `${t("label")}${datum.f_label}`,
          value: `${datum?.user_num?.toLocaleString()}`,
        }),
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
    }),
    [seleced, i18n.language, pieData],
  );

  const columns = [
    {
      title: t("label"),
      dataIndex: "f_label",
      key: "f_label",
      sorter: (a, b) => a.f_label - b.f_label,
    },
    {
      title: t("user"),
      dataIndex: "f_user_id",
      key: "f_user_id",
      sorter: (a, b) => a.f_user_id.localeCompare(b.f_user_id),
    },
    {
      title: t("f_age"),
      dataIndex: "f_age",
      key: "f_age",
      sorter: (a, b) => a.f_age - b.f_age,
    },
    {
      title: t("f_sex"),
      dataIndex: "f_sex",
      key: "f_sex",
      sorter: (a, b) => a.f_sex.localeCompare(b.f_sex),
    },
    {
      title: t("f_province"),
      dataIndex: "f_province",
      key: "f_province",
      sorter: (a, b) => a.f_province.localeCompare(b.f_province),
    },
    {
      title: t("f_city"),
      dataIndex: "f_city",
      key: "f_city",
      sorter: (a, b) => a.f_city.localeCompare(b.f_city),
    },
    {
      title: t("f_channel"),
      dataIndex: "f_channel",
      key: "f_channel",
      sorter: (a, b) => a.f_channel.localeCompare(b.f_channel),
    },
  ];

  const onLabelChange = (lable) => {
    setLabel(lable);
    setLabelData({
      data:
        scatterData
        	?.filter(i => i?.f_label === lable)
        	.sort((b, a) => b?.f_is_center - a?.f_is_center) ?? [],
      color: labelColor[lable],
    });
    getSample(lable);
  };

  return (
    <>
      <div className={style.clum}>
        <div className={style.clum_left}>
          <FilterCard
            process={[t("step1_dtw"), t("step2_dtw"), t("step3_dtw")]}
            explain={t("Obtain_various")}
            onLabelChange={onLabelChange}
            options={labelOption}
          />
        </div>
        <div className={style.clum_center}>
          <EchartCard title={`${t("clustergram")}`} subTitle={t("only_center")}>
            <Line {...centerConfig} height={320} />
          </EchartCard>
        </div>
        <div className={style.clum_right}>
          <EchartCard title={t("Distribution_Proportion")}>
            <Pie {...pieConfig} height={320} />
          </EchartCard>
        </div>
      </div>
      <EchartCard
        title={t("class_detail")}
        subTitle={t("default_display_center")}
      >
        <Line {...config} height={480} />
      </EchartCard>
      <EchartCard
        title={t("sampling_data ")}
        subTitle={t("default_display_sampling")}
      >
        <Table
          columns={columns}
          dataSource={tableData}
          pagination={false}
          loading={loading}
        />
      </EchartCard>
      <LineComm {...props} type="dtw">
        <div>* {t("Dtw")}</div>
        <div className={style.row}>
          <span>{t("Input")}：</span>
          <p>{t("sequence_Q")}</p>
        </div>
        <div className={style.row}>
          <span>{t("modeling_process")}：</span>
          <p>{t("construct_matrix")}</p>
        </div>
        <div className={style.row}>
          <span>{t("constraint_condition")}：</span>
          <p>{t("constraint_condition_explanation")}</p>
        </div>
        <div className={style.row}>
          <span>{t("question")}：</span>
          <p>{t("find_best")}</p>
        </div>
        <div className={style.row}>
          <span>{t("algorithm")}：</span>
          <p>{t("dynamic_programming")}</p>
        </div>
      </LineComm>
    </>
  );
};

export default Dtw;
