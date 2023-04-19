/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useMemo, useState } from "react";
import { message, Select } from "antd";
import style from "./index.less";
import { useTranslation } from "react-i18next";

const SelectAll = (props) => {
  const {
    label = "select_trend_indicator",
    defaultkey,
    option = [],
    onLineSelectChange,
  } = props;
  const [value, setValue] = useState({
    initValue: [],
    changeValue: [],
  });
  const { t } = useTranslation();

  const options = useMemo(
    () => (option?.length
      ? [{ label: t("All"), value: t("All") }, ...option]
      : option),
    [option],
  );

  useEffect(() => {
    if (options?.length) {
      if (defaultkey && ["f_province", "f_channel"]?.includes(defaultkey)) {
        const op = option.map(i => i.value).filter((_, idx) => idx < 5);
        setValue({
          initValue: op,
          changeValue: op,
        });
      } else {
        setValue({
          initValue: [t("All")],
          changeValue: [t("All")],
        });
      }
    } else {
      setValue({
        initValue: [],
        changeValue: [],
      });
    }
  }, [options]);

  const onChange = (v) => {
    if (v.length === 0 || (v.length === 6 && v.includes(t("All")))) {
      setValue(s => ({
        ...s,
        changeValue: [t("All")],
      }));
      return;
    }
    if (v.length > 5) {
      message.error("最多只能选5个");
      return;
    }
    if (v?.[v.length - 1] === t("All")) {
      setValue(s => ({
        ...s,
        changeValue: [t("All")],
      }));
    } else {
      setValue(s => ({
        ...s,
        changeValue: v.filter(i => i !== t("All")),
      }));
    }
  };

  const onBlur = () => {
    if (!(value.initValue === value.changeValue)) {
      setValue(s => ({
        ...s,
        initValue: s.changeValue,
      }));
      onLineSelectChange(value.changeValue);
    }
  };

  return (
    <div className={style.selectAll}>
      <div className={style.selectAll_label}>{t(label)}</div>
      <Select
        mode="multiple"
        value={value.changeValue}
        style={{ width: "1.8rem" }}
        options={options}
        maxTagCount={1}
        maxTagPlaceholder={`+${value?.changeValue?.length - 1}`}
        onChange={onChange}
        onBlur={onBlur}
        removeIcon=""
      />
    </div>
  );
};

export default React.memo(SelectAll);
