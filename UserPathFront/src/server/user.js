/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
// 请求方法
// 请求host
import request from "@/utils/request";
// import config from "@/utils/config";

// 获取用户信息
// export const getUserInfo = () => request({
//   url: "/getUserInfo",
//   baseURL: config.sessionBaseURL,
//   withCredentials: true,
// });

// // 获取用户列表
// export const getUploadUserList = () => request({
//   url: "/role/getUploadUserList",
// });

export const getUploadUserExist = data => request({
  url: "/role/getUploadUserExist",
  params: data,
  method: "POST",
});

// 注册
export const postRegister = data => request({
  url: "/role/register",
  params: data,
  method: "POST",
});

// 登录
export const postLogin = data => request({
  url: "/role/login",
  params: data,
  method: "POST",
});

// 判断用户是否存在
export const getUserNameExist = data => request({
  url: "/role/getUserNameExist",
  params: data,
  method: "POST",
});

// 获取公钥
export const getPublicKey = () => request({ url: "/role/getPublicKey", method: "POST" });

// 获取管理员时上传人列表
export const getUploadUserList = data => request({
  url: "/role/getUploadUserList",
  params: data,
  method: "POST",
});
