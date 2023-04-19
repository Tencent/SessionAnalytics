/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo, useState } from "react";
import { Radio } from "antd";
import style from "@/style/comm.less";

const ToggleCard = (props) => {
  const { children, title, sessionData = [], pvData = [], ...reset } = props;
  const [value, setValue] = useState("session");
  const options = [
    {
      value: "session",
      label: "Session",
      data: sessionData,
      xKey: "session_num",
    },
    {
      value: "pv",
      label: "PV",
      data: pvData,
      xKey: "pv_num",
    },
  ];

  const data = useMemo(
    () => options.find(i => i.value === value)?.data,
    [value, sessionData, pvData],
  );

  const xKey = useMemo(
    () => options.find(i => i.value === value)?.xKey,
    [value],
  );

  return (
    <div className={style.echartCard}>
      <div className={style.echartCardTitle}>
        <div>{title}</div>
        <Radio.Group
          value={value}
          options={options}
          onChange={e => setValue(e?.target?.value)}
          style={{ fontWeight: 500 }}
        />
      </div>
      {children({ data, xKey, ...reset })}
    </div>
  );
};

export default ToggleCard;
