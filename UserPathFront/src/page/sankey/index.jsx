/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect, useMemo } from "react";
import { Form, Select, Row, Col, Table as AntdTable } from "antd";
import ReactECharts from "echarts-for-react";
import { useStore } from "@royjs/core";
import {
  groupData,
  findFetchApi,
  filterParams,
  saveAcessRecord,
  getKeyTovalue,
  filterEchartData,
} from "@/utils/datamaps";
import {
  rgb,
  hierarchyArrToLv,
  hierarchyToLv,
  getPagenation,
  reduceSum,
} from "@/utils";
import {
  getSessionEventCategoryList, // 大类筛选
  getSessionEventSubCategoryList, // 小类筛选
  getSessionEventList, //  页面筛选
  getSessionEventCategorySample, // 不含层级筛选 大类
  getSessionEventSubCategorySample, // 不含层级筛选 小类
  getSessionEventSample, // 不含层级筛选 页面
  getCategoryColor, // 获取颜色
  saveCategoryColor, // 插入颜色
} from "@/server/path";
import dayjs from "dayjs";
import { withRouter } from "react-router-dom";
import { useTranslation } from "react-i18next";
import {
  Sample,
  Tabs,
  Section,
  RangeInput,
  EchratMain,
  Table,
  EchartCard,
  DataContent,
} from "@/components/compontents";
import style from "@/style/comm.less";

const Summary = ({ data = [], event }) => {
  const { t } = useTranslation();
  switch (event) {
    case 1:
    case 2:
      return (
        <AntdTable.Summary.Row>
          <AntdTable.Summary.Cell index={0}>
            {t("total_num")}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={1} />
          <AntdTable.Summary.Cell index={2}>
            {reduceSum(data, "category_session")?.toLocaleString()}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={3}>100%</AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={3}>
            {reduceSum(data, "category_pv")?.toLocaleString()}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={3}>100%</AntdTable.Summary.Cell>
        </AntdTable.Summary.Row>
      );
    case 3:
    case 4:
      return (
        <AntdTable.Summary.Row>
          <AntdTable.Summary.Cell index={0}>
            {t("total_num")}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={1} />
          <AntdTable.Summary.Cell index={2} />
          <AntdTable.Summary.Cell index={3} />
          <AntdTable.Summary.Cell index={4}>
            {reduceSum(data, "subcategory_session")?.toLocaleString()}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={5}>100%</AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={6}>
            {reduceSum(data, "subcategory_pv")?.toLocaleString()}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={7}>100%</AntdTable.Summary.Cell>
        </AntdTable.Summary.Row>
      );
    case 5:
    case 6:
      return (
        <AntdTable.Summary.Row>
          <AntdTable.Summary.Cell index={0}>
            {t("total_num")}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={1} />
          <AntdTable.Summary.Cell index={2} />
          <AntdTable.Summary.Cell index={3} />
          <AntdTable.Summary.Cell index={4} />
          <AntdTable.Summary.Cell index={5} />
          <AntdTable.Summary.Cell index={6}>
            {reduceSum(data, "event_session")?.toLocaleString()}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={7}>100%</AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={8}>
            {reduceSum(data, "event_pv")?.toLocaleString()}
          </AntdTable.Summary.Cell>
          <AntdTable.Summary.Cell index={9}>100%</AntdTable.Summary.Cell>
        </AntdTable.Summary.Row>
      );
  }
};

