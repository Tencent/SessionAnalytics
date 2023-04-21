/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import {
  getSessionCategoryDistinctEntity, // 大类分析去重
  getSessionCategoryEntity, // 大类分析不去重
  getSessionSubcategoryDistinctEntity, // 小类分析去重
  getSessionSubcategoryEntity, // 小类分析不去重
  getSessionEventDistinctEntity, // 页面分析去重
  getSessionEventEntity, // 页面分析不去重
  saveRecentAcessRecord, // 插入记录
  getFunnelSessionCategoryList1, // 漏斗图大类筛选_(不去重)层级1
  getFunnelSessionCategoryList2, // 获取漏斗图大类筛选层级2
  getFunnelSessionCategoryList3, // 获取漏斗图大类筛选层级3
  getFunnelSessionCategoryList4, // 获取漏斗图大类筛选层级4
  getFunnelSessionSubcategoryList1, // 漏斗图小类筛选_(不去重)层级1
  getFunnelSessionSubcategoryList2, // 漏斗图小类筛选_(不去重)层级2
  getFunnelSessionSubcategoryList3, // 漏斗图小类筛选_(不去重)层级3
  getFunnelSessionSubcategoryList4, // 漏斗图小类筛选_(不去重)层级4
  getFunnelSessionEventList1, // 漏斗图页面筛选_(不去重)层级1
  getFunnelSessionEventList2, // 漏斗图页面筛选_(不去重)层级2
  getFunnelSessionEventList3, // 漏斗图页面筛选_(不去重)层级3
  getFunnelSessionEventList4, // 漏斗图页面筛选_(不去重)层级4
  getFunnelSessionCategoryDistinctEntity,
  getFunnelSessionCategoryEntity,
  getFunnelSessionSubcategoryDistinctEntity,
  getFunnelSessionSubcategoryEntity,
  getFunnelSessionEventDistinctEntity,
  getFunnelSessionEventEntity,
  getChordSessionCategoryDistinctEntity,
  getChordSessionCategoryEntity,
  getChordSessionSubcategoryDistinctEntity,
  getChordSessionSubcategoryEntity,
  getChordSessionEventDistinctEntity,
  getChordSessionEventEntity,
  getTreeSessionCategoryDistinctEntity,
  getTreeSessionCategoryEntity,
  getTreeSessionSubcategoryDistinctEntity,
  getTreeSessionSubcategoryEntity,
  getTreeSessionEventDistinctEntity,
  getTreeSessionEventEntity,
  getSessionDimCategoryDetailEntity,
  getSessionDimCategoryDistinctEntity,
  getSessionDimSubCategoryDetailEntity,
  getSessionDimSubCategoryDistinctEntity,
  getSessionDimEventDetailEntity,
  getSessionDimEventDistinctEntity,
  getSessionFunnelDimCategoryDetailEntity,
  getSessionFunnelDimCategoryDistinctEntity,
  getSessionFunnelDimSubcategoryDetailEntity,
  getSessionFunnelDimSubcategoryDistinctEntity,
  getSessionFunnelDimEventDetailEntity,
  getSessionFunnelDimEventDistinctEntity,
  getSessionChordDimCategoryDetailEntity,
  getSessionChordDimCategoryDistinctEntity,
  getSessionChordDimSubcategoryDetailEntity,
  getSessionChordDimSubcategoryDistinctEntity,
  getSessionChordDimEventDetailEntity,
  getSessionChordDimEventDistinctEntity,
} from "@/server/path";

import {
  getSessionLogAgeList, // 获取session年龄段
  getSessionLogSexList, // 获取session性别
  getSessionLogProvinceList, // 获取session省份
  getSessionLogChannelList, // 获取session渠道
  getSessionLogAgeUserCntTrend, // session 总览查询(总用户数 趋势)分年龄段
  getSessionLogAgeSessionCntTrend,
  getSessionLogAgePvCntTrend,
  getSessionLogSexUserCntTrend,
  getSessionLogSexSessionCntTrend,
  getSessionLogSexPvCntTrend,
  getSessionLogProvinceUserCntTrend,
  getSessionLogProvinceSessionCntTrend,
  getSessionLogProvincePvCntTrend,
  getSessionLogChannelUserCntTrend,
  getSessionLogChannelSessionCntTrend,
  getSessionLogChannelPvCntTrend,
} from "@/server/trend";

