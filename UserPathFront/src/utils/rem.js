/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
export function setRem() {
  // 定义设计图的尺寸 1920
  const designSize = 1940;
  // 获取 html 元素
  const html = document.documentElement;
  // 定义窗口的宽度
  const clientW = html.clientWidth;
  // html 的fontsize 大小
  let htmlRem = clientW / designSize;
  if (clientW >= 1400) {
    html.style.fontSize = `${htmlRem * 100}px`;
  } else {
    htmlRem = 1400 / 1940;
    html.style.fontSize = `${htmlRem * 100}px`;
    html.style.minWidth = "1400px";
  }
}

setRem();
window.onresize = function () {
  setRem();
};
