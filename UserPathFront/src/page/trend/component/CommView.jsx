/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect, useMemo, useCallback } from "react";
import EchartSearchCard from "./EchartSearchCard";
import { Line, DualAxes, Column } from "@ant-design/plots";
import { useTranslation } from "react-i18next";
import { rgb, sortAge } from "@/utils";
import { ColorMaker } from "@/utils/colorMaker";
import { getProvinceColor } from "@/utils/province";

const CommView = ({ params, username, exct, event }) => {
  const [option, setOption] = useState([]);
  const [lineData, setLineData] = useState([]);
  const [columnData, setColumnData] = useState([]);
  const [dualAxesData, setDualAxesData] = useState([[], []]);
  const { t } = useTranslation();

  const getEnumColor = (pms) => {
    if ("age" in pms) {
      switch (pms.age) {
        case "0-20":
          return "#c3e8ff";
        case "20-30":
          return "#7ec4ff";
        case "30-40":
          return "#5daeff";
        case "40-50":
          return "#4197ff";
        case "50-60":
          return "#6495ED";
        case "60+":
          return "#275cb9";
        default:
          break;
      }
    } else if ("f_sex" in pms) {
      switch (pms.f_sex) {
        case "男":
        case "Male":
          return "#275cb9";
        case "女":
        case "Female":
          return "#4197ff";
        default:
          break;
      }
    } else if ("f_channel" in pms) {
      switch (pms.f_channel) {
        case "主动打开":
        case "Open actively":
          return "#275cb9";
        case "推送打开":
        case "Push open":
          return "#4197ff";
        default:
          break;
      }
    } else {
      return rgb();
    }
  };

  const getColor = useCallback(
    (data) => {
      if (exct.paramsKey === "f_channel") {
        const item = option.find(i => i?.label === data[exct.paramsKey]);
        return `#${item?.color}`;
      }
      if (exct.paramsKey === "f_province") {
        return getProvinceColor(data[exct.paramsKey]);
      }
      return getEnumColor(data);
    },
    [exct, option],
  );

  const lineConfig = useMemo(
    () => ({
      data: lineData,
      xField: "p_date",
      yField: "user_num",
      color: d => getColor(d),
      label: false,
      seriesField: exct.par,
      legend: {
        position: "top",
      },
      animation: {
        appear: {
          animation: "path-in",
          duration: 5000,
        },
      },
      tooltip: {
        showTitle: false,
        formatter: datum => ({
          name: datum[exct.par],
          value: `${datum?.user_num?.toLocaleString()}`,
        }),
      },
    }),
    [lineData],
  );

  const dualAxesconfig = useMemo(
    () => ({
      data: dualAxesData,
      xField: "p_date",
      yField: ["session_num", "session_num"],
      geometryOptions: [
        {
          geometry: "column",
          isGroup: true,
          seriesField: exct.par,
          color: p => getColor(p),
        },
        {
          geometry: "line",
          color: "#6ed8a6",
          seriesField: exct.par,
        },
      ],
      label: false,
      legend: {
        position: "top",
      },
      tooltip: {
        showTitle: false,
        formatter: datum => ({
          name: datum[exct.par],
          value: `${datum?.session_num?.toLocaleString()}`,
        }),
      },
    }),
    [dualAxesData],
  );

  const columnConfig = useMemo(
    () => ({
      data: columnData,
      isGroup: true,
      xField: "p_date",
      yField: "pv_num",
      seriesField: exct.par,
      color: p => getColor(p),
      legend: {
        position: "top",
      },
      label: false,
      tooltip: {
        showTitle: false,
        formatter: datum => ({
          name: datum[exct.par],
          value: `${datum?.pv_num?.toLocaleString()}`,
        }),
      },
    }),
    [columnData],
  );

  const getOptions = async (params) => {
    const { code, data } = await exct.optionApi({
      ...params,
      f_upload_user: username,
    });
    if (code === 0 && data && data.list) {
      const colorMaker = new ColorMaker(data?.list?.length);
      if (exct.paramsKey === "f_channel") {
        setOption(data.list.map((item, idx) => ({
          label: item[exct.optionKey],
          value: item[exct.optionKey],
          color: colorMaker?.GetColor(idx)?.Color,
        })));
      } else {
        setOption(data.list.map(item => ({
          label: item[exct.optionKey],
          value: item[exct.optionKey],
        })));
      }
      if (["f_channel", "f_province"]?.includes(exct.paramsKey)) {
        const paramsKey = data?.list
          ?.reduce(
            (cur, pre, idx) => (idx < 5 ? [...cur, pre[exct.optionKey]] : cur),
            [],
          )
          .join(",");
        const optionParams = {
          ...params,
          [exct.paramsKey]: paramsKey,
        };
        commRequest(optionParams);
      }
    } else {
      setOption([]);
    }
  };

  const getUserCntTrend = async (params) => {
    const { code, data } = await exct.userApi({
      ...params,
      f_upload_user: username,
    });
    if (code === 0 && data && data.list) {
      if (exct.par === "age") {
        setLineData(sortAge(data?.list));
      } else {
        setLineData(data.list);
      }
    } else {
      setLineData([]);
    }
  };

  const getSessionCntTrend = async (params) => {
    const { code, data } = await exct.sessionApi({
      ...params,
      f_upload_user: username,
    });
    if (code === 0 && data && data.list) {
      if (exct.par === "age") {
        setDualAxesData(data.list.map((item, idx) => (idx === 0
          ? sortAge(item)
          : item.map(i => ({ ...i, [exct.par]: t("tren") })))));
      } else {
        setDualAxesData(data.list.map((item, idx) => (idx === 1
          ? item.map(i => ({ ...i, [exct.par]: t("tren") }))
          : item)));
      }
    } else {
      setDualAxesData([[], []]);
    }
  };

  const getPvCntTrend = async (params) => {
    const { code, data } = await exct.pvApi({
      ...params,
      f_upload_user: username,
    });
    if (code === 0 && data && data.list) {
      if (exct.par === "age") {
        setColumnData(sortAge(data?.list));
      } else {
        setColumnData(data?.list);
      }
    } else {
      setColumnData([]);
    }
  };

  const commRequest = (parasm) => {
    getUserCntTrend(parasm);
    getSessionCntTrend(parasm);
    getPvCntTrend(parasm);
  };

  useEffect(() => {
    if (params?.f_upload_name && username) {
      const param = {
        ...params,
        [exct.paramsKey]: "all",
      };
      if (["f_province", "f_channel"]?.includes(exct.paramsKey)) {
        getOptions(param);
      } else {
        getOptions(param);
        commRequest(param);
      }
    }
  }, [params?.f_upload_name, event]);

  const onLineSelectBlur = (v, fn) => {
    fn({
      ...params,
      [exct.paramsKey]: v.includes(t("All")) ? "all" : v.join(","),
    });
  };

  return (
    <>
      <EchartSearchCard
        title={t("user_daily_trend")}
        option={option}
        onLineSelectChange={v => onLineSelectBlur(v, getUserCntTrend)}
        defaultkey={exct?.paramsKey}
      >
        <Line {...lineConfig} height={487} />
      </EchartSearchCard>
      <EchartSearchCard
        title={t("session_daily_trend")}
        option={option}
        onLineSelectChange={v => onLineSelectBlur(v, getSessionCntTrend)}
        defaultkey={exct?.paramsKey}
      >
        <DualAxes {...dualAxesconfig} height={487} />
      </EchartSearchCard>
      <EchartSearchCard
        title={t("pv_daily_trend")}
        option={option}
        onLineSelectChange={v => onLineSelectBlur(v, getPvCntTrend)}
        defaultkey={exct?.paramsKey}
      >
        <Column {...columnConfig} height={487} />
      </EchartSearchCard>
    </>
  );
};

export default CommView;
