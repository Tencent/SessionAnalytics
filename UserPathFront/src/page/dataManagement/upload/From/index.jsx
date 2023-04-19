/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { Suspense, useState } from "react";
import style from "./index.less";
import { Form, Input, InputNumber, Button, message, Spin } from "antd";
import CsvUpload from "./csvupload";
import { Card } from "@/components/compontents";
import { uploadExcel, getUploadNameList } from "@/server/path";
import { useStore } from "@royjs/core";
import { useTranslation } from "react-i18next";

const { Item } = Form;
const { TextArea } = Input;

const From = ({ history }) => {
  const [form] = Form.useForm();
  const { t } = useTranslation();
  const { userInfo } = useStore(state => state.user);
  const [fileList, setfileList] = useState([]);
  const [loading, setLoading] = useState(false);

  const formItemLayout = {
    labelCol: { span: 10 },
    wrapperCol: { span: 6 },
  };

  const onFinish = async (f) => {
    setLoading(true);
    const { code: c, data } = await getUploadNameList({
      f_upload_user: userInfo?.username,
    });
    if (c === 0 && data && data.list) {
      const item = data.list.find(i => i.f_upload_name === f.f_upload_name);
      if (item) {
        message.error(t("duplicate dataset names"));
      } else {
        const params = new FormData();
        params.append("f_upload_user", userInfo?.username);
        Object.keys(f).forEach(k => params.append(k, f[k]));
        const { code, message: msg } = await uploadExcel(params);
        setLoading(false);
        if (code === 0) {
          message.success(msg);
          history.goBack();
        } else {
          message.error(msg);
        }
      }
    } else {
      message.error(t("failed_to_obtain"));
    }
  };

  return (
    <Suspense>
      <Spin spinning={loading} size="large">
        <div className={style.from}>
          <Card
            title={t("csv_upload")}
            content={
              <div className={style.from_content}>
                <Form
                  {...formItemLayout}
                  form={form}
                  name="uploadsession"
                  initialValues={{ f_session_split_time: 30 }}
                  onFinish={onFinish}
                >
                  <Item
                    label={t("initial_data")}
                    name="f_upload_name"
                    rules={[
                      { required: true, message: t("please_upload_name") },
                    ]}
                  >
                    <Input placeholder={t("scene_title_p")} />
                  </Item>
                  <Item
                    label={t("description")}
                    name="f_scene_desc"
                    rules={[
                      {
                        required: true,
                        message: t("please_scene_description"),
                      },
                    ]}
                  >
                    <TextArea
                      rows={4}
                      placeholder={t("scene_description_p")}
                      maxLength={200}
                      showCount={true}
                    />
                  </Item>
                  <Item
                    label={t("slice_the_session_event")}
                    name="f_session_event"
                    rules={[
                      {
                        required: true,
                        message: t("please_slice_the_session_event"),
                      },
                    ]}
                  >
                    <TextArea rows={4} placeholder={t("enter_app")} />
                  </Item>
                  <Item
                    label={t("time_interval")}
                    name="f_session_split_time"
                    rules={[
                      { required: true, message: t("please_time_interval") },
                    ]}
                  >
                    <InputNumber
                      addonAfter={t("minute")}
                      style={{ width: "100%" }}
                      placeholder={t("please_time_interval")}
                    />
                  </Item>
                  <Item
                    label={t("csv_file")}
                    name="f_file"
                    rules={[
                      { required: true, message: t("please_csv_upload") },
                    ]}
                  >
                    <CsvUpload fileList={fileList} setfileList={setfileList} />
                  </Item>
                  <Item wrapperCol={{ offset: 12, span: 16 }}>
                    <Button onClick={() => history.goBack()}>
                      {t("cancel")}
                    </Button>
                    <Button
                      type="primary"
                      htmlType="submit"
                      style={{ marginLeft: "0.3rem" }}
                    >
                      {t("submit")}
                    </Button>
                  </Item>
                </Form>
              </div>
            }
          />
        </div>
      </Spin>
    </Suspense>
  );
};

export default From;
