/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useMemo, useState } from "react";
import { Button } from "antd";
import { withRouter } from "react-router-dom";
import { downloadTemplate } from "@/utils";
import {
  getUserLogUploadStatusCnt, // 任务情况数量
  getUserLogUploadStatusEntity, // 任务情况列表
} from "@/server/path";
import { useStore } from "@royjs/core";
import { useTranslation } from "react-i18next";
import { v4 as uuidv4 } from "uuid";
import { Table, Search, Card } from "@/components/compontents";
import { downloadUlr } from "../utils";
import style from "./index.less";
import classNames from "classnames";
import CommPreview from "../components/CommPreview";
import StatusProgress from "../components/StatusProgress";

const Upload = ({ history }) => {
  const { t, i18n } = useTranslation();
  const initForm = {
    f_status: "all",
    f_upload_name: undefined,
    page: 0,
    page_size: 10,
  };

  const { username } = useStore(state => state.user);
  const [statusCnt, setStatusCnt] = useState([]);
  const [form, setForm] = useState(initForm);

  const [table, setTable] = useState({
    total: 0,
    list: [],
  });
  const [tempTable, setTemptable] = useState({
    total: 0,
    list: [],
  });

  const [loading, setLoading] = useState(false);

  const getKeyTovalue = (data, status) => {
    const item = data.find(i => i.f_deal_status === status);
    return item ? item.f_cnt ?? 0 : 0;
  };

  const geTtemplate = async () => {
    await getUserLogEntity(
      {
        ...initForm,
        f_upload_user: i18n.language === "cn" ? "demo" : "demo_en",
        f_status: undefined,
      },
      setTemptable,
    );
  };

  const tasksData = useMemo(
    () => [
      {
        tl: t("progress_task"),
        num: getKeyTovalue(statusCnt, "上传进行中"),
        color: "#3B93B4",
      },
      {
        tl: t("completed_task"),
        num: getKeyTovalue(statusCnt, "上传完成"),
        color: "#02830F",
      },
      {
        tl: t("failed_task"),
        num: getKeyTovalue(statusCnt, "上传失败"),
        color: "#D9001B",
      },
    ],
    [t, statusCnt],
  );

  const statusData = [
    {
      key: t("all"),
      value: "all",
    },
    {
      key: t("conduct"),
      value: 2,
    },
    {
      key: t("waiting"),
      value: 0,
    },
    {
      key: t("completed"),
      value: 1,
    },
    {
      key: t("fail"),
      value: 3,
    },
  ];

  const exampleColums = [
    {
      title: t("initial_data"),
      dataIndex: "f_upload_name",
    },
    {
      title: t("description"),
      dataIndex: "f_scene_desc",
    },
    {
      title: t("slice_the_session_event"),
      dataIndex: "f_session_event",
    },
    {
      title: t("time_interval"),
      dataIndex: "f_session_split_time",
    },
    {
      title: t("csv_file"),
      dataIndex: "operate",
      render() {
        return (
          <>
            <CommPreview
              r={tempTable?.list?.[0]}
              disabled={!tempTable?.list?.length}
            />
            <Button
              type="link"
              onClick={() => downloadTemplate(downloadUlr[i18n.language])}
            >
              {t("demo_csv")}
            </Button>
          </>
        );
      },
    },
  ];

  const exampleDataSource = [
    {
      f_upload_name: t("example_title"),
      f_scene_desc: t("example_scenario_description"),
      f_session_event: t("example_xxx_event"),
      f_session_split_time: t("xx_minute"),
    },
  ];

  const columns = [
    {
      title: t("initial_data"),
      dataIndex: "fuploadName",
      width: "15%",
    },
    {
      title: t("description"),
      dataIndex: "fsceneDesc",
      width: "25%",
    },
    {
      title: t("create_user"),
      dataIndex: "fuploadUser",
      width: "10%",
    },
    {
      title: t("creation_time"),
      dataIndex: "fcreateTime",
      width: "10%",
    },
    {
      title: t("task_execution_time"),
      dataIndex: "fupdateTime",
      width: "10%",
    },
    {
      title: t("upload_progress"),
      dataIndex: "fuploadStatus",
      width: "20%",
      render(t) {
        return <StatusProgress status={t} />;
      },
    },
    {
      title: t("operation"),
      dataIndex: "cz",
      width: "10%",
      render(_, r) {
        return <CommPreview disabled={!(r.fuploadStatus === 1)} r={r} />;
      },
    },
  ];

  const getStatusCnt = async () => {
    const { code, data } = await getUserLogUploadStatusCnt({
      f_upload_user: username,
    });
    if (code === 0 && data && data.list && data.list.length) {
      setStatusCnt(data.list);
    } else {
      setStatusCnt([]);
    }
  };

  const getUserLogEntity = async (params, set, load) => {
    load && load(true);
    const { code, data } = await getUserLogUploadStatusEntity(params);
    if (code === 0 && data && data.list && data.list.length) {
      set({
        total: data.total,
        list: data.list,
      });
    } else {
      set({
        total: 0,
        list: [],
      });
    }
    load && load(false);
  };

  const onTableChange = (p) => {
    setForm(p);
    getUserLogEntity(
      {
        ...p,
        f_status: p.f_status === "all" ? undefined : p.f_status,
        f_upload_user: username,
      },
      setTable,
      setLoading,
    );
  };

  useEffect(() => {
    if (username) {
      getStatusCnt();
      geTtemplate();
      getUserLogEntity(
        {
          ...form,
          f_status: form.f_status === "all" ? undefined : form.f_status,
          f_upload_user: username,
        },
        setTable,
        setLoading,
      );
    }
  }, [username]);

  return (
    <div className={style.main_content}>
      <Card
        title={t("task_process")}
        content={
          <div className={style.tasks}>
            {tasksData.map(item => (
              <div className={style.tasks_item} key={uuidv4()}>
                <p style={{ color: item.color }}>{item.tl}</p>
                <p style={{ color: item.color }}>
                  {`${item.num}${i18n.language === "cn" ? "个" : " "}${t("task")}`}
                </p>
              </div>
            ))}
          </div>
        }
      />
      <Card
        title={t("upload")}
        content={
          <div className={style.csvupload}>
            <div
              className={style.tasksupload}
              onClick={() => history.push("/upload/csv")}
            >
              + {t("add_upload_csv")}
            </div>
            <div className={style.example}>{t("Example")}</div>
            <Table
              rowKey="id"
              dataSource={exampleDataSource}
              columns={exampleColums}
              pagination={false}
              className={style.example}
            />
          </div>
        }
      />
      <Card
        title={t("task_list")}
        rigth={
          <div className={style.t_headright}>
            <div className={style.t_radioGroup}>
              {statusData?.map(item => (
                <span
                  key={uuidv4()}
                  className={classNames(
                    style.span,
                    form.f_status === item.value && style.span_click,
                  )}
                  onClick={() => onTableChange({ ...form, f_status: item.value, page: 0 })
                  }
                >
                  {item.key}
                </span>
              ))}
            </div>
            <Search
              value={form.f_upload_name}
              placeholder={t("please_placeholder")}
              onChange={(v) => {
                setForm(s => ({
                  ...s,
                  f_upload_name: v,
                }));
              }}
              onSearch={() => {
                onTableChange({ ...form, page: 0 });
              }}
            />
          </div>
        }
        content={
          <Table
            rowKey="id"
            dataSource={table}
            className={style.tasktable}
            style={{}}
            columns={columns}
            loading={loading}
            pagination={form}
            onChange={(page) => {
              onTableChange({
                ...form,
                page: page - 1,
              });
            }}
          />
        }
      />
    </div>
  );
};

export default withRouter(Upload);
