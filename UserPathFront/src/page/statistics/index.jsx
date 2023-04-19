/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect } from "react";
import style from "@/style/comm.less";
import { OverView, AgeView, SexView, AreaView, ChannelView } from "./page";
import {
  Tabs,
  EchartCard,
  Section,
} from "@/components/compontents";
import { useStore } from "@royjs/core";
import {
  findAnalysisApi,
  getAnalysisKeyTovalue,
  analysisOptions,
  saveAcessRecord,
} from "@/utils/datamaps";
import { useTranslation } from "react-i18next";
import {
  getSessionLogCnt,
  getSessionLogCntByAge,
  getSessionLogCntBySex,
  getSessionLogCntByProvince,
  getSessionLogCntByChannel,
} from "@/server/statistics";
import dayjs from "dayjs";
import { CommTable } from "./component";

const Component = (props) => {
  switch (props.event) {
    case 1:
      return <OverView {...props} />;
    case 2:
      return <AgeView {...props} />;
    case 3:
      return <SexView {...props} />;
    case 4:
      return <AreaView {...props} />;
    case 5:
      return <ChannelView {...props} />;
    default:
      return <div />;
  }
};

const Statistics = ({ location: { query } }) => {
  const [event, setEvent] = useState(1);
  const [combo, setCombo] = useState({});
  const { username, uploadName } = useStore(state => state.user);
  const { t, i18n } = useTranslation();
  const [ageData, setAgeData] = useState([]);
  const [genderData, setGenderData] = useState([]);
  const [areaData, setAreaData] = useState([]);
  const [channelData, setChannelData] = useState([]);

  // 拆入日志
  const postVisitLog = (event) => {
    saveAcessRecord({
      f_analysis_name_first: "statistics",
      f_analysis_name_second: getAnalysisKeyTovalue("value", "lkey", event),
      f_access_time: dayjs().format("YYYY-MM-DD HH:mm:ss"),
    });
  };

  useEffect(() => {
    let ev = event;
    if (username && query && Object.keys(query).length) {
      ev = getAnalysisKeyTovalue(
        "lkey",
        "value",
        query?.f_analysis_name_second,
      );
      setEvent(ev);
    }
  }, []);

  const getCombo = async (params) => {
    const { code, data } = await getSessionLogCnt(params);
    if (code === 0 && data && data.list) {
      setCombo(data?.list?.[0] ?? {});
    } else {
      setCombo({});
    }
  };

  // 年龄
  const getAgeData = async (params) => {
    const { code, data } = await getSessionLogCntByAge(params);
    if (code === 0 && data && data.list) {
      setAgeData(data.list);
    } else {
      setAgeData([]);
    }
  };

  // 性别
  const getGenderData = async (params) => {
    const { code, data } = await getSessionLogCntBySex(params);
    if (code === 0 && data && data.list) {
      setGenderData(data.list);
    } else {
      setGenderData([]);
    }
  };

  // 省份
  const getAreaData = async (params) => {
    const { code, data } = await getSessionLogCntByProvince(params);
    if (code === 0 && data && data.list) {
      setAreaData(data?.list);
    } else {
      setAreaData([]);
    }
  };

  // 渠道
  const getChannelData = async (params) => {
    const { code, data } = await getSessionLogCntByChannel(params);
    if (code === 0 && data && data.list) {
      setChannelData(data?.list);
    } else {
      setChannelData([]);
    }
  };

  useEffect(() => {
    if (username && uploadName) {
      const arg = {
        f_upload_name: uploadName,
        f_upload_user: username,
      };
      getCombo(arg);
      getAgeData(arg);
      getGenderData(arg);
      getAreaData(arg);
      getChannelData(arg);
      postVisitLog(event);
    }
  }, [uploadName]);

  const memoCombo = React.useMemo(
    () => [
      {
        label: t("total_users"),
        value: combo?.user_num ?? 0,
      },
      {
        label: t("total_sessions"),
        value: combo?.session_num ?? 0,
      },
      {
        label: t("total_pv"),
        value: combo?.pv_num ?? 0,
      },
    ],
    [combo, i18n.language],
  );

  const onEventChange = (v) => {
    setEvent(v);
    postVisitLog(v);
  };

  return (
    <Section>
      <Tabs
        data={analysisOptions}
        event={event}
        onEventChange={onEventChange}
      />
      <div className={style.main_content}>
        <EchartCard height="1.46rem">
          <div className={style.combo}>
            {memoCombo.map(item => (
              <div key={item.label}>
                <p className={style.combo_title}>{item.label}</p>
                <p className={style.combo_value}>
                  {item?.value?.toLocaleString()}
                </p>
              </div>
            ))}
          </div>
        </EchartCard>
        <Component
          params={{
            f_upload_name: uploadName,
          }}
          username={username}
          event={event}
          ageData={ageData}
          genderData={genderData}
          areaData={areaData}
          channelData={channelData}
        />
        {event !== 1 && (
          <CommTable
            username={username}
            params={{
              f_upload_name: uploadName,
            }}
            exct={findAnalysisApi(event)}
          />
        )}
      </div>
    </Section>
  );
};

export default Statistics;
