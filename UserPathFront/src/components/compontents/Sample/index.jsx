/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React, { useMemo, useState } from "react";
import style from "./index.less";
import { CaretUpOutlined, CaretDownOutlined } from "@ant-design/icons";

const Sample = ({ data = [] }) => {
  const [index, setIndex] = useState(0);

  const menuData = useMemo(() => {
    const res = [];
    for (let index = 0; index < data.length; index += 6) {
      res.push(data.slice(index, index + 6));
    }
    return res;
  }, [data]);

  return (
    <div className={style.scrolling}>
      <div className={style.innerMenu}>
        {menuData[index]?.map(item => (
          <div className={style.innerMenu_item} key={item.value}>
            <div style={{ backgroundColor: item.color }} />
            <div>{item.value}</div>
          </div>
        ))}
      </div>
      <div className={style.scrolling_icon}>
        <CaretUpOutlined
          className={style.arrowLeft}
          onClick={() => setIndex(s => (s > 0 ? s - 1 : s))}
          style={{ color: index === 0 ? "#d9d9d9" : "#000" }}
        />
        <div>
          {index + 1}/{menuData?.length}
        </div>
        <CaretDownOutlined
          className={style.arrowRigth}
          style={{ color: index === menuData?.length - 1 ? "#d9d9d9" : "#000" }}
          onClick={() => setIndex(s => (s < menuData?.length - 1 ? s + 1 : s))
          }
        />
      </div>
    </div>
  );
};

export default React.memo(Sample);
