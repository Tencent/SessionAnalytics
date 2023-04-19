/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState } from "react";
import {
  InboxOutlined,
  ExclamationCircleOutlined,
  LoadingOutlined,
} from "@ant-design/icons";
import { Upload, Modal, message } from "antd";
import { useTranslation } from "react-i18next";
import Papa from "papaparse";
// 如遇格式乱码请解开注释、下载对应库
// import jschardet from "jschardet";
// import iconv from "iconv-lite";
// import encoding from "encoding";
import { Table } from "@/components/compontents";
import { getCommPreviewColums } from "../../utils";
import style from "./index.less";

const { Dragger } = Upload;
const { confirm } = Modal;

const ConfirmTable = (prop) => {
  const { columns = [], dataSource = [] } = prop;
  return (
    <Table
      columns={columns}
      dataSource={dataSource}
      scroll={{ x: 1400, heigth: 800 }}
      pagination={false}
    />
  );
};

const CsvUpload = (props) => {
  const { onChange, fileList, setfileList } = props;
  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);

  const beforeUpload = (file) => {
    setLoading(true);
    const type = file?.name?.split(".");
    const isXlsx = type?.length && type?.[type?.length - 1] === "csv";
    if (file.size <= 1024 * 1024 * 200) {
      if (isXlsx) {
        const fReader = new FileReader();
        fReader.readAsBinaryString(file.slice(0, 1024 * 1024 * 0.5));
        fReader.onload = (event) => {
          const fileBuf = event.target.result;
          // const encodeType = jschardet.detect(fileBuf).encoding;
          // if (encodeType !== "UTF-8") {
          //   fileBuf = encoding.convert(fileBuf, "utf8", encodeType);
          // }
          // iconv.skipDecodeWarning = true;
          // const textData = iconv.decode(fileBuf, "utf8");
          Papa.parse(fileBuf, {
            encoding: "UTF-8",
            complete(results) {
              const jsonArr = results.data;
              if (jsonArr.length > 0) {
                const dataSource = jsonArr
                  .filter((_, index) => index >= 1 && index <= 10)
                  .map(item => item.reduce(
                    (cur, pre, idx) => ({
                      ...cur,
                      [getCommPreviewColums(t)[idx]?.dataIndex]: pre,
                    }),
                    {},
                  ));
                confirm({
                  width: "calc(100vw - 50px)",
                  centered: true,
                  title: t("please_ok_csv"),
                  icon: <ExclamationCircleOutlined />,
                  content: (
                    <ConfirmTable
                      dataSource={dataSource}
                      columns={getCommPreviewColums(t)}
                    />
                  ),
                  okText: t("ok"),
                  cancelText: t("cancel"),
                  onOk: () => {
                    setLoading(false);
                    onChange(file);
                    setfileList([file]);
                  },
                  onCancel: () => {
                    setLoading(false);
                  },
                  getContainer: window.document.getElementById("app"),
                });
              }
            },
          });
        };
      } else {
        message.error(t("error_upload_type"));
      }
    } else {
      message.error(t("file_restriction"));
    }
    return false;
  };

  const onRemove = () => {
    setfileList([]);
    onChange();
  };

  return (
    <div className={style.uploadbox}>
      <Dragger
        accept={[".csv"]}
        maxCount={1}
        multiple={false}
        fileList={fileList}
        className="dragger"
        beforeUpload={beforeUpload}
        onRemove={onRemove}
      >
        <div className={style.upload_drag}>
          <p className={style.upload_drag_icon}>
            {loading ? <LoadingOutlined /> : <InboxOutlined />}
          </p>
          <p>{t("please_click_scv")}</p>
          <p>{t("extensions_supported")}</p>
          <p>{t("file_type")}</p>
          <p style={{ color: "red", fontSize: "0.12rem" }}>{`${t("upload_msg")}：? * : " , < > \ / |))`}</p>
        </div>
      </Dragger>
    </div>
  );
};

export default CsvUpload;
