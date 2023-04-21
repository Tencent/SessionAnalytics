/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect, useMemo } from "react";
import { Form, Select, Row, Col } from "antd";
import { useStore } from "@royjs/core";
import {
  groupData,
  findFetchApi,
  filterParams,
  saveAcessRecord,
  getKeyTovalue,
  filterEchartData,
} from "@/utils/datamaps";
import { hierarchyArrToLv, hierarchyToLv } from "@/utils";
import {
  getSessionEventCategoryList, // 大类筛选
  getSessionEventSubCategoryList, // 小类筛选
  getSessionEventList, //  页面筛选
  getSessionEventCategorySample, // 不含层级筛选 大类
  getSessionEventSubCategorySample, // 不含层级筛选 小类
  getSessionEventSample, // 不含层级筛选 页面
} from "@/server/path";
import dayjs from "dayjs";
import { withRouter } from "react-router-dom";
import { useTranslation } from "react-i18next";
import {
  Tabs,
  Section,
  RangeInput,
  EchartCard,
  EchratMain,
  DataContent,
} from "@/components/compontents";
import ReactECharts from "echarts-for-react";
import style from "@/style/comm.less";

const Screens = (props) => {
  const {
    location: { query },
  } = props;

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
  const [echartData, setEchartData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalparams, setModalParams] = useState({});
  const [open, setOpen] = useState(true);
  const [echartHeight, setEchartHeight] = useState("6.0rem");

  // 初始数据
  const initialValues = {
    from_layer: 1,
    to_layer: 3,
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
      f_analysis_name_first: "tree",
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
      setSample(data?.list?.map(item => ({
        label: item.category,
        value: item.category,
      })));
    } else {
      setSample([]);
    }
  };

  // 获取树图查询数据
  const getFetchApi = async (params, event) => {
    setLoading(true);
    const { code, data } = await findFetchApi(event).treeApi({
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
        form.setFieldValue(initialValues);
        getCommMessage();
        getFetchApi(filterParams(initialValues, ev), ev);
        postVisitLog(ev);
      }
      document.getElementsByClassName("echarts-for-react")[0].oncontextmenu =        () => false;
    }
  }, [uploadName]);

  const option = useMemo(() => {
    const { linksdata = [] } = filterEchartData(echartData, event);
    const links = linksdata.map(item => ({
      ...item,
      source: item?.source,
      target: item?.target,
    }));

    const height = linksdata?.reduce(
      (cur, pre) => (pre?.target?.split("_")[0]?.includes(3) ? cur + 1 : cur),
      0,
    );

    if (height > 20) {
      setEchartHeight(`${height * 0.2}rem`);
    } else {
      setEchartHeight("6.0rem");
    }

    function getchilds(target, array) {
      const childs = [];
      for (const arr of array) {
        if (
          arr.source === target
          && !childs.find(i => i.name === arr.target)
        ) {
          // 循环获取子节点
          childs.push({
            name: arr.target,
            value: arr.value,
          });
        }
      }
      for (const child of childs) {
        // 获取子节点的子节点
        const childscopy = getchilds(child.name, array); // 递归获取子节点
        if (childscopy.length > 0) {
          child.children = childscopy;
        }
      }
      return childs;
    }

    function generateOptions(params) {
      let result = [];
      for (const param of params) {
        const list = params.map(i => i.target).includes(param.source);
        if (!list) {
          const idx = result.findIndex(i => i.name === param.source);
          if (idx !== -1) {
            result[idx] = {
              ...result[idx],
              children: [
                ...result[idx].children,
                {
                  name: param.target,
                  value: param.value,
                  children: getchilds(param.target, params),
                },
              ],
            };
          } else {
            const parent = {
              name: param.source,
              children: [
                {
                  name: param.target,
                  value: param.value,
                  children: getchilds(param.target, params),
                },
              ],
            };
            result = [...result, parent];
          }
        }
      }
      return result;
    }

    function replaceName(arr) {
      arr.forEach((item) => {
        item.name = item.name.split("_&_")[0];
        if (item.children) {
          replaceName(item.children);
        }
      });
      return arr;
    }

    const data = {
      name: t("start"),
      children: replaceName(generateOptions(links)),
    };

    return {
      tooltip: {
        trigger: "item",
        triggerOn: "mousemove",
        formatter: (params) => {
          const index = params?.treeAncestors?.findIndex(i => i.name === params.name);
          return `${hierarchyToLv(i18n, params?.treeAncestors[index - 1]?.name)} -> ${
            hierarchyToLv(i18n, params.name)
          }：${params.value ? params?.value?.toLocaleString() : "-"}`;
        },
      },
      series: [
        {
          type: "tree",
          data: [data],
          top: "1%",
          left: "7%",
          bottom: "1%",
          right: "20%",
          symbolSize: 7,
          label: {
            position: "left",
            verticalAlign: "middle",
            align: "right",
            fontSize: "0.12rem",
            formatter: params => hierarchyToLv(i18n, params?.name),
          },
          initialTreeDepth: 3,
          leaves: {
            label: {
              position: "right",
              verticalAlign: "middle",
              align: "left",
            },
          },
          emphasis: {
            focus: "descendant",
          },
        },
      ],
      animation: true,
    };
  }, [echartData, i18n.language]);

  // 查询
  const onFinish = () => {
    if (uploadName) {
      const formValue = form.getFieldsValue();
      const params = filterParams(formValue, event);
      getFetchApi(params, event);
    }
  };

  const onSelect = (value, key) => {
    const includesItems = value?.map(i => i?.split("_")[0].replace("层级", ""));
    const filterOption = oldOptions?.[key]?.reduce((cur, pre) => {
      const flag = includesItems?.includes(pre?.value?.split("_")[0].replace("层级", ""));
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

  const onContextmenu = (info) => {
    console.log("info", info);
    if (info?.name !== t("start")) {
      const index = info?.treeAncestors?.findIndex(i => i.name === info.name);
      if (info?.treeAncestors?.[index - 1]?.name === t("start")) {
        setModalParams({
          from: info.name,
          type: "from",
        });
      } else {
        setModalParams({
          from: info?.treeAncestors?.[index - 1]?.name,
          to: info.name,
          type: "to",
        });
      }
      !open && setOpen(true);
    }
  };

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
          resetParams={() => setModalParams({})}
        >
          <EchartCard title={t("tree")}>
            <div
              className={style.echart_main}
              style={{ height: event > 2 ? "5.6rem" : "6.1rem" }}
            >
              <EchratMain refresh={onBlur}>
                <ReactECharts
                  option={option}
                  style={{
                    width: "95%",
                    height: echartHeight,
                  }}
                  showLoading={loading}
                  onEvents={{
                    contextmenu(info) {
                      onContextmenu(info);
                    },
                  }}
                />
              </EchratMain>
            </div>
          </EchartCard>
        </DataContent>
      </div>
    </Section>
  );
};

export default withRouter(Screens);
