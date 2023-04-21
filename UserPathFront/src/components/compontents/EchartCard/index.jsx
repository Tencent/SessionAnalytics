/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import style from "@/style/comm.less";
import classNames from "classnames";

const EchartCard = (props) => {
  const {
    title,
    children,
    subTitle,
    rigthComp,
    setHeight = true,
    nodestyle,
  } = props;

  return (
    <div
      className={style.echartCard}
      style={nodestyle ? nodestyle : { height: "auto" }}
    >
      {title ? (
        <div
          className={classNames({
            [style.echartCardTitle]: true,
            [style.setheight]: setHeight,
          })}
        >
          <span>{title}</span>
          {rigthComp}
        </div>
      ) : null}
      {subTitle ? (
        <div className={style.subechartCardTitle}>{subTitle}</div>
      ) : null}
      {children}
    </div>
  );
};

export default EchartCard;
