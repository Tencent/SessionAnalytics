/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import { Input, Button } from "antd";
import { SearchOutlined } from "@ant-design/icons";

const Search = (props) => {
  const { value, onChange, onSearch, ...reset } = props;

  return (
    <>
      <Input
        value={value}
        onChange={e => onChange(e.target.value)}
        style={{ width: "2.4rem" }}
        {...reset}
      />
      <Button icon={<SearchOutlined />} onClick={onSearch} />
    </>
  );
};

export default Search;
