/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState } from "react";
import { Input } from "antd";

const EditInput = (props) => {
  const { value, onChange, disabled = false } = props;
  const [state, setState] = useState(value);

  return (
    <Input
      disabled={disabled}
      value={state}
      onChange={e => setState(e.target.value)}
      onBlur={e => onChange(e.target.value)}
      style={{ width: "3.2rem" }}
    />
  );
};

export default EditInput;
