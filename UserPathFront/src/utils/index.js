/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import dayjs from "dayjs";
import { getPublicKey } from "@/server/user";
import JSEncrypt from "jsencrypt";

// 生成随机颜色
export const rgb = () => {
  const r = Math.floor(Math.random() * 256);
  const g = Math.floor(Math.random() * 256);
  const b = Math.floor(Math.random() * 256);
  const rgb = `rgb(${r},${g},${b})`;
  return rgb;
};

// 下载模板
export const downloadTemplate = (url) => {
  if (typeof url === "object" && url instanceof Blob) {
    url = URL.createObjectURL(url);
  }
  const link = document.createElement("a");
  link.setAttribute("href", url);
  link.setAttribute("download", "");
  link.click();
};

// 生成随机数
export function random(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}

// 获取多少时间格式之前
export const dayDiff = (time, t, i18n) => {
  const d = key => dayjs().diff(time, key);
  const s = i18n.language === "en" ? "s" : "";
  const key = ["year", "month", "day", "hour", "minute", "second"]?.find(k => d(k) > 0);
  if (d(key) > 0) {
    return d(key) === 1 ? d(key) + t(key) : d(key) + t(key) + s;
  }
};

// 层级=>replace
export const hierarchyToLv = (i18n, item) => {
  if (i18n.language === "en") {
    return item
      ?.replace("层级", "LV")
      .replace("结束", "END")
      .replace("未知", "Unknown")
      .replace("生命周期", "Life cycle")
      .replace("其他", "Other");
  }
  return item;
};

// 层级=>replace
export const hierarchyArrToLv = (i18n, data, key = "label") => {
  if (i18n.language === "en") {
    return data.map(item => ({
      ...item,
      [key]: item[key]
        ?.replace("层级", "LV")
        ?.replace("结束", "END")
        .replace("未知", "Unknown"),
    }));
  }
  return data;
};

// age 排序
export const sortAge = (data = []) => {
  const map = new Map();
  let arr = [];
  ["0-20", "20-30", "30-40", "40-50", "50-60", "60+", "未知"].forEach((item) => {
    map.set(item, []);
  });
  data.forEach((item) => {
    if (map.get(item?.age)) {
      map.set(item?.age, map.get(item?.age).concat(item));
    } else {
      map.set(item?.age, [item]);
    }
  });

  for (const value of map.values()) {
    arr = [...arr, ...value];
  }
  return arr;
};

// 本地分页
export const getPagenation = (list = [], page = 0, size = 10) => list.slice(page * size, (page + 1) * size);

// 字符encode编码替换
export const encodeParams = (params) => {
  const reParams = JSON.parse(JSON.stringify(params).replace(/\x20/g, encodeURIComponent(" ")));
  return Object.keys(reParams).reduce(
    (cur, pre, idx, arr) => (idx === arr.length - 1
      ? `${cur}${pre}=${reParams[pre]}`
      : `${cur}${pre}=${reParams[pre]}` + "&"),
    "",
  );
};

// 获取 userInfo
export const getUserInfo = () => {
  if (
    localStorage.getItem("userInfo")
    && localStorage.getItem("userInfo") !== "undefind"
  ) {
    return JSON.parse(localStorage.getItem("userInfo"));
  }
  window.location.href = "/#/login";
  localStorage.removeItem("userInfo");
  return null;
};

// 加密
export const encryption = async (password) => {
  const { code, data } = await getPublicKey();
  if (code === 0 && data) {
    const jse = new JSEncrypt();
    jse.setPublicKey(data);
    const encrypted = jse.encrypt(password);
    return encrypted;
  }
  throw new Error("公钥获取失败");
};

// 求和
export const reduceSum = (arr, key) => arr?.reduce((cur, pre) => cur + pre[key], 0);
