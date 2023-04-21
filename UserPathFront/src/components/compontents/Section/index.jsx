/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
// import Switch from "@/components/layout/switch";
import style from "./index.less";

const Section = (props) => {
  const { children } = props;
  return (
    <div className={style.section}>
      {/* <Switch /> */}
      <div className={style.children}>{children}</div>
    </div>
  );
};

export default Section;
