/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState } from "react";
import { Modal, Button, message } from "antd";
import { useTranslation } from "react-i18next";
import { getCommPreviewColums } from "../../utils";
import { Table } from "@/components/compontents";
import { getUserLogLimit } from "@/server/path";
import { useStore } from "@royjs/core";

const CommPreview = (props) => {
  const { disabled = false, r, api = getUserLogLimit } = props;
  const { t } = useTranslation();
  const { username } = useStore(state => state.user);
  const [open, setOpen] = useState(false);
  const [previewList, setPreviewList] = useState([]);

  const onPreview = async (r, l = 10) => {
    const { code, data } = await api({
      f_upload_name: r?.fuploadName,
      f_upload_user: username,
    });
    if (code === 0 && data && data.list && data.list.length) {
      if (l) {
        setPreviewList(data.list?.filter((_, idx) => idx < l));
      } else {
        setPreviewList(data.list);
      }
      setOpen(true);
    } else {
      setPreviewList([]);
      setOpen(false);
      message.warn("未查询到数据");
    }
  };

  return (
    <>
      <Button type="link" disabled={disabled} onClick={() => onPreview(r)}>
        {t("preview")}
      </Button>
      <Modal
        title={t("data_upload_preview")}
        width="calc(100vw - 32px)"
        open={open}
        getContainer={window.document.getElementById("app")}
        onOk={() => setOpen(false)}
        onCancel={() => setOpen(false)}
        destroyOnClose={true}
      >
        <Table
          rowKey="id"
          dataSource={previewList}
          columns={getCommPreviewColums(t)}
          scroll={{ x: 1400 }}
          pagination={false}
        />
      </Modal>
    </>
  );
};

export default CommPreview;