import {
  getLogSessionAgeDistribute,
  getLogPVAgeDistribute,
  getLogSessionSexDistribute,
  getLogPVSexDistribute,
  getLogSessionProvinceDistribute,
  getLogPVProvinceDistribute,
  getLogSessionChannelDistribute,
  getLogPVChannelDistribute,
} from "@/server/statistics";

// 桑基图 tab对应的icon
import tabs_icon1 from "@/images/tabs_icon1.svg";
import tabs_icon2 from "@/images/tabs_icon2.svg";
import tabs_icon3 from "@/images/tabs_icon3.png";
import tabs_icon4 from "@/images/tabs_icon4.png";
import tabs_icon5 from "@/images/tabs_icon5.png";

import tabr_icon1 from "@/images/tabr_icon1.png";
import tabr_icon2 from "@/images/tabr_icon2.png";
import tabr_icon3 from "@/images/tabr_icon3.png";
import tabr_icon4 from "@/images/tabr_icon4.png";
import tabr_icon5 from "@/images/tabr_icon5.png";
import tabr_icon6 from "@/images/tabr_icon6.png";

import store from "@/store";

// 桑基图与树图公共的参数
export const commParams = [
  "from_layer",
  "to_layer",
  "f_session_num_start",
  "f_session_num_end",
  "f_pv_num_start",
  "f_pv_num_end",
  "f_user_num_start",
  "f_user_num_end",
];

/**
 * @params groupData 公共枚举
 * label 名称
 * value 切换时的值
 * lkey 中英文转换的key
 * sankeyApi 桑基图api
 * treeApi 树图key
 * extra 额外
 * firstApi 层级1api
 * secondApi 层级2api
 * threeApi 层级3api
 * fourApi 层级4api
 * dataCode data对应key值
 * funnelApi 漏斗图api
 * chordApi 和旋图api
 * key key值
 * enKey enkey值
 * chordkey 和旋图
 * chordlabelkey 和旋图label
 * icon tab对应icon
 * modalSankeyApi 桑基图树图查看分析接口
 * modalFunnelApi 漏斗图查看分析接口
 * modalChordApi  和旋图查看分析接口
 */
