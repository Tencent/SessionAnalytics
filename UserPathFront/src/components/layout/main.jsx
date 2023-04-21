/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { Suspense, useMemo } from "react";
import { routers } from "@/routes";
import ProtectedRoute from "./ProtectedRoute";
import { LoadingOutlined } from "@ant-design/icons";
import { Spin } from "antd";
import { useStore } from "@royjs/core";
import style from "./index.less";

const Main = () => {
  const { navList } = useStore(state => state.user);
  const roleRoute = useMemo(() => {
    if (navList?.includes("all")) {
      return routers;
    }
    return routers.filter(i => navList?.includes(i.key));
  }, [navList]);

  const renderRoutes = (data) => {
    if (data.children) {
      return data.children
        .map(renderRoutes)
        .concat(<ProtectedRoute
          path={data.path}
          exact
          component={data.component}
          key={data.key}
        />);
    }
    return (
      <ProtectedRoute
        path={data.path}
        exact
        component={data.component}
        key={data.path}
      />
    );
  };

  return (
    <Suspense
      fallback={
        <div className={style.body}>
          <Spin
            indicator={<LoadingOutlined style={{ fontSize: "0.36rem" }} spin />}
          />
        </div>
      }
    >
      <div className={style.main}>{roleRoute.map(renderRoutes)}</div>
    </Suspense>
  );
};

export default Main;
