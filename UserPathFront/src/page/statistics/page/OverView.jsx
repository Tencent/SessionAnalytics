/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useMemo, useState, useCallback } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import {
  AgeSession,
  GenderSession,
  ChannelSession,
  ToggleCard,
  ProvinceSession,
} from "../component";
import { useTranslation } from "react-i18next";
import {
  getLogSessionDistribute,
  getLogPVDistribute,
} from "@/server/statistics";
import { depend } from "@/utils/datamaps";
import ReactECharts from "echarts-for-react";
import style from "@/style/comm.less";
import { v4 as uuidv4 } from "uuid";

const OverView = (props) => {
  const {
    params,
    username,
    ageData = [],
    genderData = [],
    areaData = [],
    channelData = [],
  } = props;
  const { t, i18n } = useTranslation();
  const [column, setColumn] = useState({});
  const [line, setLine] = useState({});
  const [sessionLoading, setSessionLoading] = useState(false);
  const [pvLoading, setPvLoading] = useState(false);

  const memoColumn = useMemo(
    () => [
      {
        name: t("25-Quantile"),
        value: column?.f_session_25p?.toLocaleString(),
      },
      {
        name: t("50-Quantile"),
        value: column?.f_session_50p?.toLocaleString(),
      },
      {
        name: t("75-Quantile"),
        value: column?.f_session_75p?.toLocaleString(),
      },
      {
        name: t("maximum"),
        value: column?.f_session_max?.toLocaleString(),
      },
      {
        name: t("average"),
        value: column?.f_session_avg?.toLocaleString(),
      },
    ],
    [column, i18n.language],
  );

  const memoLine = useMemo(
    () => [
      {
        name: t("25-Quantile"),
        value: line?.f_session_pv_25p?.toLocaleString(),
      },
      {
        name: t("50-Quantile"),
        value: line?.f_session_pv_50p?.toLocaleString(),
      },
      {
        name: t("75-Quantile"),
        value: line?.f_session_pv_75p?.toLocaleString(),
      },
      {
        name: t("maximum"),
        value: line?.f_session_pv_max?.toLocaleString(),
      },
      {
        name: t("average"),
        value: line?.f_session_pv_avg?.toLocaleString(),
      },
    ],
    [line, i18n.language],
  );

  const boxplotOption = (d) => {
    const data = d?.map(item => item?.value);
    return {
      dataset: [
        {
          source: [data],
        },
        {
          transform: {
            type: "boxplot",
          },
        },
        {
          fromDatasetIndex: 1,
          fromTransformResult: 1,
        },
      ],
      series: [
        {
          type: "boxplot",
          datasetIndex: 1,
        },
        {
          name: "outlier",
          type: "scatter",
          datasetIndex: 2,
        },
      ],
      xAxis: {
        type: "category",
        boundaryGap: true,
        nameGap: 30,
        splitArea: {
          show: false,
        },
        splitLine: {
          show: false,
        },
      },
      yAxis: {
        type: "value",
        splitArea: {
          show: true,
        },
      },
      tooltip: {
        show: false,
      },
    };
  };

  const getColumn = async (params) => {
    setSessionLoading(true);
    const { code, data } = await getLogSessionDistribute(params);
    if (code === 0 && data && data.list) {
      setColumn(data?.list?.[0] ?? {});
    } else {
      setColumn({});
    }
    setSessionLoading(false);
  };

  const getLine = async (params) => {
    setPvLoading(true);
    const { code, data } = await getLogPVDistribute(params);
    if (code === 0 && data && data.list) {
      setLine(data?.list?.[0] ?? {});
    } else {
      setLine({});
    }
    setPvLoading(false);
  };

  useEffect(() => {
    if (params?.f_upload_name && username) {
      const param = {
        ...params,
        f_upload_user: username,
      };
      getColumn(param);
      getLine(param);
    }
  }, [...depend(params)]);

  const ageToggle = useCallback(
    props => <AgeSession {...props} />,
    [memoColumn],
  );

  const genderToggle = useCallback(
    props => <GenderSession {...props} />,
    [genderData],
  );

  const provinceToggle = useCallback(
    props => <ProvinceSession {...props} />,
    [areaData],
  );

  const channelToggle = useCallback(
    props => <ChannelSession {...props} />,
    [channelData],
  );

  const Tooltip = ({ tipList = [] }) => (
    <div className={style.boxplotBox}>
      {tipList?.map(item => (
        <div key={uuidv4()}>
          <span>{item?.name}：</span>
          <span>{item?.value}</span>
        </div>
      ))}
    </div>
  );

  return (
    <>
      <div className={style.pieview}>
        <div className={style.pieview_item}>
          <EchartCard title={t("session_distribution")}>
            <ReactECharts
              option={boxplotOption(memoColumn)}
              style={{ height: "4.8rem", width: "100%" }}
              showLoading={sessionLoading}
            />
            <Tooltip tipList={memoColumn} />
          </EchartCard>
        </div>
        <div className={style.pieview_item}>
          <EchartCard title={t("pv_distribution")}>
            <ReactECharts
              option={boxplotOption(memoLine)}
              style={{ height: "4.8rem", width: "100%" }}
              showLoading={pvLoading}
            />
            <Tooltip tipList={memoLine} />
          </EchartCard>
        </div>
      </div>
      <div className={style.pieview}>
        <div className={style.pieview_item}>
          <ToggleCard
            title={t("Age_Session_Distribution")}
            sessionData={ageData?.[1] ?? []}
            pvData={ageData?.[0] ?? []}
          >
            {ageToggle}
          </ToggleCard>
        </div>
        <div className={style.pieview_item}>
          <ToggleCard
            title={t("Gender_Session_Distribution")}
            sessionData={genderData?.[1] ?? []}
            pvData={genderData?.[0] ?? []}
          >
            {genderToggle}
          </ToggleCard>
        </div>
      </div>
      <div className={style.pieview}>
        <div className={style.pieview_item}>
          <ToggleCard
            title={t("Area_session_distribution")}
            sessionData={areaData?.[1]}
            pvData={areaData?.[0]}
          >
            {provinceToggle}
          </ToggleCard>
        </div>
        <div className={style.pieview_item}>
          <ToggleCard
            title={t("channel_distribution")}
            sessionData={channelData?.[1] ?? []}
            pvData={channelData?.[0] ?? []}
          >
            {channelToggle}
          </ToggleCard>
        </div>
      </div>
    </>
  );
};

export default OverView;
