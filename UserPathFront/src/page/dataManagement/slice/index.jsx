/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useMemo, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useStore } from "@royjs/core";
import { Button, Popconfirm, message } from "antd";
import { Table, Search, Card } from "@/components/compontents";
import { QuestionCircleOutlined } from "@ant-design/icons";
import {
  getUserLogMakeStatusCnt,
  getUserLogSplitStatusOldfEntity,
  getUserLogSplitStatusNewfEntity,
  postSessionSplit,
  getUserLogMakeLimit,
} from "@/server/trend";
import { v4 as uuidv4 } from "uuid";
import classNames from "classnames";
import style from "../upload/index.less";
import CommPreview from "../components/CommPreview";
import StatusProgress from "../components/StatusProgress";

const Slice = () => {
  const { t, i18n } = useTranslation();
  const { username } = useStore(state => state.user);
  const [makeStatus, setMakeStatus] = useState([]);

  const initForm = {
    f_session_split_status: "all",
    f_upload_name: undefined,
    page: 0,
    page_size: 10,
  };

  const [initialForm, setInitialForm] = useState(initForm);
  const [makeform, setMakeForm] = useState(initForm);
  const [initTable, setInitTable] = useState({
    total: 0,
    list: [],
  });

  const [makeTable, setMakeTable] = useState({
    total: 0,
    list: [],
  });

  const [initialLoading, setInitialLoading] = useState(false);
  const [cleansingLoading, setCleansingLoading] = useState(false);

  const getNum = (arr, key) => {
    const num = arr.find(i => i?.f_deal_status === key);
    return num && num?.f_cnt ? num?.f_cnt : 0;
  };

  const tasksData = useMemo(
    () => [
      {
        tl: t("wait_slice"),
        num: getNum(makeStatus, "上传完成待切分"),
        color: "#FF8C00",
      },
      {
        tl: t("in_progress"),
        num: getNum(makeStatus, "切分处理中"),
        color: "#3B93B4",
      },
      {
        tl: t("segment_completed"),
        num: getNum(makeStatus, "切分完成"),
        color: "#02830F",
      },
      {
        tl: t("segment_failed"),
        num: getNum(makeStatus, "切分失败"),
        color: "#D9001B",
      },
    ],
    [makeStatus, i18n.language],
  );

  const taskStatusData = [
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

  const taskColumns = [
    {
      title: t("initial_data"),
      dataIndex: "fuploadName",
      width: "10%",
    },
    {
      title: t("description"),
      dataIndex: "fsceneDesc",
      width: "15%",
    },
    {
      title: t("create_user"),
      dataIndex: "fuploadUser",
      width: "10%",
    },
    {
      title: t("creation_time"),
      dataIndex: "fmakeCreateTime",
      width: "15%",
    },
    {
      title: t("slice_progress"),
      dataIndex: "fsessionSplitStatus",
      width: "10%",
      render(t) {
        return <StatusProgress status={t} />;
      },
    },
    {
      title: t("operation"),
      dataIndex: "cz",
      width: "10%",
      render(_, r) {
        return (
          <>
            <CommPreview r={r} />
            <Popconfirm
              title={t("is_slicing")}
              icon={<QuestionCircleOutlined style={{ color: "red" }} />}
              onConfirm={() => splitSession(r?.fuploadName, "old")}
              disabled={!(r?.fsessionSplitStatus === 0)}
            >
              <Button type="link" disabled={!(r?.fsessionSplitStatus === 0)}>
                {t("segment")}
              </Button>
            </Popconfirm>
          </>
        );
      },
    },
  ];

  const governColumns = [
    {
      title: t("cleansing_data"),
      dataIndex: "fuploadName",
      width: "10%",
    },
    {
      title: t("description"),
      dataIndex: "fsceneDesc",
      width: "15%",
    },
    {
      title: t("create_user"),
      dataIndex: "fuploadUser",
      width: "10%",
    },
    {
      title: t("creation_time"),
      dataIndex: "fmakeCreateTime",
      width: "15%",
    },
    {
      title: t("slice_progress"),
      dataIndex: "fsessionSplitStatus",
      width: "10%",
      render(t) {
        return <StatusProgress status={t} />;
      },
    },
    {
      title: t("operation"),
      dataIndex: "cz",
      width: "10%",
      render(_, r) {
        return (
          <>
            <CommPreview r={r} api={getUserLogMakeLimit} />
            <Popconfirm
              title={t("is_slicing")}
              icon={<QuestionCircleOutlined style={{ color: "red" }} />}
              onConfirm={() => splitSession(r?.fuploadName, "new")}
              disabled={!(r?.fsessionSplitStatus === 0)}
            >
              <Button type="link" disabled={!(r?.fsessionSplitStatus === 0)}>
                {t("segment")}
              </Button>
            </Popconfirm>
          </>
        );
      },
    },
  ];

  const getUserLogMakeCnt = async () => {
    const { code, data } = await getUserLogMakeStatusCnt({
      f_upload_user: username,
    });
    if (code === 0 && data && data.list) {
      setMakeStatus(data?.list);
    } else {
      setMakeStatus([]);
    }
  };

  const getSplitStatusOldfEntity = async (params) => {
    setInitialLoading(true);
    const { code, data } = await getUserLogSplitStatusOldfEntity({
      f_upload_user: username,
      ...params,
      f_session_split_status:
        params?.f_session_split_status === "all"
        	? undefined
        	: params?.f_session_split_status,
    });
    if (code === 0 && data && data.list && data?.list?.length) {
      setInitTable({
        total: data?.total,
        list: data?.list,
      });
    } else {
      setInitTable({
        total: 0,
        list: [],
      });
    }
    setInitialLoading(false);
  };

  const getSplitStatusNewfEntity = async (params) => {
    setCleansingLoading(true);
    const { code, data } = await getUserLogSplitStatusNewfEntity({
      f_upload_user: username,
      ...params,
      f_session_split_status:
        params?.f_session_split_status === "all"
        	? undefined
        	: params?.f_session_split_status,
    });
    if (code === 0 && data && data.list && data?.list?.length) {
      setMakeTable({
        total: data?.total,
        list: data?.list,
      });
    } else {
      setMakeTable({
        total: 0,
        list: [],
      });
    }
    setCleansingLoading(false);
  };

  // 切分
  const splitSession = async (uplodaName, key) => {
    const { code, message: msg } = await postSessionSplit({
      f_upload_user: username,
      f_upload_name: uplodaName,
    });
    if (code === 0) {
      getUserLogMakeCnt();
      if (key === "old") {
        setInitialForm(initForm);
        getSplitStatusOldfEntity(initForm);
      } else {
        setMakeForm(initForm);
        getSplitStatusNewfEntity(initForm);
      }
      message.success(msg);
    } else {
      message.error(msg);
    }
  };

  const onInitTableChange = (params) => {
    setInitialForm(s => ({
      ...s,
      ...params,
    }));
    getSplitStatusOldfEntity({
      ...initialForm,
      ...params,
    });
  };

  const onMakeTableChange = (params) => {
    setMakeForm(s => ({
      ...s,
      ...params,
    }));
    getSplitStatusNewfEntity({
      ...makeform,
      ...params,
    });
  };

  useEffect(() => {
    if (username) {
      getUserLogMakeCnt();
      getSplitStatusOldfEntity(initialForm);
      getSplitStatusNewfEntity(makeform);
    }
  }, [username]);

  return (
    <div className={style.main_content}>
      <Card
        title={t("segment_task_status ")}
        content={
          <div className={style.tasks}>
            {tasksData.map(item => (
              <div className={style.tasks_item} key={uuidv4()}>
                <p style={{ color: item.color }}>{item.tl}</p>
                <p style={{ color: item.color }}>
                  {" "}
                  {`${item.num}${i18n.language === "cn" ? "个" : " "}${t("task")}`}
                </p>
              </div>
            ))}
          </div>
        }
      />
      <Card
        title={t("initial_dataSet_list")}
        rigth={
          <div className={style.t_headright}>
            <div className={style.t_radioGroup}>
              {taskStatusData?.map(item => (
                <span
                  key={uuidv4()}
                  className={classNames(
                    style.span,
                    initialForm.f_session_split_status === item.value
                      && style.span_click,
                  )}
                  onClick={() => onInitTableChange({
                    f_session_split_status: item.value,
                    page: 0,
                  })
                  }
                >
                  {item.key}
                </span>
              ))}
            </div>
            <Search
              value={initialForm?.f_upload_name}
              placeholder={t("please_placeholder")}
              onChange={(v) => {
                setInitialForm(s => ({ ...s, f_upload_name: v }));
              }}
              onSearch={() => onInitTableChange({
                page: 0,
              })
              }
            />
          </div>
        }
        content={
          <Table
            rowKey="id"
            dataSource={initTable}
            className={style.tasktable}
            columns={taskColumns}
            loading={initialLoading}
            pagination={initialForm}
            onChange={(page) => {
              onInitTableChange({
                page: page - 1,
              });
            }}
          />
        }
      />
      <Card
        title={t("cleansing_dataSet_list")}
        rigth={
          <div className={style.t_headright}>
            <div className={style.t_radioGroup}>
              {taskStatusData?.map(item => (
                <span
                  key={uuidv4()}
                  className={classNames(
                    style.span,
                    makeform.f_session_split_status === item.value
                      && style.span_click,
                  )}
                  onClick={() => onMakeTableChange({
                    f_session_split_status: item.value,
                    page: 0,
                  })
                  }
                >
                  {item.key}
                </span>
              ))}
            </div>
            <Search
              value={makeform?.f_upload_name}
              placeholder={t("please_placeholder")}
              onChange={v => setMakeForm(s => ({ ...s, f_upload_name: v }))}
              onSearch={() => onMakeTableChange({
                page: 0,
              })
              }
            />
          </div>
        }
        content={
          <Table
            rowKey="id"
            dataSource={makeTable}
            className={style.tasktable}
            columns={governColumns}
            pagination={makeform}
            loading={cleansingLoading}
            onChange={(page) => {
              onMakeTableChange({
                page: page - 1,
              });
            }}
          />
        }
      />
    </div>
  );
};

export default Slice;
