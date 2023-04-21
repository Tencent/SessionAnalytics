/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useState } from "react";
import style from "./index.less";
import regen from "@/images/regen.svg";
import { useStore, useDispatch } from "@royjs/core";
import { getRecentAcessRecord } from "@/server/path";
import { dayDiff } from "@/utils";
import { useTranslation } from "react-i18next";
import home_cn from "@/images/home_cn.png";
import home_en from "@/images/home_en.png";

const Home = ({ history }) => {
  const { t, i18n } = useTranslation();
  const dispatch = useDispatch();
  const { username, userInfo } = useStore(state => state.user);
  const [acessRecord, setAcessRecord] = useState([]);
  const goDetail = (item) => {
    history.push({
      pathname: `/${item.f_analysis_name_first}`,
      query: item,
    });
    dispatch("user.onChangeFieldsName", item.f_upload_name);
  };

  const getAcessRecord = async () => {
    const { code, data } = await getRecentAcessRecord({
      f_upload_user: userInfo?.username,
    });
    if (code === 0 && data && data.list && data.list.length) {
      setAcessRecord(data.list.filter((_, idx) => idx < 4));
    } else {
      setAcessRecord([]);
    }
  };

  useEffect(() => {
    if (username) {
      getAcessRecord();
    }
  }, [username]);

  return (
    <>
      <div className={style.visit}>
        <div className={style.visit_title}>{t("recents")}</div>
        <div className={style.visivt_content}>
          {acessRecord.map((item, idx) => (
            <div
              className={style.visit_item}
              key={idx}
              onClick={() => goDetail(item, 1)}
            >
              <img src={regen} alt="" />
              <div className={style.item_content}>
                <span>{item?.f_upload_name}</span>
                <span>
                  {t(item.f_analysis_name_first)} -{" "}
                  {t(item.f_analysis_name_second)}
                </span>
                <p className={style.item_time}>{`${
                  i18n.language === "en" ? "Viewed" : ""
                } ${dayDiff(item?.f_access_time, t, i18n)}\u00A0${t("ago")}`}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
      <div className={style.flowChart}>
        <img src={i18n.language === "cn" ? home_cn : home_en} />
      </div>
    </>
  );
};

export default Home;