export const groupData = [
  {
    label: "大类流量分析（页面不去重）",
    value: 1,
    lkey: "category_not_repeating",
    sankeyApi: getSessionCategoryEntity,
    treeApi: getTreeSessionCategoryEntity,
    extra: ["f_category_from", "f_category"],
    firstApi: getFunnelSessionCategoryList1,
    secondApi: getFunnelSessionCategoryList2,
    threeApi: getFunnelSessionCategoryList3,
    fourApi: getFunnelSessionCategoryList4,
    dataCode: "f_category_from",
    funnelApi: getFunnelSessionCategoryEntity,
    chordApi: getChordSessionCategoryEntity,
    key: "category",
    enKey: "scene",
    chordkey: "category_from",
    chordlabelkey: "category_classpath_hierarchy",
    icon: tabr_icon2,
    modalSankeyApi: getSessionDimCategoryDetailEntity,
    modalFunnelApi: getSessionFunnelDimCategoryDetailEntity,
    modalChordApi: getSessionChordDimCategoryDetailEntity,
  },
  {
    label: "大类流量分析（页面去重）",
    value: 2,
    lkey: "category_repeat",
    sankeyApi: getSessionCategoryDistinctEntity,
    treeApi: getTreeSessionCategoryDistinctEntity,
    extra: ["f_category_from", "f_category"],
    firstApi: getFunnelSessionCategoryList1,
    secondApi: getFunnelSessionCategoryList2,
    threeApi: getFunnelSessionCategoryList3,
    fourApi: getFunnelSessionCategoryList4,
    funnelApi: getFunnelSessionCategoryDistinctEntity,
    chordApi: getChordSessionCategoryDistinctEntity,
    dataCode: "f_category_from",
    key: "category",
    enKey: "scene",
    chordkey: "category_from",
    chordlabelkey: "category_classpath_hierarchy",
    icon: tabr_icon1,
    modalSankeyApi: getSessionDimCategoryDistinctEntity,
    modalFunnelApi: getSessionFunnelDimCategoryDistinctEntity,
    modalChordApi: getSessionChordDimCategoryDistinctEntity,
  },
  {
    label: "小类流量分析（页面不去重）",
    value: 3,
    lkey: "subcategory_not_repeat",
    sankeyApi: getSessionSubcategoryEntity,
    treeApi: getTreeSessionSubcategoryEntity,
    extra: [
      "f_category_from",
      "f_category",
      "f_subcategory_from",
      "f_subcategory",
    ],
    firstApi: getFunnelSessionSubcategoryList1,
    secondApi: getFunnelSessionSubcategoryList2,
    threeApi: getFunnelSessionSubcategoryList3,
    fourApi: getFunnelSessionSubcategoryList4,
    funnelApi: getFunnelSessionSubcategoryEntity,
    chordApi: getChordSessionSubcategoryEntity,
    dataCode: "f_subcategory_from",
    key: "subcategory",
    enKey: "type",
    chordkey: "subcategory_from",
    chordlabelkey: "subcategor_classpath_hierarchy",
    icon: tabr_icon4,
    modalSankeyApi: getSessionDimSubCategoryDetailEntity,
    modalFunnelApi: getSessionFunnelDimSubcategoryDetailEntity,
    modalChordApi: getSessionChordDimSubcategoryDetailEntity,
  },
  {
    label: "小类流量分析（页面去重）",
    value: 4,
    lkey: "subcategory_repeat",
    sankeyApi: getSessionSubcategoryDistinctEntity,
    treeApi: getTreeSessionSubcategoryDistinctEntity,
    extra: [
      "f_category_from",
      "f_category",
      "f_subcategory_from",
      "f_subcategory",
    ],
    firstApi: getFunnelSessionSubcategoryList1,
    secondApi: getFunnelSessionSubcategoryList2,
    threeApi: getFunnelSessionSubcategoryList3,
    fourApi: getFunnelSessionSubcategoryList4,
    funnelApi: getFunnelSessionSubcategoryDistinctEntity,
    chordApi: getChordSessionSubcategoryDistinctEntity,
    dataCode: "f_subcategory_from",
    key: "subcategory",
    enKey: "type",
    chordkey: "subcategory_from",
    chordlabelkey: "subcategor_classpath_hierarchy",
    icon: tabr_icon3,
    modalSankeyApi: getSessionDimSubCategoryDistinctEntity,
    modalFunnelApi: getSessionFunnelDimSubcategoryDistinctEntity,
    modalChordApi: getSessionChordDimSubcategoryDistinctEntity,
  },
  {
    label: "页面流量分析（页面不去重）",
    value: 5,
    lkey: "event_not_repeat",
    sankeyApi: getSessionEventEntity,
    treeApi: getTreeSessionEventEntity,
    extra: [
      "f_category_from",
      "f_category",
      "f_subcategory_from",
      "f_subcategory",
      "f_event_from",
      "f_event",
    ],
    firstApi: getFunnelSessionEventList1,
    secondApi: getFunnelSessionEventList2,
    threeApi: getFunnelSessionEventList3,
    fourApi: getFunnelSessionEventList4,
    funnelApi: getFunnelSessionEventEntity,
    chordApi: getChordSessionEventEntity,
    dataCode: "f_event_from",
    key: "event",
    enKey: "event",
    chordkey: "event_from",
    chordlabelkey: "event_classpath_hierarchy",
    icon: tabr_icon6,
    modalSankeyApi: getSessionDimEventDetailEntity,
    modalFunnelApi: getSessionFunnelDimEventDetailEntity,
    modalChordApi: getSessionChordDimEventDetailEntity,
  },
  {
    label: "页面流量分析（页面去重）",
    value: 6,
    lkey: "event_repeat",
    sankeyApi: getSessionEventDistinctEntity,
    treeApi: getTreeSessionEventDistinctEntity,
    extra: [
      "f_category_from",
      "f_category",
      "f_subcategory_from",
      "f_subcategory",
      "f_event_from",
      "f_event",
    ],
    firstApi: getFunnelSessionEventList1,
    secondApi: getFunnelSessionEventList2,
    threeApi: getFunnelSessionEventList3,
    fourApi: getFunnelSessionEventList4,
    funnelApi: getFunnelSessionEventDistinctEntity,
    chordApi: getChordSessionEventDistinctEntity,
    dataCode: "f_event_from",
    key: "event",
    enKey: "event",
    chordkey: "event_from",
    chordlabelkey: "event_classpath_hierarchy",
    icon: tabr_icon5,
    modalSankeyApi: getSessionDimEventDistinctEntity,
    modalFunnelApi: getSessionFunnelDimEventDistinctEntity,
    modalChordApi: getSessionChordDimEventDistinctEntity,
  },
];

