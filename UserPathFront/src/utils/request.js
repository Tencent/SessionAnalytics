/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
// 引入axios
import axios from "axios";
// 引入环境变量对应请求地址
// 引入提示
import { message } from "antd";
import store from "@/store";
// 创建axios
const instance = axios.create({
  baseURL: "",
  withCredentials: false,
});

// axios请求拦截器
instance.interceptors.request.use((config) => {
  const token = store?.state?.user?.token;
  config.url = `${config.url}`;
  config.headers.token = token;
  config.headers.user = store?.state?.user?.userInfo?.username;
  return config;
});

// axios响应拦截器
instance.interceptors.response.use(
  (response) => {
    // 对响应状态码操作
    const { code, message: msg } = response.data;
    switch (code) {
      case 0:
      case 1:
      case 200:
        break;
      default:
        message.error(`[${code}]${msg}`);
        break;
    }
    return response.data;
  },
  // 对错误进行操作
  (error) => {
    const { status, data } = error.response || {};
    const msg = data?.message;
    switch (status) {
      case 401:
        message.error("信息未授权，请重新登录");
        window.location.href = "/#/login";
        localStorage.removeItem("userInfo");
        break;
      case 403:
        window.location.reload();
        break;
      case 10011:
        message.error("登录已过期，请重新登录");
        window.location.href = "/#/login";
        localStorage.removeItem("userInfo");
        break;
      default:
        if (status) {
          message.error(`[${status}]${msg || "出错了"}`);
        } else {
          message.error(error.message);
        }
        window.location.href = "/#/login";
        localStorage.removeItem("userInfo");
    }
    return Promise.reject(error);
  },
);
export default instance;
