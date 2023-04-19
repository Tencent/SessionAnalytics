/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import { Form, InputNumber } from "antd";
import { useTranslation } from "react-i18next";
import style from "./index.less";

const { Item } = Form;

const RangeInput = (props) => {
  const { names = [], label, onBlur } = props;
  const { t } = useTranslation();
  return (
    <Item label={label} style={{ marginBottom: 0 }} {...props}>
      <div className={style.rangeInput}>
        <Item
          name={names?.[0]}
          className={style.formItem}
          rules={[
            {
              required: true,
              message: t("please_session_num"),
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (
                  (!value && value !== 0)
                  || getFieldValue(names?.[1]) >= value
                ) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error(t("greater_than")));
              },
            }),
          ]}
        >
          <InputNumber className={style.numberWidth} min={0} onBlur={onBlur} />
        </Item>
        <p className={style.formline}></p>
        <Item
          className={style.formItem}
          name={names?.[1]}
          rules={[
            {
              required: true,
              message: t("please_session_num_end"),
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (
                  (!value && value !== 0)
                  || getFieldValue(names?.[0]) < value
                ) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error(t("less_than")));
              },
            }),
          ]}
        >
          <InputNumber
            className={style.numberWidth}
            min={0}
            max={1000000000}
            onBlur={onBlur}
          />
        </Item>
      </div>
    </Item>
  );
};

export default RangeInput;