// groupData tab 切换时找item
export const findFetchApi = event => groupData.find(item => item.value === event);

// 通过key找value
export const getKeyTovalue = (labelsource, labelTarget, value) => {
  const item = groupData.find(i => i[labelsource] === value);
  return item[labelTarget];
};

// 筛选接口上传字段
export const filterDataset = (data, event) => {
  const d = findFetchApi(event)?.extra ?? [];
  return commParams
    .concat(d)
    .reduce((cur, pre) => ({ ...cur, [pre]: data[pre] ?? "" }), {});
};

// 筛选接口上传字段 数组拼接字符串
export const filterParams = (data, event, flag) => {
  const d = findFetchApi(event)?.extra ?? [];
  return (flag ? d : commParams.concat(d)).reduce((cur, pre) => {
    if (Array.isArray(data?.[pre])) {
      if (data?.[pre]?.length < 5) {
        return {
          ...cur,
          [pre]: data[pre]
            .concat(new Array(5 - data?.[pre]?.length).fill(" "))
            ?.join(","),
        };
      }
      return {
        ...cur,
        [pre]: data?.[pre]?.join(","),
      };
    }
    return { ...cur, [pre]: data?.[pre] };
  }, {});
};

// 过滤桑基图树图返回的数据
export const filterEchartData = (data = [], event) => {
  let linksdata = [];
  let datas = [];
  switch (event) {
    // 大类
    case 1:
    case 2:
      linksdata = data
        .filter(i => i.f_category_from !== i.f_category_to)
        .map(item => ({
          source: item.f_category_from,
          target: item.f_category_to,
          value: item.category_session,
          pv: item.category_pv,
          user: item.category_user,
        }));
      datas = [
        ...new Set(data.reduce(
          (pre, cur) => [...pre, cur.f_category_from, cur.f_category_to],
          [],
        )),
      ].map(t => ({
        key: t,
        value: t,
      }));
      return { linksdata, datas };
    // 小类
    case 3:
    case 4:
      linksdata = data
        .filter(i => i.f_subcategory_from !== i.f_subcategory_to)
        .map(item => ({
          source: item.f_subcategory_from,
          target: item.f_subcategory_to,
          value: item.subcategory_session,
          pv: item.subcategory_pv,
          user: item.subcategory_user,
        }));
      datas = data.reduce((pre, cur) => {
        let arr = pre;
        const keys = pre?.map(i => i?.value);
        if (!keys?.includes(cur.f_subcategory_from)) {
          arr = [
            ...arr,
            {
              key: cur.f_category_from,
              value: cur.f_subcategory_from,
            },
          ];
        }
        if (!keys?.includes(cur.f_subcategory_to)) {
          arr = [
            ...arr,
            {
              key: cur.f_category_to,
              value: cur.f_subcategory_to,
            },
          ];
        }
        return arr;
      }, []);
      return { linksdata, datas };
    // 页面
    case 5:
    case 6:
      linksdata = data
        .filter(i => i.f_event_from !== i.f_event_to)
        .map(item => ({
          source: item.f_event_from,
          target: item.f_event_to,
          value: item.event_session,
          pv: item.event_pv,
          user: item.event_user,
        }));
      datas = data.reduce((pre, cur) => {
        let arr = pre;
        const keys = pre.map(i => i.value);
        if (!keys.includes(cur.f_event_from)) {
          arr = [
            ...arr,
            {
              key: cur.f_category_from,
              value: cur.f_event_from,
            },
          ];
        }

        if (!keys.includes(cur.f_event_to)) {
          arr = [
            ...arr,
            {
              key: cur.f_category_to,
              value: cur.f_event_to,
            },
          ];
        }
        return arr;
      }, []);
      return { linksdata, datas };
    default:
      return [];
  }
};

