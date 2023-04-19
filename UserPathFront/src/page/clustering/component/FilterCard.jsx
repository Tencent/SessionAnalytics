/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState } from "react";
import { Select, Button } from "antd";
import { EchartCard } from "@/components/compontents";
import { useTranslation } from "react-i18next";
import style from "./index.less";
import { v4 as uuidv4 } from "uuid";

const FilterCard = (props) => {
  const { t } = useTranslation();
  const {
    process = [],
    explain = "",
    onLabelChange,
    options = [],
    height = "340px",
    filterLable = t("filter"),
    initValue = "0",
  } = props;

  const [label, setLabel] = useState(initValue);
  return (
    <EchartCard
      title={t("instruction_and_filter")}
      nodestyle={{ flex: "none" }}
    >
      <div className={style.explain} style={{ height }}>
        <div>* {t("data_instruction")}</div>
        {process.map((i) => {
          const item = i.split("：");
          return (
            <div className={style.row} key={uuidv4()}>
              <span>{item[0]}：</span>
              <p>{item[1]}</p>
            </div>
          );
        })}
        <div style={{ marginTop: "0.2rem" }}>* {filterLable}</div>
        <div className={style.row}>{explain}</div>
        <div className={style.row}>
          <Select
            showSearch
            style={{ width: "3rem", marginTop: "0.1rem" }}
            value={label}
            onChange={e => setLabel(e)}
            placeholder={t("please_label")}
            options={options}
          />
          <Button
            type="primary"
            style={{ marginTop: "0.1rem", marginLeft: "0.2rem" }}
            onClick={() => label && onLabelChange(label)}
          >
            {t("apply")}
          </Button>
        </div>
      </div>
    </EchartCard>
  );
};

export default FilterCard;
