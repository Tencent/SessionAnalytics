/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState } from "react";
import style from "../index.less";
import logo from "@/images/login.png";
import user from "@/images/user.svg";
import password from "@/images/password.svg";
import { Button, Form, Input, message } from "antd";
import classNames from "classnames";
import { encryption } from "@/utils";
import { postLogin } from "@/server/user";
import { useDispatch } from "@royjs/core";

const { Item } = Form;

const Login = ({ history }) => {
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();
  const onFinish = async (format) => {
    setLoading(true);
    const params = {
      username: format.username,
      password: await encryption(format.password),
    };
    const { code, data } = await postLogin(params);
    if (code === 0 && data) {
      dispatch("user.setUserInfo", data);
      history.push("/statistics");
    } else {
      message.error("登录失败，请重试");
    }
    setLoading(false);
  };

  return (
    <div className={classNames(style.layout, style.flex)}>
      <div className={style.bg} />
      <div className={style.login}>
        <img src={logo} alt="" />
        <div className={style.headline}>
          <span>用户</span>
          <span>路径分析</span>
        </div>
        <Form className={style.form} onFinish={onFinish}>
          <Item
            name="username"
            rules={[
              {
                required: true,
                message: "请输入用户名",
              },
            ]}
          >
            <Input
              autoComplete="username"
              style={{ width: "300px" }}
              prefix={
                <img src={user} style={{ width: "24px", height: "24px" }} />
              }
              placeholder="请输入用户名称"
            />
          </Item>
          <Item
            name="password"
            rules={[
              {
                required: true,
                message: "请输入密码",
              },
            ]}
          >
            <Input.Password
              style={{ width: "300px" }}
              prefix={
                <img src={password} style={{ width: "24px", height: "24px" }} />
              }
              placeholder="请输入用户密码"
              autoComplete="password"
            />
          </Item>
          <Item>
            <Button
              style={{ width: "100%", textAlign: "center" }}
              type="primary"
              htmlType="submit"
              loading={loading}
            >
              登录
            </Button>
          </Item>
        </Form>
        <div className={style.bind}>
          <span onClick={() => history.push("/register")}>新用户注册</span>
        </div>
      </div>
    </div>
  );
};

export default Login;