// 过滤漏斗图返回的数据
export const filterfunEchartData = (i18n, data = [], event) => {
  switch (event) {
    // 大类
    case 1:
    case 2:
      return data.map(item => ({
        name: item.f_category,
        value: item.category_session,
        pv: item.category_pv,
        user: item.category_user,
      }));
    // 小类
    case 3:
    case 4:
      return data.map(item => ({
        name: item.f_subcategory,
        value: item.subcategory_session,
        pv: item.subcategory_pv,
        user: item.subcategory_user,
      }));
    // 页面
    case 5:
    case 6:
      return data.map(item => ({
        name: item.f_event,
        value: item.event_session,
        pv: item.event_pv,
        user: item.event_user,
      }));
    default:
      return [];
  }
};

// 过滤和旋图返回的数据
export const filterchordEachrtData = (data = [], event) => {
  switch (event) {
    case 1:
    case 2:
      return data.map(item => ({
        source: item.category_from,
        target: item.category_to,
        value: item.category_session,
        pv: item.category_pv,
        user: item.category_user,
      }));
    case 3:
    case 4:
      return data.map(item => ({
        source: item.subcategory_from,
        target: item.subcategory_to,
        value: item.subcategory_session,
        pv: item.subcategory_pv,
        user: item.subcategory_user,
      }));
    case 5:
    case 6:
      return data.map(item => ({
        source: item.event_from,
        target: item.event_to,
        value: item.event_session,
        pv: item.event_pv,
        user: item.event_user,
      }));
    default:
      return [];
  }
};

// 日志上传
export const saveAcessRecord = async (params) => {
  const user =  store?.state?.user;
  if (user?.username && user?.uploadName) {
    await saveRecentAcessRecord({
      ...params,
      f_upload_name: user?.uploadName,
      f_admin_user: user?.isadmin ? user?.userInfo?.username : user?.username,
      f_upload_user: user?.username,
    });
  }
};

// 和旋图接口字段处理
export const initfunnelValue = (data, key) => {
  const d = [
    "f_upload_user",
    "f_upload_name",
    `f_${key}_from1`,
    `f_${key}_from2`,
    `f_${key}_from3`,
    `f_${key}_from4`,
  ];
  return d.reduce(
    (cur, pre) => (d.includes(pre) ? { ...cur, [pre]: data[pre] } : { ...cur, [pre]: "" }),
    {},
  );
};

// 初始换from字段
export const initFrom = (num) => {
  const d = ["f_from_1", "f_from_2", "f_from_3", "f_from_4"];
  return d.reduce(
    (cur, pre, idx) => (num <= idx ? { ...cur, [pre]: "" } : cur),
    {},
  );
};

