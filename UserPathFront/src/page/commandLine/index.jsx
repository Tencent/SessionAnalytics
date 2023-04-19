/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState } from "react";
import style from "./index.less";
import { Tabs } from "@/components/compontents";
import SystemDemon from "./component/SystemDemo";
import Carousel from "./component/Carousel";
import caseImages from "@/images/case";
import frameworkImages from "@/images/framework";

const Component = ({ event }) => {
  switch (event) {
    case 1:
      return <SystemDemon />;
    case 2:
      return <Carousel imageList={caseImages} />;
    case 3:
      return <Carousel imageList={frameworkImages} />;
  }
};

const CommandLine = () => {
  const [event, setEvent] = useState(2);

  const tabsOption = [
    {
      value: 1,
      lkey: "system_demonstration",
    },
    {
      value: 2,
      lkey: "business_case",
    },
    {
      value: 3,
      lkey: "technical_framework",
    },
  ];

  return (
    <div className={style.commandLine}>
      <Tabs data={tabsOption} event={event} onEventChange={setEvent} />
      <Component event={event} />
    </div>
  );
};

export default CommandLine;
