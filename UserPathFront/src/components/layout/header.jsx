/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo } from "react";
import style from "./index.less";
import logo_cn from "@/images/logo_cn.png";
import logo_en from "@/images/logo-en.png";
import { routers } from "@/routes";
import { withRouter } from "react-router-dom";
import classNames from "classnames";
import { useStore, useDispatch } from "@royjs/core";
import { useTranslation } from "react-i18next";
import { Popover } from "antd";
import Switch from "@/components/layout/switch";

const Header = (props) => {
  const {
    history,
    location: { pathname },
  } = props;
  const { t, i18n } = useTranslation();
  const dispatch = useDispatch();

  const { userInfo, navList, isadmin } = useStore(state => state.user);

  const goNav = (path) => {
    history.push(path);
  };

  const styleLogo = {
    cn: {
      width: "2.24rem",
      height: "0.53rem",
    },
    en: {
      width: "2.6rem",
      height: "0.53rem",
    },
  };

  const roleRoute = useMemo(() => {
    const route = routers.filter(i => !i.hide);
    const all = navList?.includes("all");
    const includesRoute = route.filter(i => navList?.includes(i.key));
    if (all) {
      return isadmin
        ? route
        : route?.filter(i => i.key !== "platform_management");
    }
    return isadmin
      ? includesRoute
      : includesRoute?.filter(i => i.key !== "platform_management");
  }, [navList, isadmin]);

  // const languageChange = (e) => {
  //   i18n.changeLanguage(e.target.value);
  //   dispatch("user.onChangeLanguage", e.target.value);
  // };

  const logOut = () => {
    history.push("/login");
    dispatch("user.logOut");
  };

  return (
    <div className={style.header}>
      <div className={style.logo} style={styleLogo[i18n.language]}>
        <img src={i18n.language === "en" ? logo_en : logo_cn} alt="logo" />
      </div>
      <div className={style.nav}>
        {roleRoute?.map((item, idx) => (
          <span
            key={item.path}
            className={classNames(
              style.nav_item,
              pathname === item.path && style.click,
              item.line && idx !== roleRoute.length - 1 && style.line,
            )}
            onClick={() => goNav(item.path)}
          >
            {t(item.key)}
          </span>
        ))}
      </div>
      <div className={style.user}>
        <Switch />
        {/* {language === "ALL" ? (
          <Radio.Group onChange={languageChange} value={i18n.language}>
            <Radio value="cn">中</Radio>
            <Radio value="en">EN</Radio>
          </Radio.Group>
        ) : null} */}
        {userInfo?.username ? (
          <Popover
            placement="bottom"
            title={false}
            content={<div onClick={logOut} style={{ cursor: "pointer" }}>{t("logOut")}</div>}
            trigger="hover"
          >
            <span className={style.user_name}>{`${t("hello")}！${
              userInfo?.username ?? ""
            }`}</span>
          </Popover>
        ) : (
          <span className={style.user_name}>{`${t("hello")}！${
            userInfo?.username ?? ""
          }`}</span>
        )}
      </div>
    </div>
  );
};

export default withRouter(Header);