// label中英文切换
export const labelCnAndEn = (i18n, t, event) => {
  const enKey = getKeyTovalue("value", "enKey", event);
  if (i18n.language === "en") {
    return [
      `First Layer(${t(enKey)})`,
      `Second Layer(${t(enKey)})`,
      `Three Layer(${t(enKey)})`,
      `Four Layer(${t(enKey)})`,
    ];
  }
  return [
    `${t(enKey)}路径第一层`,
    `${t(enKey)}路径第二层`,
    `${t(enKey)}路径第三层`,
    `${t(enKey)}路径第四层`,
  ];
};

/**
 * @api 统计分析
 * lkey 中英文切换key
 * value tab切换值
 * icon 图标
 * optionApi 右侧多选option api
 * userApi 用户趋势图api
 * sessionApi session趋势图api
 * pvApi pv趋势图api
 * par 数据key
 * optionKey option key
 * paramsKey  params key
 * sessionTableApi  session table api
 * pvTableApi  pv table api
 * title table title
 */
export const analysisOptions = [
  {
    lkey: "overview",
    value: 1,
    icon: tabs_icon1,
  },
  {
    lkey: "age_distribution",
    value: 2,
    optionApi: getSessionLogAgeList,
    userApi: getSessionLogAgeUserCntTrend,
    sessionApi: getSessionLogAgeSessionCntTrend,
    pvApi: getSessionLogAgePvCntTrend,
    par: "age",
    optionKey: "age",
    paramsKey: "f_age",
    sessionTableApi: getLogSessionAgeDistribute,
    pvTableApi: getLogPVAgeDistribute,
    title: "f_age",
    icon: tabs_icon2,
  },
  {
    lkey: "sex_distribution",
    value: 3,
    optionApi: getSessionLogSexList,
    userApi: getSessionLogSexUserCntTrend,
    sessionApi: getSessionLogSexSessionCntTrend,
    pvApi: getSessionLogSexPvCntTrend,
    par: "f_sex",
    optionKey: "f_sex",
    paramsKey: "f_sex",
    sessionTableApi: getLogSessionSexDistribute,
    pvTableApi: getLogPVSexDistribute,
    title: "f_sex",
    icon: tabs_icon3,
  },
  {
    lkey: "provinces_distribution",
    value: 4,
    optionApi: getSessionLogProvinceList,
    userApi: getSessionLogProvinceUserCntTrend,
    sessionApi: getSessionLogProvinceSessionCntTrend,
    pvApi: getSessionLogProvincePvCntTrend,
    par: "f_province",
    optionKey: "f_province",
    paramsKey: "f_province",
    sessionTableApi: getLogSessionProvinceDistribute,
    pvTableApi: getLogPVProvinceDistribute,
    title: "f_province",
    icon: tabs_icon4,
  },
  {
    lkey: "channel_distribution",
    value: 5,
    optionApi: getSessionLogChannelList,
    userApi: getSessionLogChannelUserCntTrend,
    sessionApi: getSessionLogChannelSessionCntTrend,
    pvApi: getSessionLogChannelPvCntTrend,
    par: "f_channel",
    optionKey: "f_channel",
    paramsKey: "f_channel",
    sessionTableApi: getLogSessionChannelDistribute,
    pvTableApi: getLogPVChannelDistribute,
    title: "f_channel",
    icon: tabs_icon5,
  },
];

// 通过event 找analysisOptions item
export const findAnalysisApi = event => analysisOptions.find(item => item.value === event);

// 通过key找value
export const getAnalysisKeyTovalue = (labelsource, labelTarget, value) => {
  const item = analysisOptions.find(i => i[labelsource] === value);
  return item[labelTarget];
};

// 深层遍历
export const depend = params => Object.keys(params)
  .sort()
  .reduce((res, item) => [...res, params[item]], []);
