/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useMemo, useState } from "react";
import style from "@/style/comm.less";
import {
  groupData,
  findFetchApi,
  initfunnelValue,
  initFrom,
  filterfunEchartData,
  getKeyTovalue,
  saveAcessRecord,
  labelCnAndEn,
} from "@/utils/datamaps";
import { Form, Select, Row, Col } from "antd";
import { useStore } from "@royjs/core";
import ReactECharts from "echarts-for-react";
import { rgb, hierarchyArrToLv, hierarchyToLv } from "@/utils";
import dayjs from "dayjs";
import { useTranslation } from "react-i18next";
import {
  Section,
  Tabs,
  EchratMain,
  EchartCard,
  DataContent,
} from "@/components/compontents";

const Funnel = ({ location: { query } }) => {
  const { t, i18n } = useTranslation();
  const [event, setEvent] = useState(1);
  const [form] = Form.useForm();
  const { username, uploadName } = useStore(state => state.user);
  const [firstOpt, setFirstOpt] = useState([]);
  const [secondOpt, setSecondOpt] = useState([]);
  const [threeOpt, setThreeOpt] = useState([]);
  const [fourOpt, setFourOpt] = useState([]);
  const [echartData, setEchartData] = useState([]);
  const [loding, setLoding] = useState(false);
  const [modalparams, setModalParams] = useState({});
  const [open, setOpen] = useState(true);
  const [active, setActive] = useState(false);

  const color = ["#1E90FF", "#00BFFF", "#87CEFA", "#87CEEB"];

  // 拆入日志
  const postVisitLog = (event) => {
    saveAcessRecord({
      f_analysis_name_first: "funnel",
      f_analysis_name_second: getKeyTovalue("value", "lkey", event),
      f_access_time: dayjs().format("YYYY-MM-DD HH:mm:ss"),
    });
  };

  const commValue = useMemo(
    () => ({
      f_upload_name: uploadName,
      f_upload_user: username,
    }),
    [username, uploadName],
  );

  // 获取第一层
  const getFirst = async (event) => {
    const { code, data } = await findFetchApi(event).firstApi(commValue);
    if (code === 0 && data && data?.list && data?.list?.length) {
      const c = findFetchApi(event).dataCode;
      setFirstOpt(data.list.map(item => ({
        label: item[c],
        value: item[c],
      })));
      const first = data?.list?.[0]?.[c];
      form.setFieldValue("f_from_1", first);
      getSecond(first, event);
    } else {
      setFirstOpt([]);
      setSecondOpt([]);
      setThreeOpt([]);
      setFourOpt([]);
    }
  };

  // 第二层
  const getSecond = async (first, event) => {
    const { key } = findFetchApi(event);
    const { code, data } = await findFetchApi(event).secondApi({
      ...commValue,
      [`f_${key}_from1`]: first,
    });
    if (code === 0 && data && data?.list && data?.list?.length) {
      const c = findFetchApi(event).dataCode;
      setSecondOpt(data?.list?.map(item => ({
        label: item[c],
        value: item[c],
      })));
      const second = data?.list?.[0]?.[c];
      form.setFieldValue("f_from_2", second);
      const params = {
        ...commValue,
        [`f_${key}_from1`]: first,
        [`f_${key}_from2`]: second,
      };
      getThree(params, event);
      setThreeOpt([]);
      setFourOpt([]);
      getCommFetch(params, event, key);
    } else {
      setSecondOpt([]);
      setThreeOpt([]);
      setFourOpt([]);
    }
  };

  // 第三层
  const getThree = async (arg, event) => {
    const { code, data } = await findFetchApi(event).threeApi(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      const c = findFetchApi(event).dataCode;
      setThreeOpt(data.list.map(item => ({
        label: item[c],
        value: item[c],
      })));
      setFourOpt([]);
    } else {
      setThreeOpt([]);
    }
  };

  // 第四层
  const getFour = async (arg, event) => {
    const { code, data } = await findFetchApi(event).fourApi(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      const c = findFetchApi(event).dataCode;
      setFourOpt(data.list.map(item => ({
        label: item[c],
        value: item[c],
      })));
    } else {
      setFourOpt([]);
    }
  };

  const filterParams = () => {
    const { key } = findFetchApi(event);
    const formValue = form.getFieldsValue();
    const params = initfunnelValue(
      {
        ...commValue,
        [`f_${key}_from1`]: formValue?.f_from_1,
        [`f_${key}_from2`]: formValue?.f_from_2,
        [`f_${key}_from3`]: formValue?.f_from_3,
        [`f_${key}_from4`]: formValue?.f_from_4,
      },
      key,
    );
    return params;
  };

  const commFetch = (exist, event, key, fn) => {
    const arg = {
      ...commValue,
      ...exist,
    };
    fn && fn(arg, event);
    getCommFetch(arg, event, key);
  };

  // 公共获取
  const getCommFetch = (arg, event, key) => {
    const params = initfunnelValue(arg, key);
    getFunner(event, params);
  };

  const firstChange = (v) => {
    form.setFieldsValue(initFrom(2));
    getSecond(v, event);
  };

  const secondChange = (v) => {
    const { key } = findFetchApi(event);
    const formValue = form.getFieldsValue();
    form.setFieldsValue(initFrom(2));
    commFetch(
      {
        [`f_${key}_from1`]: formValue.f_from_1,
        [`f_${key}_from2`]: v,
      },
      event,
      key,
      getThree,
    );
  };

  const threeChange = (v) => {
    const { key } = findFetchApi(event);
    const formValue = form.getFieldsValue();
    form.setFieldsValue(initFrom(3));
    commFetch(
      {
        [`f_${key}_from1`]: formValue.f_from_1,
        [`f_${key}_from2`]: formValue.f_from_2,
        [`f_${key}_from3`]: v,
      },
      event,
      key,
      getFour,
    );
  };

  const fourChange = (v) => {
    const { key } = findFetchApi(event);
    const formValue = form.getFieldsValue();
    commFetch(
      {
        [`f_${key}_from1`]: formValue.f_from_1,
        [`f_${key}_from2`]: formValue.f_from_2,
        [`f_${key}_from3`]: formValue.f_from_3,
        [`f_${key}_from4`]: v,
      },
      event,
      key,
    );
  };
  const onEventChange = (v) => {
    if (event !== v) {
      setModalParams({});
      setEvent(v);
      setEchartData([]);
      form.resetFields();
      if (uploadName) {
        getFirst(v);
        postVisitLog(v);
      }
    }
  };

  // 获取数据
  const getFunner = async (event, arg) => {
    setLoding(true);
    const { code, data } = await findFetchApi(event).funnelApi(arg);
    if (code === 0 && data && data.list && data.list.length) {
      setEchartData(data.list);
    } else {
      setEchartData([]);
    }
    setLoding(false);
  };

  const option = useMemo(() => {
    const data = filterfunEchartData(i18n, echartData, event).map((item, idx) => ({
      ...item,
      value: item.value,
      itemStyle: {
        color: color[idx] ?? rgb(),
      },
    }));
    return {
      series: [
        {
          name: "Funnel",
          type: "funnel",
          top: 10,
          bottom: 10,
          left: "10%",
          width: "80%",
          minSize: 100,
          maxSize: "70%",
          sort: "descending",
          gap: 2,
          label: {
            show: true,
            position: "inside",
            formatter: params => `${hierarchyToLv(
              i18n,
              params.name,
            )}\n\nSession：${params?.value?.toLocaleString()}\n\n
              PV：${params?.data?.pv?.toLocaleString()}\n\n${t("user")}：${params?.data?.user?.toLocaleString()}\n\n${
  (Math.round((params.value / data?.[0].value) * 10000) / 100)?.toFixed(2)
}%`,
            color: "#fff",
          },
          labelLine: {
            length: 10,
            lineStyle: {
              width: 1,
              type: "solid",
            },
          },
          itemStyle: {
            borderColor: "#fff",
            borderWidth: 1,
          },
          emphasis: {
            label: {
              fontSize: 20,
            },
          },
          expandAndCollapse: true,
          animationDuration: 550,
          animationDurationUpdate: 750,
          data,
        },
      ],
    };
  }, [echartData, i18n.language]);

  useEffect(() => {
    setModalParams({});
    if (username) {
      let ev = event;
      if (query && Object.keys(query).length) {
        ev = getKeyTovalue("lkey", "value", query?.f_analysis_name_second);
        setEvent(ev);
      }
      if (uploadName) {
        getFirst(ev);
        postVisitLog(ev);
      }
      document.getElementsByClassName("echarts-for-react")[0].oncontextmenu =        function () {
        	return false;
      };
    }
  }, [uploadName]);

  const label = useMemo(
    () => labelCnAndEn(i18n, t, event),
    [i18n.language, event],
  );

  const onFinish = () => {
    const v = form.getFieldsValue();
    const { key } = findFetchApi(event);
    commFetch(
      {
        [`f_${key}_from1`]: v.f_from_1,
        [`f_${key}_from2`]: v.f_from_2,
        [`f_${key}_from3`]: v.f_from_3,
        [`f_${key}_from4`]: v.f_from_4,
      },
      event,
      key,
    );
  };

  const formItemLayout = {
    labelCol: {
      xs: { span: 12 },
      sm: { span: 12 },
    },
    wrapperCol: {
      xs: { span: 36 },
      sm: { span: 12 },
    },
  };

  const onContextmenu = (info) => {
    setModalParams({
      from: info.name,
      type: "from",
    });
    !open && setOpen(true);
  };

  return (
    <Section>
      <Tabs data={groupData} event={event} onEventChange={onEventChange} />
      <div className={style.content}>
        <div className={style.conetnt_form}>
          <Form
            layout="horizontal"
            name="event"
            initialValues={{}}
            form={form}
            labelAlign="right"
            onValuesChange={() => {
              setEchartData([]);
            }}
            onFinish={onFinish}
          >
            <Row gutter={24} style={{ margin: 0, padding: 0 }}>
              <Col xl={6} md={12} sm={24}>
                <Form.Item name="f_from_1" label={label[0]} {...formItemLayout}>
                  <Select
                    showSearch
                    options={hierarchyArrToLv(i18n, firstOpt)}
                    onChange={firstChange}
                  />
                </Form.Item>
              </Col>
              <Col xl={6} md={12} sm={24}>
                <Form.Item name="f_from_2" label={label[1]} {...formItemLayout}>
                  <Select
                    showSearch
                    options={hierarchyArrToLv(i18n, secondOpt)}
                    onChange={secondChange}
                  />
                </Form.Item>
              </Col>
              <Col xl={6} md={12} sm={24}>
                <Form.Item name="f_from_3" label={label[2]} {...formItemLayout}>
                  <Select
                    showSearch
                    allowClear
                    options={hierarchyArrToLv(i18n, threeOpt)}
                    onChange={threeChange}
                  />
                </Form.Item>
              </Col>
              <Col xl={6} md={12} sm={24}>
                <Form.Item name="f_from_4" label={label[3]} {...formItemLayout}>
                  <Select
                    showSearch
                    allowClear
                    options={hierarchyArrToLv(i18n, fourOpt)}
                    onChange={fourChange}
                  />
                </Form.Item>
              </Col>
            </Row>
          </Form>
        </div>
        <DataContent
          event={event}
          from={filterParams()}
          params={modalparams}
          onCancel={() => setOpen(s => !s)}
          open={open}
          apiKey="modalFunnelApi"
          resetParams={() => setModalParams({})}
        >
          <EchartCard title={t("funnel")}>
            <div className={style.echart_main}>
              <EchratMain refresh={onFinish} handleCHange={setActive}>
                <ReactECharts
                  option={option}
                  style={{ height: "6.7rem", width: "99.9%" }}
                  showLoading={loding}
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
          </EchartCard>
        </DataContent>
      </div>
    </Section>
  );
};

export default Funnel;