const Screens = (props) => {
  const {
    location: { query },
  } = props;

  const initPagination = {
    page: 0,
    page_size: 15,
  };

  const { t, i18n } = useTranslation();
  const { username, uploadName } = useStore(state => state.user);
  const [form] = Form.useForm();
  const [oldOptions, setOldOptions] = useState({
    categoryList: [],
    subcategory: [],
    eventPage: [],
  });
  const [newOptions, setNewOptions] = useState({
    categoryList: [],
    subcategory: [],
    eventPage: [],
  });
  const [tierSubCategory, setTierSubCategory] = useState([]);
  const [tierPage, setTierPage] = useState([]); // 事件
  const [sample, setSample] = useState([]); // 示例数据
  const [event, setEvent] = useState(1);
  const [colorList, setColorList] = useState([]); // 示例数据
  const [echartData, setEchartData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalparams, setModalParams] = useState({});
  const [open, setOpen] = useState(true);
  const [chartType, setChartType] = useState(1);
  const [tablePage, setTablePage] = useState(initPagination);
  const [active, setActive] = useState(false);
  const [echartHeight, setEchartHeight] = useState("6rem");

  // 初始数据
  const initialValues = {
    from_layer: 1,
    to_layer: 4,
    f_session_num_start: 100,
    f_session_num_end: 999999999,
    f_pv_num_start: 100,
    f_pv_num_end: 999999999,
    f_user_num_start: 100,
    f_user_num_end: 999999999,
    f_category_from: [],
    f_subcategory_from: [],
    f_event_from: [],
    f_category: "",
    f_subcategory: "",
    f_event: "",
  };

  // 获取公共数据
  const getCommMessage = () => {
    const arg = {
      f_upload_user: username,
      f_upload_name: uploadName,
    };
    getCategoryList(arg);
    getEventSubCategory(arg);
    getEventPage(arg);
    getSample(arg);
    getSubcategory(arg);
    getSessionEventPage(arg);
  };

  // 拆入日志
  const postVisitLog = (event) => {
    saveAcessRecord({
      f_analysis_name_first: "sankey",
      f_analysis_name_second: getKeyTovalue("value", "lkey", event),
      f_access_time: dayjs().format("YYYY-MM-DD HH:mm:ss"),
    });
  };

  // 大类筛选
  const getCategoryList = async (arg) => {
    const { code, data } = await getSessionEventCategoryList(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      const list = data?.list?.map(item => ({
        label: item.f_category_from,
        value: item.f_category_from,
      }));
      setOldOptions(s => ({ ...s, categoryList: list }));
      setNewOptions(s => ({ ...s, categoryList: list }));
    }
  };

  // 小类路径筛选
  const getSubcategory = async (arg) => {
    const { code, data } = await getSessionEventSubCategoryList(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      const list = data?.list?.map(item => ({
        label: item.f_subcategory_from,
        value: item.f_subcategory_from,
      }));
      setOldOptions(s => ({ ...s, subcategory: list }));
      setNewOptions(s => ({ ...s, subcategory: list }));
    }
  };

  // 页面路径筛选
  const getEventPage = async (arg) => {
    const { code, data } = await getSessionEventList(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      const list = data?.list?.map(item => ({
        label: item.f_event_from,
        value: item.f_event_from,
      }));
      setOldOptions(s => ({ ...s, eventPage: list }));
      setNewOptions(s => ({ ...s, eventPage: list }));
    }
  };

  // 小类无层级筛选
  const getEventSubCategory = async (arg) => {
    const { code, data } = await getSessionEventSubCategorySample(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      setTierSubCategory(data?.list?.map(item => ({
        label: item.subcategory,
        value: item.subcategory,
      })));
    } else {
      setTierSubCategory([]);
    }
  };

  // 页面无层级筛选
  const getSessionEventPage = async (arg) => {
    const { code, data } = await getSessionEventSample(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      setTierPage(data?.list?.map(item => ({
        label: item.event,
        value: item.event,
      })));
    } else {
      setTierPage([]);
    }
  };

  // 大类无层级筛选
  const getSample = async (arg) => {
    const { code, data } = await getSessionEventCategorySample(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      const {
        data: { list = [] },
      } = await getCategoryColor();
      const nonentity = data.list
        .filter(i => !list?.find(l => l.f_category === i.category))
        .map((m) => {
          const item = {
            color: rgb(),
            value: m.category,
          };
          if (m?.category) {
            saveCategoryColor({
              f_category: m?.category,
              f_color: rgb(),
            });
          }
          return item;
        });
      const exist = data.list.reduce((cur, pre) => {
        const item = list?.find(l => l.f_category === pre.category);
        return item
          ? [
            ...cur,
            {
              color: item.f_color,
              value: pre.category,
            },
          ]
          : cur;
      }, []);

      const all = list.reduce(
        (cur, pre) => [
          ...cur,
          {
            color: pre.f_color,
            value: pre.f_category,
          },
        ],
        [],
      );
      const defaultObj = [
        {
          color: rgb(128, 128, 128),
          value: "Unknown",
        },
        {
          color: rgb(192, 192, 192),
          value: "END",
        },
        {
          color: "red",
          value: "Life cycle",
        },
        {
          color: rgb(128, 128, 128),
          value: "未知",
        },
        {
          color: rgb(192, 192, 192),
          value: "结束",
        },
        {
          color: "red",
          value: "生命周期",
        },
      ];
      const defaultColors = defaultObj?.filter(j => ![...new Set(list?.map(i => i?.f_category))]?.includes(j?.value));
      if (defaultColors?.length) {
        defaultColors?.forEach((item) => {
          saveCategoryColor({
            f_category: item?.value,
            f_color: item?.color,
          });
        });
      }
      setColorList([...nonentity, ...all, ...exist, ...defaultColors]);
      setSample([...nonentity, ...exist]);
    } else {
      setSample([]);
    }
  };

  // 获取桑基图查询数据
  const getFetchApi = async (params, event) => {
    setLoading(true);
    const { code, data } = await findFetchApi(event).sankeyApi({
      ...params,
      f_upload_name: uploadName,
      f_upload_user: username,
    });
    if (code === 0 && data && data.list && data?.list?.length) {
      setEchartData(data.list);
    } else {
      setEchartData([]);
    }
    setLoading(false);
  };

  // evevt 变化
  const onEventChange = (ev) => {
    if (ev !== event) {
      setModalParams({});
      setEvent(ev);
      setEchartData([]);
      setNewOptions(oldOptions);
      if (uploadName) {
        const params = filterParams(initialValues, ev);
        form.setFieldsValue(initialValues);
        getFetchApi(params, ev);
        postVisitLog(ev);
      }
    }
  };

  // 首次进入查询
  useEffect(() => {
    setModalParams({});
    if (username) {
      let ev = event;
      if (query && Object.keys(query).length) {
        ev = getKeyTovalue("lkey", "value", query?.f_analysis_name_second);
        setEvent(ev);
      }
      if (uploadName) {
        form.setFieldsValue(initialValues);
        getCommMessage();
        getFetchApi(filterParams(initialValues, ev), ev);
        postVisitLog(ev);
      }
    }
  }, [uploadName]);

  useEffect(() => {
    document.getElementsByClassName("echarts-for-react")[0].oncontextmenu =      function () {
      	return false;
    };
  }, []);

  const truncatString = str => (str?.length > 25 ? `${str?.slice(0, 25)}...` : str);

  const option = useMemo(() => {
    if (chartType === 1) {
      const { linksdata = [], datas = [] } = filterEchartData(
        echartData,
        event,
      );
      const links = linksdata.map(item => ({
        ...item,
        source: item?.source,
        target: item?.target,
      }));
      const deptMax = {};
      const dataList = datas.map((item) => {
        const color = colorList?.find(i => item?.key?.split("_")[1] === i?.value);
        const depth = item?.value?.split("_")[0].replace("层级", "");
        if (depth in deptMax) {
          deptMax[depth] = deptMax[depth] + 1;
        } else {
          deptMax[depth] = 1;
        }
        return {
          name: item?.value,
          itemStyle: {
            color: color ? color?.color : rgb(),
          },
          depth: !isNaN(depth) ? Number(depth) : 0,
        };
      });
      const max = Object?.values(deptMax).sort((a, b) => b - a)?.[0];
      if (max && max > 8) {
        setEchartHeight(`${max + 1}rem`);
      } else {
        setEchartHeight("6rem");
      }
      return {
        tooltip: {
          trigger: "item",
          triggerOn: "mousemove",
          formatter: (params) => {
            const { data, dataType, value } = params;
            if (dataType === "node") {
              let formList = [];
              const isFirstPath = data?.name?.split("_")[0].replace("层级", "") == 1;
              const getSum = key => formList?.reduce((cur, pre) => cur + pre?.[key], 0);
              if (isFirstPath) {
                formList = links?.filter(i => i?.source === data?.name);
              } else {
                formList = links?.filter(i => i?.target === data?.name);
              }
              return `${
                hierarchyToLv(i18n, data?.name)
              }<br/>Session：${value?.toLocaleString()}<br/>PV：${getSum("pv")?.toLocaleString()}<br/>${t("user")}：${getSum("user")?.toLocaleString()}`;
            }
            return `${hierarchyToLv(i18n, data?.source)}<br/>${
              hierarchyToLv(i18n, data?.target)
            }<br/>Session：${data.value?.toLocaleString()}<br/>PV：${data.pv?.toLocaleString()}<br/>User：${data.user?.toLocaleString()}`;
          },
        },
        series: [
          {
            type: "sankey",
            top: 20,
            bottom: 20,
            data: dataList,
            links,
            emphasis: {
              focus: "adjacency",
            },
            label: {
              fontSize: 12,
              position: "inside",
              formatter: params => truncatString(hierarchyToLv(i18n, params?.name)),
            },
            lineStyle: {
              color: "source",
              curveness: 0.5,
            },
            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750,
          },
        ],
        animation: true,
      };
    }
  }, [chartType, colorList, echartData, i18n.language]);

  const onContextmenu = (info) => {
    const { dataType, data } = info;
    console.log("info", info);
    if (dataType === "node") {
      setModalParams({
        from: data?.name,
        type: "from",
      });
    }
    if (dataType === "edge") {
      setModalParams({
        from: data?.source,
        to: data.target,
        type: "to",
      });
    }
    !open && setOpen(true);
  };

  // 查询
  const onFinish = () => {
    if (uploadName) {
      const formValue = form.getFieldsValue();
      const params = filterParams(formValue, event);
      getFetchApi(params, event);
    }
  };

  const onSelect = (value, key) => {
    const includesItems = value?.map(i => i?.split("_")[0].replace(t("LV"), ""));
    const filterOption = oldOptions?.[key]?.reduce((cur, pre) => {
      const flag = includesItems?.includes(pre?.value?.split("_")[0].replace(t("LV"), ""));
      if (flag && !value?.includes(pre?.value)) {
        return cur;
      }
      return cur.concat(pre);
    }, []);
    setNewOptions(s => ({
      ...s,
      [key]: filterOption,
    }));
    onBlur();
  };

  const multipleRules = [
    () => ({
      validator(_, value) {
        if (value.length <= 5) {
          return Promise.resolve();
        }
        return Promise.reject(new Error(t("length_than_f")));
      },
    }),
  ];

  const chartTypeData = [
    {
      value: 1,
      lkey: "sankey",
    },
    {
      value: 2,
      lkey: "path_detail",
    },
  ];

  const sankeyColumns = useMemo(() => {
    switch (event) {
      case 1:
      case 2:
        return [
          {
            title: t("from"),
            dataIndex: "f_category_from",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("to"),
            dataIndex: "f_category_to",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: "Session",
            dataIndex: "category_session",
            render: t => t?.toLocaleString(),
            sorter: (a, b) => b?.category_session - a?.category_session,
          },
          {
            title: t("session_percent"),
            dataIndex: "session_percent",
            render: (_, r) => `${(
              (r?.category_session
                  / reduceSum(echartData, "category_session"))
                * 100
            ).toFixed(2)} %`,
          },
          {
            title: "PV",
            dataIndex: "category_pv",
            render: t => t?.toLocaleString(),
            sorter: (a, b) => b?.category_pv - a?.category_pv,
          },
          {
            title: t("pv_percent"),
            dataIndex: "pv_percent",
            render: (_, r) => `${(
              (r?.category_pv / reduceSum(echartData, "category_pv"))
                * 100
            ).toFixed(2)} %`,
          },
          {
            title: "User",
            dataIndex: "category_user",
            render: t => t?.toLocaleString(),
          },
        ];
      case 3:
      case 4:
        return [
          {
            title: t("scene_from"),
            dataIndex: "f_category_from",
            width: "15%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("scene_to"),
            dataIndex: "f_category_to",
            width: "15%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("from"),
            dataIndex: "f_subcategory_from",
            width: "15%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("to"),
            dataIndex: "f_subcategory_to",
            width: "15%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: "Session",
            dataIndex: "subcategory_session",
            width: "10%",
            sorter: (a, b) => b?.subcategory_session - a?.subcategory_session,
            render: t => t?.toLocaleString(),
          },
          {
            title: t("session_percent"),
            dataIndex: "session_percent",
            width: "10%",
            render: (_, r) => `${(
              (r?.subcategory_session
                  / reduceSum(echartData, "subcategory_session"))
                * 100
            ).toFixed(2)} %`,
          },
          {
            title: "PV",
            dataIndex: "subcategory_pv",
            width: "10%",
            sorter: (a, b) => b?.subcategory_pv - a?.subcategory_pv,
            render: t => t?.toLocaleString(),
          },
          {
            title: "PV Percent",
            dataIndex: "pv_percent",
            width: "10%",
            render: (_, r) => `${(
              (r?.subcategory_pv / reduceSum(echartData, "subcategory_pv"))
                * 100
            ).toFixed(2)} %`,
          },
          {
            title: "User",
            dataIndex: "subcategory_user",
            width: "5%",
            render: t => t?.toLocaleString(),
          },
        ];
      case 5:
      case 6:
        return [
          {
            title: t("scene_from"),
            dataIndex: "f_category_from",
            width: "10%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("scene_to"),
            dataIndex: "f_category_to",
            width: "10%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("type_from"),
            dataIndex: "f_subcategory_from",
            width: "10%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("type_to"),
            dataIndex: "f_subcategory_to",
            width: "10%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("from"),
            dataIndex: "f_event_from",
            width: "10%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: t("to"),
            dataIndex: "f_event_to",
            width: "10%",
            render: t => hierarchyToLv(i18n, t),
          },
          {
            title: "Session",
            dataIndex: "event_session",
            width: "10%",
            sorter: (a, b) => b?.event_session - a?.event_session,
            render: t => t?.toLocaleString(),
          },
          {
            title: t("session_percent"),
            dataIndex: "session_percent",
            width: "10%",
            render: (_, r) => `${(
              (r?.event_session / reduceSum(echartData, "event_session"))
                * 100
            ).toFixed(2)} %`,
          },
          {
            title: "PV",
            dataIndex: "event_pv",
            width: "10%",
            sorter: (a, b) => b?.event_pv - a?.event_pv,
            render: t => t?.toLocaleString(),
          },
          {
            title: "PV Percent",
            dataIndex: "pv_percent",
            width: "10%",
            render: (_, r) => `${(
              (r?.event_pv / reduceSum(echartData, "event_pv"))
                * 100
            ).toFixed(2)} %`,
          },
          {
            title: "User",
            dataIndex: "event_user",
            width: "5%",
            render: t => t?.toLocaleString(),
          },
        ];
    }
  }, [event, i18n.language, echartData]);

  const onChartType = (ty) => {
    setChartType(ty);
    if (ty === 2) {
      setTablePage(initPagination);
      setOpen(false);
      setModalParams({});
    }
    if (ty === 1) {
      setOpen(true);
    }
  };

  const normalFormItemLayout = {
    labelCol: {
      sm: { span: 8 },
    },
    wrapperCol: {
      sm: { span: 16 },
    },
  };

  const onBlur = () => {
    form.submit();
  };

  const dataSource = useMemo(
    () => getPagenation(echartData, tablePage.page, initPagination.page_size),
    [tablePage, echartData],
  );

  return (
    <Section>
      <Tabs data={groupData} event={event} onEventChange={onEventChange} />
      <div className={style.content}>
        <div className={style.conetnt_form}>
          <Form
            layout="horizontal"
            name="event"
            initialValues={initialValues}
            form={form}
            labelAlign="center"
            onFinish={onFinish}
          >
            <Row>
              <Col xl={6}>
                <RangeInput
                  label={t("path_layer")}
                  names={["from_layer", "to_layer"]}
                  {...normalFormItemLayout}
                  onBlur={onBlur}
                />
              </Col>
              <Col xl={6}>
                <RangeInput
                  label={t("sessions_threshold")}
                  names={["f_session_num_start", "f_session_num_end"]}
                  {...normalFormItemLayout}
                  onBlur={onBlur}
                />
              </Col>
              <Col xl={6}>
                <RangeInput
                  label={t("pv_threshold")}
                  names={["f_pv_num_start", "f_pv_num_end"]}
                  {...normalFormItemLayout}
                  onBlur={onBlur}
                />
              </Col>
              <Col xl={6}>
                <RangeInput
                  label={t("user_threshold")}
                  names={["f_user_num_start", "f_user_num_end"]}
                  {...normalFormItemLayout}
                  onBlur={onBlur}
                />
              </Col>
              <Col xl={event <= 2 ? 12 : 9}>
                <Form.Item
                  name="f_category_from"
                  label={t("category_classpath_path")}
                  rules={multipleRules}
                  labelCol={{ span: event <= 2 ? 6 : 8 }}
                  wrapperCol={{ span: event <= 2 ? 18 : 16 }}
                >
                  <Select
                    mode="multiple"
                    showSearch
                    options={hierarchyArrToLv(i18n, newOptions?.categoryList)}
                    onChange={value => onSelect(value, "categoryList")}
                    allowClear
                    showArrow={true}
                  />
                </Form.Item>
              </Col>
              <Col xl={6}>
                <Form.Item
                  name="f_category"
                  label={t("category_classpath_hierarchy")}
                  labelCol={{ span: 12 }}
                  wrapperCol={{ span: 12 }}
                >
                  <Select
                    showSearch
                    options={sample}
                    allowClear
                    onChange={onBlur}
                  />
                </Form.Item>
              </Col>
              {event > 2 ? (
                <Col xl={9}>
                  <Form.Item
                    name="f_subcategory_from"
                    label={t("subcategor_classpath_path")}
                    rules={multipleRules}
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                  >
                    <Select
                      mode="multiple"
                      showSearch
                      options={hierarchyArrToLv(i18n, newOptions?.subcategory)}
                      onChange={value => onSelect(value, "subcategory")}
                      allowClear
                      showArrow={true}
                    />
                  </Form.Item>
                </Col>
              ) : null}
              {event > 2 ? (
                <Col xl={6}>
                  <Form.Item
                    name="f_subcategory"
                    label={t("subcategor_classpath_hierarchy")}
                    labelCol={{ span: 12 }}
                    wrapperCol={{ span: 12 }}
                  >
                    <Select
                      showSearch
                      options={tierSubCategory}
                      allowClear
                      onChange={onBlur}
                    />
                  </Form.Item>
                </Col>
              ) : null}
              {event > 4 ? (
                <Col xl={9}>
                  <Form.Item
                    name="f_event_from"
                    label={t("event_classpath_path")}
                    rules={multipleRules}
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                  >
                    <Select
                      mode="multiple"
                      showSearch
                      options={hierarchyArrToLv(i18n, newOptions.eventPage)}
                      onChange={value => onSelect(value, "eventPage")}
                      allowClear
                      showArrow={true}
                    />
                  </Form.Item>
                </Col>
              ) : null}
              {event > 4 ? (
                <Col xl={6}>
                  <Form.Item
                    name="f_event"
                    label={t("event_classpath_hierarchy")}
                    labelCol={{ span: 12 }}
                    wrapperCol={{ span: 12 }}
                  >
                    <Select
                      showSearch
                      options={tierPage}
                      allowClear
                      onChange={onBlur}
                    />
                  </Form.Item>
                </Col>
              ) : null}
            </Row>
          </Form>
        </div>
        <DataContent
          event={event}
          from={filterParams(form.getFieldsValue(), event, true)}
          params={modalparams}
          onCancel={() => setOpen(s => !s)}
          open={open}
          apiKey="modalSankeyApi"
          resetParams={setModalParams}
          chartType={chartType}
        >
          <EchartCard
            setHeight={false}
            title={
              <Tabs
                data={chartTypeData}
                event={chartType}
                onEventChange={onChartType}
                bottom={true}
                padding={false}
              />
            }
          >
            {chartType === 1 ? (
              <>
                <Sample data={sample} open={open} />
                <div
                  className={style.echart_main}
                  style={{ height: event > 2 ? "5.4rem" : "6rem" }}
                >
                  <EchratMain
                    refresh={() => onFinish()}
                    handleCHange={state => setActive(state)}
                  >
                    <ReactECharts
                      option={option}
                      style={{
                        height:
                          active && echartHeight === "6rem"
                          	? "100%"
                          	: echartHeight,
                        width: "99.9%",
                      }}
                      showLoading={loading}
                      onEvents={{
                        contextmenu(info) {
                          !active && onContextmenu(info);
                        },
                        click(info) {
                          !active && onContextmenu(info);
                        },
                      }}
                    />
                  </EchratMain>
                </div>
              </>
            ) : (
              <Table
                rowKey="id"
                dataSource={dataSource}
                columns={sankeyColumns}
                sourceLength={echartData?.length}
                pagination={tablePage}
                onChange={(page) => {
                  setTablePage(s => ({
                    ...s,
                    page: page - 1,
                  }));
                }}
                summary={() => <Summary data={echartData} event={event} />}
              />
            )}
          </EchartCard>
        </DataContent>
      </div>
    </Section>
  );
};

export default React.memo(withRouter(Screens));
