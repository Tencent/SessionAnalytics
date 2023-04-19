/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo, useState } from "react";
import { SwapOutlined } from "@ant-design/icons";
import style from "./index.less";
import { useTranslation } from "react-i18next";
import { Select, Drawer, Row, Col } from "antd";
import { useStore, useDispatch } from "@royjs/core";
import { withRouter } from "react-router-dom";
import { routers } from "@/routes";

const Switch = ({ location }) => {
  const [open, setOpen] = useState(false);
  const {
    t,
    i18n: { language },
  } = useTranslation();
  const dispatch = useDispatch();
  const { userList, option, uploadName, username, isadmin } = useStore(state => state.user);

  const ColStyle = {
    textAlign: "right",
    paddingRight: "0.1rem",
    lineHeight: "0.32rem",
  };

  const showSwitch = useMemo(() => {
    const includePath = routers?.find(i => i?.path === location?.pathname && i?.include);
    if (includePath && uploadName) {
      return (
        <>
          <span>{t("Current_DataSet")}：</span>
          <div className={style.commTree} onClick={() => setOpen(true)}>
            <SwapOutlined style={{ fontSize: 18, marginRight: 10 }} />
            <span className={style.commTree_name}>{uploadName}</span>
          </div>
        </>
      );
    }
  }, [location?.pathname, uploadName, language]);

  return (
    <>
      {showSwitch}
      <Drawer
        placement="right"
        open={open}
        className={style.drawer}
        onClose={() => setOpen(false)}
      >
        <div className={style.drawer_title}>{t("Switch_Datasets")}:</div>
        {isadmin ? (
          <Row style={{ marginBottom: "0.15rem" }}>
            <Col span={4} style={ColStyle}>
              {t("user")}：
            </Col>
            <Col span={18}>
              <Select
                value={username}
                options={userList}
                style={{ width: "4.4rem", height: "0.32rem" }}
                onChange={(name) => {
                  dispatch("user.onChangeUserName", name);
                }}
              />
            </Col>
          </Row>
        ) : null}
        <Row style={{ marginBottom: "0.15rem" }}>
          <Col span={4} style={ColStyle}>
            {t("Datasets")}：
          </Col>
          <Col span={18}>
            <Select
              value={uploadName}
              options={option}
              style={{ width: "4.4rem", height: "0.32rem" }}
              onChange={(name) => {
                dispatch("user.onChangeFieldsName", name);
              }}
            />
          </Col>
        </Row>
      </Drawer>
    </>
  );
};

export default withRouter(Switch);
