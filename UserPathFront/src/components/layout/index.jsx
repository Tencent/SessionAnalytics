/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, Suspense } from "react";
import { useDispatch } from "@royjs/core";
import Header from "./header";
import Main from "./main";
import { withRouter } from "react-router-dom";
import { useTranslation } from "react-i18next";

const Routes = () => {
  const dispatch = useDispatch();
  const { i18n } = useTranslation();
  useEffect(() => {
    localStorage.getItem("userInfo")
      && dispatch("user.getUserInfo", i18n.language);
  }, []);

  return (
    <Suspense>
      <Header />
      <Main />
    </Suspense>
  );
};

export default withRouter(Routes);
