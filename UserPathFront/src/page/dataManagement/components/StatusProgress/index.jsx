/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import { Progress } from "antd";

const StatusProgress = ({ status }) => {
  switch (status) {
    case 2:
      return <Progress percent={50} status="active" />;
    case 0:
      return <Progress percent={0} />;
    case 1:
      return <Progress percent={100} />;
    case 3:
      return <Progress percent={0} status="exception" />;
    default:
      return null;
  }
};

export default StatusProgress;
