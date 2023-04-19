/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import { Redirect, Route } from "react-router-dom";
import { useStore } from "@royjs/core";

const ProtectedRoute = ({ path, component }) => {
  const { token } = useStore(state => state.user);

  if (!token && window.location.pathname !== "/login") {
    return <Redirect exact to="/login" />;
  }
  return <Route path={path} exact component={component} />;
};

export default ProtectedRoute;
