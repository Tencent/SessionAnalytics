/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useState, useEffect } from "react";
import style from "@/style/comm.less";
import classNames from "classnames";
import { useTranslation } from "react-i18next";
import { Table, EchartCard, Card } from "@/components/compontents";
import {
  getRecentAcessUV,
  getRecentAcessPV,
  getRecentAcessFrequency,
  getRecentAcessRecordList,
} from "@/server/trend";
import { DatePicker, Input } from "antd";
import moment from "moment";

const { RangePicker } = DatePicker;
const { Search } = Input;
const format = "YYYY-MM-DD";

const Management = () => {
  const { t } = useTranslation();

  const initDate = [moment().subtract(1, "month"), moment()];
  const initPageNation = {
    page: 0,
    page_size: 10,
  };
  const [rangeDate, setRangeDate] = useState(initDate);
  const [pageNation, setPageNation] = useState(initPageNation);
  const [combo, setCombo] = useState({
    users: 0,
    uv: 0,
    pv: 0,
  });
  const [user, setUser] = useState("");
  const [userList, setUserList] = useState([]);
  const [detail, setDetail] = useState({
    total: 0,
    list: [],
  });

  const getUv = async (params, key) => {
    const { code, data } = await getRecentAcessUV(params);
    if (code === 0 && data) {
      setCombo(s => ({
        ...s,
        [key]: data?.list?.[0]?.uv,
      }));
    } else {
      setCombo(s => ({
        ...s,
        [key]: 0,
      }));
    }
  };

  const getPv = async (params) => {
    const { code, data } = await getRecentAcessPV(params);
    if (code === 0 && data) {
      setCombo(s => ({
        ...s,
        pv: data?.list?.[0]?.pv,
      }));
    } else {
      setCombo(s => ({
        ...s,
        pv: 0,
      }));
    }
  };

  const getUserList = async (params) => {
    const { code, data } = await getRecentAcessFrequency(params);
    if (code === 0 && data && data?.list) {
      setUserList(data?.list);
    } else {
      setUserList([]);
    }
  };

  const getDetailList = async (params) => {
    const { code, data } = await getRecentAcessRecordList(params);
    if (code === 0 && data && data?.list) {
      setDetail({
        total: data?.total,
        list: data.list,
      });
    } else {
      setDetail({
        total: 0,
        list: [],
      });
    }
  };

  const commRequest = (date) => {
    const params = {
      f_start_time: moment(date[0]).format(format),
      f_end_time: moment(date[1]).format(format),
    };
    getUv(params, "uv");
    getPv(params);
    getUserList(params);
    getDetailList({
      ...params,
      ...initPageNation,
      f_upload_user: user,
    });
  };

  useEffect(() => {
    getUv({}, "users");
    commRequest(initDate);
  }, []);

  const userColumns = [
    {
      title: t("user"),
      dataIndex: "f_admin_user",
    },
    {
      title: t("pv"),
      dataIndex: "pv",
      sorter: (a, b) => a.pv - b.pv,
      render: m => m?.toLocaleString(),
    },
  ];

  const detailColumns = [
    {
      title: t("user"),
      dataIndex: "fadminUser",
    },
    {
      title: t("first_directory"),
      dataIndex: "fanalysisNameFirst",
      render: m => t(m),
    },
    {
      title: t("second_directory"),
      dataIndex: "fanalysisNameSecond",
      render: m => t(m),
    },
    {
      title: t("access_time"),
      dataIndex: "faccessTime",
    },
  ];

  const onRangeChange = (date) => {
    setRangeDate(date);
    setPageNation(initPageNation);
    commRequest(date);
  };

  const onUserChange = (u) => {
    setUser(u);
    setPageNation(initPageNation);
    getDetailList({
      ...initPageNation,
      f_admin_user: u,
      f_start_time: moment(rangeDate[0]).format(format),
      f_end_time: moment(rangeDate[1]).format(format),
    });
  };

  const onPageSizeChange = (page, page_size) => {
    const params = {
      page: page - 1,
      page_size,
    };
    setPageNation(params);
    getDetailList({
      ...params,
      f_start_time: moment(rangeDate[0]).format(format),
      f_end_time: moment(rangeDate[1]).format(format),
      f_admin_user: user,
    });
  };

  return (
    <div className={style.main_content}>
      <Card
        content={
          <div className={style.range}>
            <RangePicker
              allowClear={false}
              value={rangeDate}
              format={format}
              onChange={onRangeChange}
            />
          </div>
        }
      />
      <EchartCard height="1.46rem">
        <div className={classNames(style.combo, style.maCombo)}>
          <div className={style.maCombo_item}>
            <p className={style.combo_title}>{t("total_users")}</p>
            <p className={style.combo_value}>{combo?.uv?.toLocaleString()}</p>
          </div>
          <div className={style.maCombo_item}>
            <p className={style.combo_title}>{t("total_uv")}</p>
            <p className={style.combo_value}>{combo?.uv?.toLocaleString()}</p>
          </div>
          <div className={style.maCombo_item}>
            <p className={style.combo_title}>{t("total_pv")}</p>
            <p className={style.combo_value}>{combo?.pv?.toLocaleString()}</p>
          </div>
        </div>
      </EchartCard>
      <EchartCard title={t("high_frequency_user")}>
        <Table columns={userColumns} dataSource={userList} pagination={false} />
      </EchartCard>
      <Card
        title={t("access_details")}
        rigth={
          <Search
            allowClear
            placeholder={t("please_enter_user")}
            style={{ width: "2.6rem" }}
            onSearch={onUserChange}
          />
        }
        content={
          <div className={style.detail_table}>
            <Table
              columns={detailColumns}
              dataSource={detail}
              pagination={pageNation}
              onChange={onPageSizeChange}
            />
          </div>
        }
      />
    </div>
  );
};

export default Management;
