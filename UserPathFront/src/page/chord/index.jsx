/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect, useMemo } from "react";
import { Form, Select, Switch, Row, Col } from "antd";
import { useTranslation } from "react-i18next";
import {
  groupData,
  findFetchApi,
  filterchordEachrtData,
  getKeyTovalue,
  saveAcessRecord,
} from "@/utils/datamaps";
import style from "@/style/comm.less";
import { useStore } from "@royjs/core";
import {
  getSessionEventSubCategorySample,
  getSessionEventSample,
  getSessionEventCategorySample,
} from "@/server/path";
import {
  Tabs,
  Section,
  RangeInput,
  EchratMain,
  EchartCard,
  DataContent,
} from "@/components/compontents";
import { Chord } from "@ant-design/plots";
import dayjs from "dayjs";

import { hierarchyToLv } from "@/utils";

const Grap = (props) => {
  const { t, i18n } = useTranslation();
  const {
    location: { query },
  } = props;
  const [form] = Form.useForm();
  const { username, uploadName } = useStore(state => state.user);
  const [event, setEvent] = useState(1);
  const [sample, setSample] = useState([]);
  const [tierSubCategory, setTierSubCategory] = useState([]);
  const [tierPage, setTierPage] = useState([]);
  const [echartData, setEchartData] = useState([]);
  const [screen, setScreen] = useState(false);
  const [modalparams, setModalParams] = useState({});
  const [open, setOpen] = useState(true);

  const initSessionNum = {
    f_session_num_start: 1,
    f_session_num_end: 999999999,
    f_pv_num_start: 100,
    f_pv_num_end: 999999999,
    f_user_num_start: 100,
    f_user_num_end: 999999999,
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

  // 拆入日志
  const postVisitLog = (event) => {
    saveAcessRecord({
      f_analysis_name_first: "chord",
      f_analysis_name_second: getKeyTovalue("value", "lkey", event),
      f_access_time: dayjs().format("YYYY-MM-DD HH:mm:ss"),
    });
  };

  const option = useMemo(() => {
    switch (event) {
      case 1:
      case 2:
        return sample;
      case 3:
      case 4:
        return tierSubCategory;
      case 5:
      case 6:
        return tierPage;
      default:
        return [];
    }
  }, [event, sample, tierPage, tierPage]);

  // 示例数据变化
  const getSample = async (arg) => {
    const { code, data } = await getSessionEventCategorySample(arg);
    if (code === 0 && data && data?.list && data?.list?.length) {
      setSample(data?.list.map(item => ({
        label: item.category,
        value: item.category,
      })));
    } else {
      setSample([]);
    }
  };

  // 获取公共数据
  const getCommMessage = (uploadName, event) => {
    const arg = {
      f_upload_user: username,
      f_upload_name: uploadName,
    };
    getSample(arg);
    getEventSubCategory(arg);
    getSessionEventPage(arg);
    getChordData(
      {
        ...arg,
        ...initSessionNum,
      },
      event,
    );
    form.setFieldValue(findFetchApi(event).chordkey, "");
  };

  useEffect(() => {
    setModalParams({});
    if (username) {
      let ev = event;
      if (query && Object.keys(query)?.length) {
        ev = getKeyTovalue("lkey", "value", query?.f_analysis_name_second);
        setEvent(ev);
      }
      if (uploadName) {
        getCommMessage(uploadName, ev);
        postVisitLog(ev);
      }
      document.getElementsByClassName("fullscreen")[0].oncontextmenu = () => false;
    }
  }, [uploadName]);

  const onCommChange = () => {
    getChordData(filterParams(), event);
  };

  const filterParams = (flag) => {
    const formValue = form.getFieldValue();
    const keys = flag
      ? [findFetchApi(event)?.chordkey]
      : Object.keys(initSessionNum)?.concat(findFetchApi(event)?.chordkey);
    return keys?.reduce(
      (cur, pre) => ({ ...cur, [pre]: formValue?.[pre] }),
      {},
    );
  };

  const onEventChange = (v) => {
    if (event !== v) {
      setModalParams({});
      setEvent(v);
      const arg = {
        f_upload_user: username,
        f_upload_name: uploadName,
        ...initSessionNum,
      };
      form.setFieldsValue({
        [findFetchApi(v).chordkey]: "",
        ...initSessionNum,
      });
      getChordData(arg, v);
      postVisitLog(v);
    }
  };

  const getChordData = async (params, event) => {
    const { code, data } = await findFetchApi(event).chordApi({
      ...params,
      f_upload_name: uploadName,
      f_upload_user: username,
    });
    if (code === 0 && data && data.list && data?.list?.length) {
      const list = filterchordEachrtData(data?.list, event)
        .map(item => ({
          ...item,
          source: hierarchyToLv(i18n, item.source),
          target: hierarchyToLv(i18n, item.target),
        }))
        .filter(item => (!screen ? item : item?.source !== item?.target));
      setEchartData(list);
    } else {
      setEchartData([]);
    }
  };

  const config = useMemo(
    () => ({
      data: echartData,
      sourceField: "source",
      targetField: "target",
      weightField: "value",
      label: {
        offset: 10,
        style: {
          fontSize: 12,
          width: 10,
        },
      },
      tooltip: {
        showTitle: false,
        showContent: true,
        customContent: (_, datum) => {
          if (datum?.length) {
            if (datum?.[0]?.data?.isNode) {
              return (
                <div className={style.chord_tootip}>
                  <div>
                    <i style={{ backgroundColor: datum?.[0]?.color }} />
                    <p>{datum?.[0]?.data?.name}</p>
                  </div>
                </div>
              );
            }
            const item = echartData?.find(i => i?.target === datum?.[0]?.data?.target
                && i?.source === datum?.[0]?.data?.source);
            return (
              <div className={style.chord_tootip}>
                <div>
                  <i style={{ backgroundColor: datum?.[0]?.color }} />
                  <p>{datum?.[0]?.name}</p>
                </div>
                <p>Session：{item?.value?.toLocaleString()}</p>
                <p>PV：{item?.pv?.toLocaleString()}</p>
                <p>
                  {t("user")}：{item?.user?.toLocaleString()}
                </p>
              </div>
            );
          }
        },
      },
    }),
    [echartData, i18n.language],
  );

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

  const onAntdChange = () => {
    form.submit();
  };

  const onReady = (plot) => {
    plot.on("plot:contextmenu", (evt) => {
      const {
        data: { data, shape },
      } = evt;
      if (shape === "polygon") {
        setModalParams({
          from: data?.name,
          type: "from",
        });
      }
      if (shape === "arc") {
        setModalParams({
          from: data?.source,
          to: data.target,
          type: "to",
        });
      }
      setOpen(true);
    });
  };
  return (
    <Section>
      <Tabs data={groupData} event={event} onEventChange={onEventChange} />
      <div className={style.content}>
        <div className={style.conetnt_form}>
          <Form
            layout="horizontal"
            name="event"
            initialValues={initSessionNum}
            form={form}
            labelAlign="right"
            onFinish={onCommChange}
          >
            <Row>
              <Col xl={6} md={12} sm={24}>
                <RangeInput
                  label={t("sessions_threshold")}
                  names={["f_session_num_start", "f_session_num_end"]}
                  {...formItemLayout}
                  onBlur={onAntdChange}
                />
              </Col>
              <Col xl={6} md={12} sm={24}>
                <RangeInput
                  label={t("pv_threshold")}
                  names={["f_pv_num_start", "f_pv_num_end"]}
                  {...formItemLayout}
                  onBlur={onAntdChange}
                />
              </Col>
              <Col xl={6} md={12} sm={24}>
                <RangeInput
                  label={t("user_threshold")}
                  names={["f_user_num_start", "f_user_num_end"]}
                  {...formItemLayout}
                  onBlur={onAntdChange}
                />
              </Col>
              <Col xl={6} md={12} sm={24}>
                <Form.Item
                  name={findFetchApi(event).chordkey}
                  label={t(findFetchApi(event).chordlabelkey)}
                  {...formItemLayout}
                >
                  <Select
                    showSearch
                    allowClear
                    options={option}
                    onChange={onAntdChange}
                  />
                </Form.Item>
              </Col>
            </Row>
          </Form>
        </div>
        <DataContent
          event={event}
          from={filterParams(true)}
          params={modalparams}
          onCancel={() => setOpen(s => !s)}
          open={open}
          apiKey="modalChordApi"
          resetParams={setModalParams}
        >
          <EchartCard
            title={t("chord")}
            rigthComp={
              <div className={style.chordRight}>
                <span className={style.tile_switch}>
                  {t("Remove_Autocorrelation")}：
                </span>
                <Switch
                  checked={screen}
                  onChange={() => setScreen(s => !s)}
                  checkedChildren={t("yes")}
                  unCheckedChildren={t("no")}
                />
              </div>
            }
          >
            <div className={style.echart_main}>
              <EchratMain refresh={onCommChange}>
                {echartData?.length ? (
                  <Chord
                    {...config}
                    style={{
                      width: "95%",
                      height: "6.7rem",
                    }}
                    onReady={onReady}
                  />
                ) : null}
              </EchratMain>
            </div>
          </EchartCard>
        </DataContent>
      </div>
    </Section>
  );
};

export default Grap;
