/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useState } from "react";
import { Row, Col, Form, Input, Button, Select, DatePicker } from "antd";
import { useTranslation } from "react-i18next";
import {
  getSessionLogQuery,
  getSessionEventCategorySample,
  getSessionEventSubCategorySample,
  getSessionEventSample,
} from "@/server/path";
import {
  getSessionLogChannelList,
  getSessionLogProvinceList,
} from "@/server/trend";
import { useStore } from "@royjs/core";
import { Table, Section, Card } from "@/components/compontents";
import dayjs from "dayjs";
import style from "@/style/comm.less";

const { Item } = Form;
const { RangePicker } = DatePicker;
const format = "YYYY/MM/DD";

const DataSelect = () => {
  const [form] = Form.useForm();
  const { t } = useTranslation();
  const initPagenation = {
    page: 0,
    page_size: 10,
  };
  const initForm = {
    f_user_id: "",
    f_channel: "",
    f_province: "",
    f_city: "",
    f_category: "",
    f_subcategory: "",
    f_date: "",
  };
  const { username, uploadName } = useStore(state => state.user);
  const [channelList, setChannelList] = useState([]);
  const [provinceList, setProvinceList] = useState([]);
  const [categorylList, setCategorylList] = useState([]);
  const [subCategoryList, setSubCategoryList] = useState([]);
  const [eventList, setEventList] = useState([]);
  const [dataSource, setDataSource] = useState([]);
  const [pageNation, setPageNation] = useState(initPagenation);
  const [loading, setLoading] = useState(false);

  const disabledDate = current => current && current > dayjs();

  const getCommOption = async (params, Api, setOption, key) => {
    const { code, data } = await Api(params);
    if (code === 0 && data && data.list) {
      setOption(data.list.map(item => ({
        label: item[key],
        value: item[key],
      })));
    } else {
      setOption([]);
    }
  };

  const commRequest = (params) => {
    getCommOption(
      params,
      getSessionLogChannelList,
      setChannelList,
      "f_channel",
    );
    getCommOption(
      params,
      getSessionLogProvinceList,
      setProvinceList,
      "f_province",
    );
    getCommOption(
      params,
      getSessionEventCategorySample,
      setCategorylList,
      "category",
    );
    getCommOption(
      params,
      getSessionEventSubCategorySample,
      setSubCategoryList,
      "subcategory",
    );
    getCommOption(params, getSessionEventSample, setEventList, "event");
  };

  const getSessionList = async (params) => {
    setLoading(true);
    const { code, data } = await getSessionLogQuery({
      f_upload_user: username,
      ...params,
      f_upload_name: uploadName,
      f_date: undefined,
      f_start_time: params?.f_date?.[0]
        ? dayjs(params?.f_date?.[0]).format(format)
        : undefined,
      f_end_time: params?.f_date?.[1]
        ? dayjs(params?.f_date?.[1]).format(format)
        : undefined,
    });
    if (code === 0 && data && data.list && data.list.length) {
      setDataSource({
        list: data.list,
        total: data.total,
      });
    } else {
      setDataSource({
        list: [],
        total: 0,
      });
    }
    setLoading(false);
  };

  const onPageSizeChange = (page, page_size) => {
    const formValue = form.getFieldsValue();
    setPageNation({
      page: page - 1,
      page_size,
    });
    getSessionList({
      page: page - 1,
      page_size,
      ...formValue,
    });
  };

  const onCommChange = () => {
    const formValue = form.getFieldsValue();
    setPageNation(initPagenation);
    getSessionList({
      ...initPagenation,
      ...formValue,
    });
  };

  useEffect(() => {
    if (username && uploadName) {
      form.setFieldsValue(initForm);
      commRequest({
        f_upload_user: username,
        f_upload_name: uploadName,
      });
      setPageNation(initPagenation);
      getSessionList({
        ...initPagenation,
        f_upload_user: username,
      });
    }
  }, [uploadName]);

  const formItemLayout = {
    labelCol: {
      xs: { span: 6 },
      sm: { span: 6 },
    },
    wrapperCol: {
      xs: { span: 18 },
      sm: { span: 18 },
    },
  };

  const columns = [
    {
      title: "UserId",
      dataIndex: "fuserId",
      key: "fuserId",
      sorter: (a, b) => a.fuserId.localeCompare(b.fuserId),
    },
    {
      title: t("f_age"),
      dataIndex: "fage",
      key: "fage",
      sorter: (a, b) => a.fage - b.fage,
    },
    {
      title: t("f_sex"),
      dataIndex: "fsex",
      key: "fsex",
      sorter: (a, b) => a.fsex.localeCompare(b.fsex),
    },
    {
      title: t("f_province"),
      dataIndex: "fprovince",
      key: "fprovince",
      sorter: (a, b) => a.fprovince.localeCompare(b.fprovince),
    },
    {
      title: t("f_city"),
      dataIndex: "fcity",
      key: "fcity",
      sorter: (a, b) => a.fcity.localeCompare(b.fcity),
    },
    {
      title: t("f_channel"),
      dataIndex: "fchannel",
      key: "fchannel",
      sorter: (a, b) => a.fchannel.localeCompare(b.fchannel),
    },
    {
      title: "SessionId",
      dataIndex: "fsessionId",
      key: "fsessionId",
      sorter: (a, b) => a.fsessionId - b.fsessionId,
    },
    {
      title: "SubId",
      dataIndex: "fsessionRank",
      key: "fsessionRank",
      sorter: (a, b) => a.fsessionRank - b.fsessionRank,
    },
    {
      title: t("event"),
      dataIndex: "fevent",
      key: "fevent",
      sorter: (a, b) => a.fevent.localeCompare(b.fevent),
    },
    {
      title: t("scene"),
      dataIndex: "fcategory",
      key: "fcategory",
      sorter: (a, b) => a.fcategory.localeCompare(b.fcategory),
    },
    {
      title: t("type"),
      dataIndex: "fsubcategory",
      key: "fsubcategory",
      sorter: (a, b) => a.fsubcategory.localeCompare(b.fsubcategory),
    },
    {
      title: t("f_client_time"),
      dataIndex: "fclientTime",
      key: "fclientTime",
      sorter: (a, b) => new Date(a.fclientTime).getTime() - new Date(b.fclientTime).getTime(),
    },
  ];

  const colStyle = {
    xl: 6,
    md: 12,
    sm: 24,
  };

  return (
    <Section>
      <div className={style.main_content}>
        <Card
          title={t("analysis_and_screening")}
          content={
            <Form
              layout="horizontal"
              name="event"
              initialValues={initForm}
              form={form}
              style={{ padding: "0.2rem" }}
              labelAlign="right"
              onFinish={onCommChange}
            >
              <Row gutter={24} style={{ margin: 0, padding: 0 }}>
                <Col {...colStyle}>
                  <Item
                    name="f_user_id"
                    label={t("User_id")}
                    {...formItemLayout}
                  >
                    <Input style={{ width: "80%" }} />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Item
                    name="f_channel"
                    label={t("Channel")}
                    {...formItemLayout}
                  >
                    <Select
                      style={{ width: "80%" }}
                      options={channelList}
                      onChange={onCommChange}
                      allowClear={true}
                    />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Item
                    name="f_province"
                    label={t("f_province")}
                    {...formItemLayout}
                  >
                    <Select
                      style={{ width: "80%" }}
                      options={provinceList}
                      onChange={onCommChange}
                      allowClear={true}
                    />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Item name="f_city" label={t("City")} {...formItemLayout}>
                    <Input style={{ width: "80%" }} />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Item
                    name="f_category"
                    label={t("Scene")}
                    {...formItemLayout}
                  >
                    <Select
                      options={categorylList}
                      onChange={onCommChange}
                      allowClear={true}
                      style={{ width: "80%" }}
                    />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Item
                    name="f_subcategory"
                    label={t("Type")}
                    {...formItemLayout}
                  >
                    <Select
                      options={subCategoryList}
                      onChange={onCommChange}
                      allowClear={true}
                      style={{ width: "80%" }}
                    />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Item name="f_event" label={t("Event")} {...formItemLayout}>
                    <Select
                      options={eventList}
                      onChange={onCommChange}
                      allowClear={true}
                      style={{ width: "80%" }}
                    />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Item name="f_date" label={t("Date")} {...formItemLayout}>
                    <RangePicker
                      disabledDate={disabledDate}
                      format={format}
                      style={{ width: "80%" }}
                      onChange={onCommChange}
                    />
                  </Item>
                </Col>
                <Col {...colStyle}>
                  <Button
                    type="primary"
                    htmlType="submit"
                    style={{ marginLeft: "1rem" }}
                  >
                    {t("apply")}
                  </Button>
                </Col>
              </Row>
            </Form>
          }
        />
        <Card
          title=""
          content={
            <Table
              style={{ margin: "0 0.2rem" }}
              columns={columns}
              dataSource={dataSource}
              loading={loading}
              pagination={pageNation}
              onChange={onPageSizeChange}
            />
          }
        />
      </div>
    </Section>
  );
};

export default DataSelect;
