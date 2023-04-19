/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo } from "react";
import { Table as AntTable } from "antd";
import style from "./index.less";

const Table = (props) => {
  const {
    pagination,
    dataSource,
    onChange,
    sourceLength,
    current = true,
    ...reset
  } = props;

  const pagiNation = useMemo(
    () => (!pagination
      ? false
      : {
        hideOnSinglePage: true,
        current: pagination?.page + (current ? 1 : 0),
        pageSize: pagination?.page_size,
        total: Array.isArray(dataSource)
          ? sourceLength ?? 0
          : dataSource?.total ?? 0,
        onChange,
      }),
    [pagination, dataSource, sourceLength, current],
  );

  const dataSourceLIst = useMemo(() => (Array.isArray(dataSource) ? dataSource : dataSource?.list), [dataSource]);

  return (
    <div className={style.tableBox}>
      <AntTable
        pagination={pagiNation}
        dataSource={dataSourceLIst || []}
        {...reset}
      />
    </div>
  );
};

export default React.memo(Table);
