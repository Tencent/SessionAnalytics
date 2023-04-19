/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useState } from "react";
import { Tabs, Section } from "@/components/compontents";
import kms_icon from "@/images/clustering_icon1.png";
import dt_icon from "@/images/clustering_icon2.png";
import Dtw from "./component/dtw";
import Kmeans from "./component/kmeans";
import Graph from "./component/Graph";
import style from "@/style/comm.less";
import { useStore } from "@royjs/core";
import { useTranslation } from "react-i18next";
import { saveAcessRecord } from "@/utils/datamaps";
import dayjs from "dayjs";

const Component = (props) => {
  switch (props.event) {
    case 1:
      return <Kmeans {...props} />;
    case 2:
      return <Dtw {...props} />;
    case 3:
      return <Graph {...props} />;
    default:
      return <></>;
  }
};

const Clustering = ({ location: { query } }) => {
  const { t, i18n } = useTranslation();
  const { username, uploadName } = useStore(state => state.user);
  const [event, setEvent] = useState(1);

  // 拆入日志
  const postVisitLog = (event) => {
    saveAcessRecord({
      f_analysis_name_first: "clustering",
      f_analysis_name_second: options.find(item => item.value === event)
        ?.lkey,
      f_access_time: dayjs().format("YYYY-MM-DD HH:mm:ss"),
    });
  };

  const options = [
    {
      lkey: "Kmeans",
      value: 1,
      icon: kms_icon,
    },
    {
      lkey: "dtw",
      value: 2,
      icon: dt_icon,
    },
    {
      lkey: "graph",
      value: 3,
      icon: kms_icon,
    },
  ];

  useEffect(() => {
    let ev = event;
    if (username && query && Object.keys(query).length) {
      ev = options.find(item => item.lkey === query?.f_analysis_name_second)?.value;
      setEvent(ev);
    }
  }, []);

  useEffect(() => {
    if (uploadName) {
      postVisitLog(event);
    }
  }, [uploadName]);

  const onEventChange = (v) => {
    setEvent(v);
    postVisitLog(v);
  };

  return (
    <Section>
      <Tabs data={options} event={event} onEventChange={onEventChange} />
      <div className={style.main_content}>
        <Component
          event={event}
          params={{
            f_upload_name: uploadName,
          }}
          username={username}
          t={t}
          i18n={i18n}
        />
      </div>
    </Section>
  );
};

export default Clustering;
