/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useMemo, useEffect } from "react";
import { Table, EditInput, Search, Card } from "@/components/compontents";
import { Input, Button, message, Tooltip, Popconfirm, Modal, Form } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";
import { useTranslation } from "react-i18next";
import { useStore } from "@royjs/core";
import {
  getUserLogMakeStatusEntity,
  getUserLogTop,
  updateUserLogTop,
  getUserLogMake,
  updateMakeLogName,
} from "@/server/trend";
import { getUserLogUploadStatusEntity, getUploadNameList } from "@/server/path";
import { v4 as uuidv4 } from "uuid";
import classNames from "classnames";
import style from "../upload/index.less";
import CommPreview from "../components/CommPreview";
import StatusProgress from "../components/StatusProgress";
import { getPagenation } from "@/utils";

const { TextArea } = Input;
const { Item } = Form;

const Governance = () => {
  const { t } = useTranslation();
  const [form] = Form.useForm();
  const { username } = useStore(state => state.user);
  const [topGovernance, setTopGovernance] = useState({});
  const [editOpen, setEditOpen] = useState(false);
  const [editParameters, setEditParameters] = useState({});

  const initPagination = {
    page: 0,
    page_size: 10,
  };


  const initMakeForm = {
    f_make_status: "all",
    f_upload_name: undefined,
    ...initPagination,
  };

  const [initialLoading, setInitialLoading] = useState(false);
  const [cleansingLoading, setCleansingLoading] = useState(false);
  const [mappingLoading, setMappingLoading] = useState(false);

  const initForm = {
    f_status: "all",
    f_upload_name: undefined,
    ...initPagination,
  };

  const initTopList = {
    category: [],
    subcategory: [],
    event: [],
  };
  const [makeform, setMakeForm] = useState(initMakeForm);
  const [initialForm, setInitialForm] = useState(initForm);
  const [gover, setGover] = useState("category");
  const [makeTable, setMakeTable] = useState({
    total: 0,
    list: [],
  });
  const [initTable, setInitTable] = useState({
    total: 0,
    list: [],
  });
  const [topList, setTopList] = useState(initTopList);

  const formItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 16 },
  };

  const [makePage, setMakePage] = useState(initPagination);

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

  const governanceData = [
    {
      key: t("scene"),
      value: "category",
    },
    {
      key: t("type"),
      value: "subcategory",
    },
    {
      key: t("event"),
      value: "event",
    },
  ];

  const taskColumns = [
    {
      title: t("initial_data"),
      dataIndex: "fuploadName",
      width: "20%",
    },
    {
      title: t("description"),
      dataIndex: "fsceneDesc",
      width: "20%",
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
      title: t("upload_progress"),
      dataIndex: "fuploadStatus",
      width: "15%",
      render(t) {
        return <StatusProgress status={t} />;
      },
    },
    {
      title: t("operation"),
      dataIndex: "cz",
      width: "20%",
      render(_, r) {
        return (
          <div>
            <CommPreview disabled={!(r.fuploadStatus === 1)} r={r} />
            <Tooltip title={t("click_to_look")}>
              <Button
                type="link"
                onClick={() => onGovernanceChange(
                  {
                    ...r,
                    edit: true,
                  },
                  true,
                )
                }
                disabled={!(r.fuploadStatus === 1)}
              >
                {t("data_cleansing")}
              </Button>
            </Tooltip>
          </div>
        );
      },
    },
  ];

  const governColumns = [
    {
      title: t("cleansing_data"),
      dataIndex: "fuploadName",
      width: "20%",
    },
    {
      title: t("description"),
      dataIndex: "fsceneDesc",
      width: "20%",
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
      title: t("cleansing_progress"),
      dataIndex: "fmakeStatus",
      width: "15%",
      render(t) {
        return <StatusProgress status={t} />;
      },
    },
    {
      title: t("mapping_relation"),
      dataIndex: "mr",
      width: "10%",
      render(_, r) {
        return (
          <Button
            type="link"
            onClick={() => onGovernanceChange(
              {
                ...r,
                edit: false,
              },
              true,
            )
            }
          >
            {t("preview")}
          </Button>
        );
      },
    },
    {
      title: t("operation"),
      dataIndex: "cz",
      width: "10%",
      render(_, r) {
        return (
          <Button
            type="link"
            onClick={() => {
              setEditOpen(true);
              setEditParameters(r);
            }}
          >
            {t("revise")}
          </Button>
        );
      },
    },
  ];

  const governanceColumns = [
    {
      title: t("scene_title"),
      dataIndex: "fuploadName",
      width: "20%",
      render: () => topGovernance?.fuploadName,
    },
    {
      title: t("No"),
      dataIndex: "idx",
      width: "10%",
      render: t => t + 1,
    },
    {
      title: "PV",
      dataIndex: "pv",
      width: "10%",
      render: t => t?.toLocaleString(),
    },
    {
      title: t("original_scene"),
      dataIndex: "f_old",
      width: "25%",
    },
    {
      title: t("mapping_scene"),
      dataIndex: "f_new",
      width: "35%",
      render(t, r) {
        return (
          <EditInput
            value={t}
            disabled={disabled || r?.f_old === "生命周期"}
            key={uuidv4()}
            onChange={v => onGovernanceTableChange(v, r.f_old)}
          />
        );
      },
    },
  ];

  const sliceList = useMemo(
    () => topList?.[gover]
      ?.sort((a, b) => b.pv - a.pv)
      ?.map((i, idx) => ({ ...i, idx })) ?? [],
    [gover, topList],
  );

  const disabled = useMemo(
    () => !topGovernance?.edit || sliceList?.length === 0,
    [topGovernance, sliceList],
  );

  const onGovernanceTableChange = (v, old) => {
    setTopList(s => ({
      ...s,
      [gover]: s?.[gover]?.map(i => (i.f_old === old ? { ...i, f_new: v } : i)),
    }));
  };

  const transitionName = (arr, key) => arr?.map(i => ({
    f_old: i?.[key],
    f_new: "",
    pv: i?.pv,
  }));

  const filterTypeName = (arr, key) => arr?.reduce(
    (cur, pre) => (pre?.f_type === key
      ? [
        ...cur,
        {
          f_old: pre?.f_old,
          f_new: pre?.f_old === pre?.f_new ? "" : pre?.f_new,
          pv: pre?.f_pv,
        },
      ]
      : cur),
    [],
  );

  // 治理列表获取
  const getMekeEntityList = async (params) => {
    setCleansingLoading(true);
    const { code, data } = await getUserLogMakeStatusEntity({
      f_upload_user: username,
      ...params,
      f_make_status:
        params?.f_make_status === "all" ? undefined : params?.f_make_status,
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

  // 原始列表获取
  const getUserLogEntity = async (params, flag) => {
    setInitialLoading(true);
    const { code, data } = await getUserLogUploadStatusEntity({
      ...params,
      f_upload_user: username,
      f_status: params.f_status === "all" ? undefined : params.f_status,
    });
    if (code === 0 && data && data.list && data.list.length) {
      setInitTable({
        total: data.total,
        list: data.list,
      });
      const item = data?.list?.find(i => i.fuploadStatus === 1);
      flag
        && item
        && onGovernanceChange(
        	{
        		...item,
        		edit: item.fuploadStatus === 1,
        	},
        	false,
        );
    } else {
      setInitTable({
        total: 0,
        list: [],
      });
    }
    setInitialLoading(false);
  };

  // 保存时进行数据治理
  const updateMakeUserLogTop = async (params) => {
    const { code, message: msg } = await updateUserLogTop({
      f_upload_user: username,
      ...params,
    });
    if (code === 1) {
      getMekeEntityList(initForm);
      setTopList(initTopList);
      message.success(msg);
    } else {
      message.error(msg);
    }
  };

  // 获取数据治理Top10
  const getMakeUserLogTop = async (uplodaName) => {
    const { code, data } = await getUserLogTop({
      f_upload_user: username,
      f_upload_name: uplodaName,
    });
    if (code === 0 && data && data.list) {
      setTopList({
        category: transitionName(data?.list?.[2] ?? [], "f_category"),
        subcategory: transitionName(data?.list?.[1] ?? [], "f_subcategory"),
        event: transitionName(data?.list?.[0] ?? [], "f_event"),
      });
    } else {
      setTopList(initTopList);
    }
    setMappingLoading(false);
  };

  // 获取数据治理完成Top10
  const getUpdateUserLogMake = async (uplodaName) => {
    const { code, data } = await getUserLogMake({
      f_upload_user: username,
      f_upload_name: uplodaName,
    });
    if (code === 0 && data && data.list) {
      setTopList({
        category: filterTypeName(data.list ?? [], "category"),
        subcategory: filterTypeName(data.list ?? [], "subcategory"),
        event: filterTypeName(data.list ?? [], "event"),
      });
    } else {
      setTopList(initTopList);
    }
    setMappingLoading(false);
  };

  // 原始列表操作
  const initialTableChange = (params) => {
    setInitialForm(s => ({
      ...s,
      ...params,
    }));
    getUserLogEntity({
      ...initialForm,
      ...params,
    });
  };

  // 治理列表操作
  const makeTableChange = (params) => {
    setMakeForm(s => ({
      ...s,
      ...params,
    }));
    getMekeEntityList({
      ...makeform,
      ...params,
    });
  };

  // 不同状态获取top10
  const onGovernanceChange = (r, f) => {
    setTopGovernance(r);
    setMappingLoading(true);
    if (r?.edit) {
      getMakeUserLogTop(r?.fuploadName);
    } else {
      getUpdateUserLogMake(r?.fuploadName);
    }
    f
      && window.scrollTo({
      	top: document.documentElement.scrollHeight,
      	behavior: "smooth",
      });
  };

  // 数据治理确认
  const makeSlice = async () => {
    const jsonArray = Object.keys(topList)?.reduce(
      (cur, pre) => cur.concat(topList?.[pre]?.map(i => ({
        f_type: pre,
        f_old: i?.f_old,
        f_new: i.f_new ? i?.f_new : i?.f_old,
        f_pv: i.pv,
      }))),
      [],
    );
    const params = {
      f_upload_name: topGovernance?.fuploadName,
      f_json_array: JSON.stringify(jsonArray),
    };
    await updateMakeUserLogTop(params);
  };

  useEffect(() => {
    if (username) {
      setTopGovernance({});
      setTopList(initTopList);
      getUserLogEntity(initialForm, true);
      getMekeEntityList(makeform);
    }
  }, [username]);

  const onFinish = async (r) => {
    const { code, data } = await getUploadNameList({
      f_upload_user: username,
    });
    if (code === 0 && data && data.list) {
      const item = data.list.find(i => i.f_upload_name === r.f_upload_name_new);
      if (item) {
        message.error(t("duplicate dataset names"));
      } else {
        const { code: c, message: msg } = await updateMakeLogName({
          ...r,
          f_upload_user: username,
          f_upload_name: editParameters?.fuploadName,
        });
        if (c === 200) {
          setEditParameters({});
          form.resetFields();
          setEditOpen(false);
          getMekeEntityList(makeform);
          message.success(msg);
        } else {
          message.error(msg);
        }
      }
    } else {
      message.error(t("failed_to_obtain"));
    }
  };

  const onCacle = () => {
    form.resetFields();
    setEditOpen(false);
  };

  return (
    <div className={style.main_content}>
      <Card
        title={t("initial_dataSet_list")}
        rigth={
          <div className={style.t_headright}>
            <div className={style.t_radioGroup}>
              {statusData?.map(item => (
                <span
                  key={uuidv4()}
                  className={classNames(
                    style.span,
                    initialForm.f_status === item.value && style.span_click,
                  )}
                  onClick={() => initialTableChange({
                    f_status: item.value,
                    page: 0,
                  })
                  }
                >
                  {item.key}
                </span>
              ))}
            </div>
            <Search
              value={initialForm.f_upload_name}
              placeholder={t("please_placeholder")}
              onChange={v => setInitialForm(s => ({ ...s, f_upload_name: v }))
              }
              onSearch={() => {
                initialTableChange({
                  page: 0,
                });
              }}
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
              initialTableChange({
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
              {statusData?.map(item => (
                <span
                  key={uuidv4()}
                  className={classNames(
                    style.span,
                    makeform?.f_make_status === item?.value && style.span_click,
                  )}
                  onClick={() => makeTableChange({
                    f_make_status: item.value,
                    page: 0,
                  })
                  }
                >
                  {item.key}
                </span>
              ))}
            </div>
            <Search
              value={makeform.f_upload_name}
              placeholder={t("please_placeholder")}
              onChange={v => setMakeForm(s => ({ ...s, f_upload_name: v }))}
              onSearch={() => {
                makeTableChange({
                  page: 0,
                });
              }}
            />
          </div>
        }
        content={
          <Table
            rowKey="id"
            dataSource={makeTable}
            className={style.tasktable}
            columns={governColumns}
            loading={cleansingLoading}
            pagination={makeform}
            onChange={(page) => {
              makeTableChange({
                page: page - 1,
              });
            }}
          />
        }
      />
      <Card
        title={
          <div style={{ position: "relative" }}>
            <p>{t("data_mapping")}</p>
            <p
              style={{
                position: "absolute",
                fontSize: "0.1rem",
                color: "gray",
                fontWeight: 400,
                width: "max-content",
                marginLeft: "0.15rem",
              }}
            >
              {t("relate_to task")}
            </p>
          </div>
        }
        rigth={
          <div className={style.t_headright}>
            <div
              className={style.t_radioGroup}
              // style={{ marginRight: "3.07rem" }}
            >
              {governanceData?.map(item => (
                <span
                  key={uuidv4()}
                  className={classNames(
                    style.span,
                    gover === item.value && style.span_click,
                  )}
                  onClick={() => {
                    setGover(item.value);
                    setMakePage(initPagination);
                  }}
                >
                  {item.key}
                </span>
              ))}
            </div>
            <Popconfirm
              title={t("Whether_to_start_slicing")}
              icon={<QuestionCircleOutlined style={{ color: "red" }} />}
              onConfirm={() => makeSlice()}
              disabled={disabled}
            >
              <Button type="primary" disabled={disabled}>
                {t("ok")}
              </Button>
            </Popconfirm>
          </div>
        }
        content={
          <Table
            rowKey="id"
            dataSource={getPagenation(sliceList, makePage.page)}
            className={style.tasktable}
            columns={governanceColumns}
            loading={mappingLoading}
            sourceLength={sliceList?.length}
            pagination={makePage}
            onChange={(page) => {
              setMakePage(s => ({
                ...s,
                page: page - 1,
              }));
            }}
          />
        }
      />
      <Modal open={editOpen} closeIcon={true} centered={true} footer={false}>
        <Form form={form} name="edit" {...formItemLayout} onFinish={onFinish}>
          <Item
            label="新数据集名称"
            name="f_upload_name_new"
            rules={[
              {
                required: true,
                message: "请输入新数据集名称",
              },
            ]}
          >
            <Input />
          </Item>
          <Item
            label="新描述"
            name="f_scene_desc_new"
            rules={[
              {
                required: true,
                message: "请输入新描述",
              },
            ]}
          >
            <TextArea rows={4} maxLength={200} showCount={true} />
          </Item>
          <div style={{ textAlign: "center" }}>
            <Button type="primary" htmlType="submit">
              {t("ok")}
            </Button>
            <Button
              type="primary"
              style={{ marginLeft: "0.2rem" }}
              onClick={onCacle}
            >
              取消
            </Button>
          </div>
        </Form>
      </Modal>
    </div>
  );
};

export default Governance;
