/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useState } from "react";
import EchartCard from "@/components/compontents/EchartCard";
import { Table } from "@/components/compontents";
import { useTranslation } from "react-i18next";
import { depend } from "@/utils/datamaps";

const CommTable = (props) => {
  const initPagination = {
    page: 1,
    page_size: 10,
  };
  const initTableData = {
    total: 0,
    list: [],
  };

  const { params, username, exct } = props;
  const [sessionDataSource, setSessionDataSource] = useState(initTableData);
  const [pvDataSource, setPvDataSource] = useState(initTableData);
  const [sessionPagination, setSessionPagination] = useState({
    initPagination,
  });
  const [pvPagination, setPvPagination] = useState({
    initPagination,
  });
  const { t } = useTranslation();

  const [sessionLoading, setSessionLoading] = useState(false);
  const [pvLoading, setPvLoading] = useState(false);

  const sessionColumns = [
    {
      title: t(exct.title),
      dataIndex: "f_dim",
      sorter: (a, b) => a[exct.par].localeCompare(b[exct.par]),
    },
    {
      title: t("number_of_users"),
      dataIndex: "f_user_num",
      sorter: (a, b) => a.user_num - b.user_num,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("totals"),
      dataIndex: "f_session_num",
      sorter: (a, b) => a.session_num - b.session_num,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("maximum"),
      dataIndex: "f_session_max",
      sorter: (a, b) => a.session_max - b.session_max,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("minimum"),
      dataIndex: "f_session_min",
      sorter: (a, b) => a.session_min - b.session_min,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("average"),
      dataIndex: "f_session_avg",
      sorter: (a, b) => a.session_avg - b.session_avg,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("median"),
      dataIndex: "f_session_50p",
      sorter: (a, b) => a.session_50p - b.session_50p,
      render: m => m?.toLocaleString(),
    },
  ];

  const pvColumns = [
    {
      title: t(exct.title),
      dataIndex: "f_dim",
      sorter: (a, b) => a[exct.par]?.localeCompare(b[exct.par]),
    },
    {
      title: t("number_of_users"),
      dataIndex: "f_user_pv_num",
      sorter: (a, b) => a.user_pv_num - b.user_pv_num,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("totals"),
      dataIndex: "f_session_pv_num",
      sorter: (a, b) => a.session_pv_num - b.session_pv_num,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("maximum"),
      dataIndex: "f_session_pv_max",
      sorter: (a, b) => a.session_pv_max - b.session_pv_max,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("minimum"),
      dataIndex: "f_session_pv_min",
      sorter: (a, b) => a.session_pv_min - b.session_pv_min,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("average"),
      dataIndex: "f_session_pv_avg",
      sorter: (a, b) => a.session_pv_avg - b.session_pv_avg,
      render: m => m?.toLocaleString(),
    },
    {
      title: t("median"),
      dataIndex: "f_session_pv_50p",
      sorter: (a, b) => a.session_pv_50p - b.session_pv_50p,
      render: m => m?.toLocaleString(),
    },
  ];

  const getSessionTableData = async (params) => {
    setSessionLoading(true);
    const { code, data } = await exct.sessionTableApi(params);
    if (code === 0 && data && data?.list) {
      setSessionDataSource({
        total: data.total,
        list: data?.list,
      });
    } else {
      setSessionDataSource({});
    }
    setSessionLoading(false);
  };

  const getPvTableData = async (params) => {
    setPvLoading(true);
    const { code, data } = await exct.pvTableApi(params);
    if (code === 0 && data && data?.list) {
      setPvDataSource({
        total: data.total,
        list: data?.list,
      });
    } else {
      setPvDataSource({});
    }
    setPvLoading(false);
  };

  useEffect(() => {
    setSessionDataSource([]);
    setPvDataSource([]);
    if (params?.f_upload_name && username) {
      setPvPagination(initPagination);
      setSessionPagination(initPagination);
      getSessionTableData({
        ...params,
        ...initPagination,
        f_upload_user: username,
      });
      getPvTableData({
        ...params,
        ...initPagination,
        f_upload_user: username,
      });
    }
  }, [...depend(params), exct]);

  const onSessionPageChange = (v) => {
    setSessionPagination(s => ({
      ...s,
      ...v,
    }));
    getSessionTableData({
      ...params,
      ...sessionPagination,
      ...v,
      f_upload_user: username,
    });
  };

  const onPageChange = (v, set, getApi) => {
    set(s => ({
      ...s,
      ...v,
    }));
    getApi({
      ...params,
      ...sessionPagination,
      ...v,
      f_upload_user: username,
    });
  };

  return (
    <>
      <EchartCard title={t("session_analytics")}>
        <Table
          columns={sessionColumns}
          dataSource={sessionDataSource}
          pagination={sessionPagination}
          loading={sessionLoading}
          current={false}
          onChange={(page) => {
            onSessionPageChange(
              {
                page,
              },
              setSessionPagination,
              getSessionTableData,
            );
          }}
        />
      </EchartCard>
      <EchartCard title={t("PV_analytics")}>
        <Table
          columns={pvColumns}
          dataSource={pvDataSource}
          pagination={pvPagination}
          loading={pvLoading}
          current={false}
          onChange={(page) => {
            onPageChange(
              {
                page,
              },
              setPvPagination,
              getPvTableData,
            );
          }}
        />
      </EchartCard>
    </>
  );
};

export default CommTable;
