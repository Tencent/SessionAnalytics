/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useEffect, useMemo, useState } from "react";
import { Tabs } from "@/components/compontents";
import { findFetchApi } from "@/utils/datamaps";
import {
  getSessionLogCntByAge,
  getSessionLogCntBySex,
  getSessionLogCntByProvince,
  getSessionLogCntByChannel,
} from "@/server/statistics";
import {
  AgeSession,
  GenderSession,
  ChannelSession,
  ProvinceSession,
} from "@/page/statistics/component";
import { useTranslation } from "react-i18next";
import style from "./index.less";
import { RightCircleOutlined, LeftCircleOutlined } from "@ant-design/icons";
import { useStore } from "@royjs/core";
import { Spin, Radio, Button } from "antd";
import { hierarchyToLv } from "@/utils";

const Component = (props) => {
  switch (props.activeKey) {
    case 1:
      return <AgeSession {...props} legendPosition="top" height="3.5rem" />;
    case 2:
      return <GenderSession {...props} legendPosition="top" />;
    case 4:
      return <ChannelSession textLength={5} labelPosition="right" {...props} />;
    case 3:
      return <ProvinceSession labelPosition="top" {...props} />;
    default:
      return <></>;
  }
};

const MapComponent = React.memo((props) => {
  const { t } = useTranslation();
  const {
    event,
    data: { data, init },
    ...rest
  } = props;
  const [value, setValue] = useState(1);
  const echartKeys = useMemo(() => {
    if (init) {
      return ["user_num", "session_num", "pv_num"];
    }
    switch (event) {
      case 1:
      case 2:
        return ["category_user", "category_session", "category_pv"];
      case 3:
      case 4:
        return ["subcategory_user", "subcategory_session", "subcategory_pv"];
      case 5:
      case 6:
        return ["event_user", "event_session", "event_pv"];
    }
  }, [event, init]);

  const option = [
    {
      label: "Session",
      value: 1,
    },
    {
      label: "PV",
      value: 2,
    },
    {
      label: t("user"),
      value: 3,
    },
  ];

  return (
    <div className={style.content_echart}>
      <Radio.Group
        value={value}
        options={option}
        className={style.content_radio}
        onChange={e => setValue(e.target.value)}
      />
      <div className={style.content_echart_comm}>
        <Component
          xKey={echartKeys[value - 1]}
          height={300}
          data={!init ? data : data?.[value - 1]}
          lableFontSize={10}
          {...rest}
        />
      </div>
    </div>
  );
});

const DataModal = (props) => {
  const {
    from = {},
    params = {},
    event,
    children,
    open = false,
    onCancel,
    apiKey,
    resetParams,
    chartType,
  } = props;
  const { t, i18n } = useTranslation();
  const { username, uploadName } = useStore(state => state.user);
  const [activeKey, setActiveKey] = useState(1);
  const [loading, setLoading] = useState(false);
  const [echartData, setEchartData] = useState({
    init: true,
    data: [],
  });
  const options = [
    {
      lkey: "f_age",
      value: 1,
      dim: "age",
      optionKey: "age",
      initApi: getSessionLogCntByAge,
    },
    {
      lkey: "f_sex",
      value: 2,
      dim: "sex",
      optionKey: "f_sex",
      initApi: getSessionLogCntBySex,
    },
    {
      lkey: "f_province",
      value: 3,
      dim: "province",
      optionKey: "f_province",
      initApi: getSessionLogCntByProvince,
    },
    {
      lkey: "f_channel",
      value: 4,
      dim: "channel",
      optionKey: "f_channel",
      initApi: getSessionLogCntByChannel,
    },
  ];

  const paramsKey = (ev, param) => {
    const { type, from, to } = param;
    switch (ev) {
      case 1:
      case 2:
        return {
          f_category_from_a: from,
          f_category_to_a: type === "to" ? to : undefined,
        };
      case 3:
      case 4:
        return {
          f_subcategory_from_a: from,
          f_subcategory_to_a: type === "to" ? to : undefined,
        };
      case 5:
      case 6:
        return {
          f_event_from_a: from,
          f_event_to_a: type === "to" ? to : undefined,
        };
      default:
        return {};
    }
  };

  const onActiveKeyChange = (k) => {
    setActiveKey(k);
  };

  const getData = async () => {
    setLoading(true);
    const { code, data } = await findFetchApi(event)[apiKey]({
      f_upload_name: uploadName,
      f_upload_user: username,
      ...from,
      ...paramsKey(event, params),
      f_dim: options?.find(i => i.value === activeKey)?.dim,
      f_click: params?.type,
    });
    if (code === 0 && data && data?.list?.length) {
      const optionsKey = options?.find(i => i.value === activeKey)?.optionKey;
      const reduceData = Object.values(data.list?.reduce((cur, pre) => {
        const key = pre?.[optionsKey];
        if (cur?.[key]) {
          const item = Object.keys(pre)
            ?.filter(i => i !== optionsKey)
            ?.reduce(
              (ck, pk) => ({ ...ck, [pk]: cur?.[key]?.[pk] + pre?.[pk] }),
              {},
            );
          return {
            ...cur,
            [key]: {
              ...item,
              [optionsKey]: pre?.[optionsKey],
            },
          };
        }
        return {
          ...cur,
          [key]: pre,
        };
      }, {}));
      setEchartData({
        init: false,
        data: reduceData,
      });
    } else {
      setEchartData({
        init: false,
        data: [],
      });
    }
    setLoading(false);
  };

  const getInitData = async () => {
    setLoading(true);
    const { code, data } = await options
      .find(i => i.value === activeKey)
      .initApi({
        f_upload_name: uploadName,
        f_upload_user: username,
      });
    if (code === 0 && data && data?.list?.length) {
      setEchartData({
        init: true,
        data: data.list?.slice(0, 3)?.reverse(),
      });
    } else {
      setEchartData({
        init: true,
        data: [],
      });
    }
    setLoading(false);
  };

  useEffect(() => {
    setEchartData([]);
    if (open && username && uploadName) {
      if (Object.keys(params)?.length) {
        getData();
      } else {
        getInitData();
      }
    }
  }, [params, activeKey, uploadName]);


  return (
    <div className={style.article}>
      {children}
      {open ? (
        <div className={style.data_content}>
          <Spin spinning={loading} delay={500}>
            <div className={style.content_top}>
              <div className={style.content_top_titleDiv}>
                <span className={style.content_top_title}>
                  {t("dimensional_distribution")}
                </span>
                <span className={style.content_top_subtitle}>
                  {t("right_click_path")}
                </span>
              </div>
              <Button type="link" onClick={() => resetParams({})}>
                {t("reset")}
              </Button>
            </div>
            <Tabs
              data={options}
              event={activeKey}
              onEventChange={onActiveKeyChange}
              padding={false}
              margin10={true}
            />
            <div className={style.titleStyle}>
              {params?.from
                ? params?.type === "from"
                  ? hierarchyToLv(i18n, params?.from)
                  : `${hierarchyToLv(i18n, params?.from)} - ${hierarchyToLv(
                    i18n,
                    params?.to,
                  )}`
                : null}
            </div>
            <MapComponent
              data={echartData}
              activeKey={activeKey}
              event={event}
            />
          </Spin>
          <RightCircleOutlined className={style.closeIcon} onClick={onCancel} />
        </div>
      ) : null}
      {(chartType == 1 && !open) || (!chartType && !open) ? (
        <LeftCircleOutlined className={style.openIcon} onClick={onCancel} />
      ) : null}
    </div>
  );
};

export default React.memo(DataModal);
