/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { Suspense, lazy } from "react";
import { Provider } from "@royjs/core";
import store from "@/store";
import Routes from "@/components/layout";
import { HashRouter, Switch, Route, Redirect } from "react-router-dom";
import { ConfigProvider } from "antd";
import { useTranslation } from "react-i18next";
import enUS from "antd/es/locale/en_US";
import zhCN from "antd/es/locale/zh_CN";

const App = () => {
  const { i18n } = useTranslation();
  return (
    <Suspense>
      <ConfigProvider locale={i18n.language === "cn" ? zhCN : enUS}>
        <Provider store={store}>
          <HashRouter basename="/">
            <Switch>
              <Route
                exact
                path="/login"
                component={lazy(() => import("@/page/user/login"))}
                key="login"
              />
              <Route
                exact
                path="/register"
                component={lazy(() => import("@/page/user/register"))}
                key="register"
              />
              <Routes />
              <Redirect from="/*" to="/" />
            </Switch>
          </HashRouter>
        </Provider>
      </ConfigProvider>
    </Suspense>
  );
};

export default App;
