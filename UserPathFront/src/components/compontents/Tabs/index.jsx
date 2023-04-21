/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import style from "./index.less";
import classNames from "classnames";
import { useTranslation } from "react-i18next";

const Tabs = (props) => {
  const { t } = useTranslation();
  const {
    data = [],
    event,
    onEventChange,
    bottom = false,
    padding = true,
    margin10 = false,
  } = props;

  return (
    <div
      className={classNames({
        [style.tabs]: true,
        [style.tab_margin_bottom]: bottom,
      })}
    >
      <div
        className={classNames({
          [style.tabs_nav]: true,
          [style.tabs_nav_padding]: padding,
        })}
      >
        {data.map(item => (
          <div
            key={item.lkey}
            className={classNames({
              [style.tabs_tab]: true,
              [style.tabs_tab_hover]: item.value === event,
              [style.tabs_tab_margin10]: margin10,
            })}
            onClick={() => onEventChange(item.value)}
          >
            {item.icon && <img src={item.icon} alt="" />}
            <span>{t(item.lkey)}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Tabs;
