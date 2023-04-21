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
import logo_cn from "@/images/logo_cn.png";
import mistake from "@/images/mistake.png";
import succese from "@/images/succese.png";
import classNames from "classnames";
import { Form, Input, Button, message } from "antd";
import ReactSimpleVerify from "react-simple-verify";
import { postRegister, getUserNameExist } from "@/server/user";
import { debounce } from "lodash";
import { encryption } from "@/utils";

const { Item } = Form;

const Register = ({ history }) => {
  const [form] = Form.useForm();
  const [userVisible, setUserVisible] = useState(false);
  const [pwdVisible, setPwdVisible] = useState(false);
  const [showResult, setShowResult] = useState(true);
  const [verifySuccess, setVerifySuccess] = useState(0);
  const [loading, setLoading] = useState(false);

  const onFinish = async (format) => {
    if (format.password !== format.rePassword) {
      setPwdVisible(true);
      return;
    }
    if (userVisible) return;
    if (verifySuccess === 0 || verifySuccess === 2) {
      setVerifySuccess(2);
      return;
    }
    setLoading(true);
    console.log("format", format);
    const params = {
      username: format.username,
      password: await encryption(format.password),
    };
    const { code, message: msg } = await postRegister(params);
    setLoading(false);
    if (code === 0) {
      setShowResult(false);
    } else {
      message.error(msg);
    }
  };

  const onUserNameExist = async (e) => {
    if (e.target.value) {
      const { code } = await getUserNameExist({
        username: e.target.value,
      });
      if (code === 1) {
        setUserVisible(true);
      } else {
        setUserVisible(false);
      }
    }
  };

  const onPwdNameExist = (e) => {
    const formValue = form.getFieldValue();

    if (
      (!formValue.password && !e.target.value)
      || formValue.password === e.target.value
    ) {
      setPwdVisible(false);
    } else {
      setPwdVisible(true);
    }
  };

  return (
    <div className={classNames(style.layout, style.direction)}>
      <div className={style.head}>
        <img src={logo_cn} alt="logo" />
      </div>
      <div className={style.section}>
        {showResult ? (
          <div className={style.register}>
            <div className={style.register_back}>
              已有账号？
              <span onClick={() => history.push("/login")}>{"快速登录>>"}</span>
            </div>
            <div className={style.form_title}>欢迎注册用户路径分析</div>
            <Form form={form} onFinish={onFinish}>
              <Item style={{ position: "relative" }}>
                <Item
                  noStyle
                  name="username"
                  rules={[
                    {
                      required: true,
                      message: "请输入用户名",
                    },
                    {
                      pattern: /^[a-zA-Z0-9-_]{6,16}$/,
                      message: "由字母、数字、下划线组成,6~16位",
                    },
                  ]}
                >
                  <Input
                    autoComplete="username"
                    style={{ width: "320px" }}
                    onChange={debounce(onUserNameExist, 500)}
                    placeholder="设置用户名"
                  />
                </Item>
                {userVisible && (
                  <div className={style.hint}>
                    <img src={mistake} alt="" />
                    <span>用户名已被占用</span>
                  </div>
                )}
              </Item>
              <Item
                name="password"
                rules={[
                  {
                    required: true,
                    message: "请输入密码",
                  },
                  {
                    pattern: /^(?![a-zA-Z]+$)(?!\d+$)(?![^\da-zA-Z\s]+$).{8,20}$/,
                    message: "由字母、数字、特殊字符，任意2种组成，8~20位",
                  },
                ]}
              >
                <Input.Password
                  style={{ width: "320px" }}
                  autoComplete="password"
                  placeholder="设置你的登录密码"
                />
              </Item>
              <Item style={{ position: "relative" }}>
                <Item
                  noStyle
                  name="rePassword"
                  rules={[
                    {
                      required: true,
                      message: "请确认密码",
                    },
                  ]}
                >
                  <Input.Password
                    autoComplete="re-password"
                    style={{ width: "320px" }}
                    onChange={debounce(onPwdNameExist, 200)}
                    placeholder="请再次输入你的密码"
                  />
                </Item>
                {pwdVisible && (
                  <div className={style.hint}>
                    <img src={mistake} alt="" />
                    <span>两次密码不一致请检查</span>
                  </div>
                )}
              </Item>
              <Item style={{ position: "relative" }}>
                <Item noStyle>
                  <ReactSimpleVerify
                    width={320}
                    movedColor="#3B93B4"
                    success={() => setVerifySuccess(1)}
                  />
                </Item>
                {verifySuccess === 2 && (
                  <div className={style.hint}>
                    <img src={mistake} alt="" />
                    <span>请进行滑块验证</span>
                  </div>
                )}
              </Item>
              <Button
                style={{ width: "100%", textAlign: "center" }}
                type="primary"
                htmlType="submit"
                loading={loading}
              >
                注册
              </Button>
            </Form>
          </div>
        ) : (
          <div className={classNames(style.register, style.result)}>
            <img src={succese} alt="" />
            <p>恭喜注册成功</p>
            <p>请妥善保管您的账户信息</p>
            <p onClick={() => history.push("/login")}>{"返回登录页面 >"}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Register;
