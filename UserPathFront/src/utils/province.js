/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
// 颜色配置
// 随机颜色不够美观 采用枚举方式设置中国省份颜色
// 该颜色枚举重要用于趋势图统计分布
// 可自行修改
export const provinceColor = [
  { label: "北京市", color: "#F08080" },
  { label: "天津市", color: "#A9A9A9" },
  { label: "上海市", color: "#FA8072" },
  { label: "重庆市", color: "#FF6347" },
  { label: "河北省", color: "#FFA07A" },
  { label: "河南省", color: "#FFF5EE" },
  { label: "云南省", color: "#F4A460" },
  { label: "辽宁省", color: "#FFDAB9" },
  { label: "黑龙江省", color: "#FAF0E6" },
  { label: "湖南省", color: "#FFE4C4" },
  { label: "安徽省", color: "#fffed8" },
  { label: "山东省", color: "#D2B48C" },
  { label: "新疆维吾尔自治区", color: "#FFDEAD" },
  { label: "江苏省", color: "#FFA500" },
  { label: "浙江省", color: "#F5DEB3" },
  { label: "江西省", color: "#DAA520" },
  { label: "湖北省", color: "#FFD700" },
  { label: "广西壮族自治区", color: "#F0E68C" },
  { label: "甘肃省", color: "#EEE8AA" },
  { label: "山西省", color: "#FFFF00" },
  { label: "内蒙古自治区", color: "#7CFC00" },
  { label: "陕西省", color: "#00FA9A" },
  { label: "吉林省", color: "#40E0D0" },
  { label: "福建省", color: "#48D1CC" },
  { label: "贵州省", color: "#D4F2E7" },
  { label: "广东省", color: "#AFEEEE" },
  { label: "青海省", color: "#ADD8E6" },
  { label: "西藏西藏自治区", color: "#B0C4DE" },
  { label: "四川省", color: "#EE82EE" },
  { label: "宁夏回族自治区", color: "#D8BFD8" },
  { label: "海南省", color: "#FFC0CB" },
  { label: "台湾省", color: "#90EE90" },
  { label: "香港特别行政区", color: "#FFE4E1" },
  { label: "澳门特别行政区", color: "#FF7F50" },
];

// 该方法通过数据的省份名称寻找枚举中对应的身份颜色
export const getProvinceColor = name => provinceColor?.find(i => i?.label === name)?.color;
