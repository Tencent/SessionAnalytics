/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import { ReloadOutlined, ExpandOutlined } from "@ant-design/icons";
import { FullScreen, useFullScreenHandle } from "react-full-screen";
import style from "@/style/comm.less";

const EchratMain = (props) => {
  const { refresh, children, handleCHange } = props;
  const handle = useFullScreenHandle();

  return (
    <>
      <FullScreen
        handle={handle}
        onChange={handleCHange}
        className={style.fullScreen}
      >
        {children}
      </FullScreen>
      <div className={style.functional}>
        <ReloadOutlined className={style.functional_icon} onClick={refresh} />
        <ExpandOutlined
          className={style.functional_icon}
          onClick={handle.enter}
        />
      </div>
    </>
  );
};

export default React.memo(EchratMain);
