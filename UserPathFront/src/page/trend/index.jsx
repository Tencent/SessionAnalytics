/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect } from "react";
import style from "@/style/comm.less";
import { OverView, CommView } from "./component";
import { useStore } from "@royjs/core";
import {
  findAnalysisApi,
  getAnalysisKeyTovalue,
  analysisOptions,
  saveAcessRecord,
} from "@/utils/datamaps";
import { Tabs, Section } from "@/components/compontents";
import { useTranslation } from "react-i18next";
import dayjs from "dayjs";

const Component = (props) => {
  if (props?.event === 1) {
    return <OverView {...props} />;
  }
  if (props?.event > 1 && props?.event <= 5) {
    return <CommView {...props} exct={findAnalysisApi(props.event)} />;
  }
  return <div />;
};

const Trend = ({ location: { query } }) => {
  const { t } = useTranslation();
  const [event, setEvent] = useState(1);
  const { username, uploadName } = useStore(state => state.user);

  // 拆入日志
  const postVisitLog = (event) => {
    saveAcessRecord({
      f_analysis_name_first: "trend",
      f_analysis_name_second: getAnalysisKeyTovalue("value", "lkey", event),
      f_access_time: dayjs().format("YYYY-MM-DD HH:mm:ss"),
    });
  };

  const onEventChange = (v) => {
    setEvent(v);
    postVisitLog(v);
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

  useEffect(() => {
    if (uploadName && username) {
      postVisitLog(event);
    }
  }, [uploadName]);

  return (
    <Section>
      <Tabs
        data={analysisOptions}
        event={event}
        onEventChange={onEventChange}
      />
      <div className={style.main_content}>
        <Component
          params={{
            f_upload_name: uploadName,
          }}
          username={username}
          event={event}
          t={t}
        />
      </div>
    </Section>
  );
};

export default Trend;
