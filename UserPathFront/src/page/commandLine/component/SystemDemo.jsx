/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import { useTranslation } from "react-i18next";
import { Card } from "@/components/compontents";
import circles from "@/images/circles.svg";
import classNames from "classnames";
import processGuide1 from "@/images/demo/processGuide1.png";
import processGuide2 from "@/images/demo/processGuide2.png";
import processGuide3 from "@/images/demo/processGuide3.png";
import processGuide4 from "@/images/demo/processGuide4.png";
import processGuide5 from "@/images/demo/processGuide5.png";
import enprocessGuide1 from "@/images/demo/enprocessGuide1.png";
import enprocessGuide2 from "@/images/demo/enprocessGuide2.png";
import enprocessGuide3 from "@/images/demo/enprocessGuide3.png";
import enprocessGuide4 from "@/images/demo/enprocessGuide4.png";
import enprocessGuide5 from "@/images/demo/enprocessGuide5.png";
import style from "../index.less";

const SystemDemon = () => {
  const { t, i18n } = useTranslation();
  const stepData = [
    {
      st: t("the_first_step"),
      st1: t("Go_to_the_Upload_data_page"),
    },
    {
      st: t("step_two"),
      st1: t("and_upload_CSV_file"),
    },
    {
      st: t("step_three"),
      st1: t("wait_task_execute"),
    },
    {
      st: t("step_four"),
      st1: t("task_execution_completes"),
    },
    {
      st: t("step_five"),
      st1: t("review_the_results"),
    },
  ];

  const processData = [
    t("data_upload"),
    t("wait_for_the_task"),
    t("view_notifications"),
    t("view_the_analytics"),
  ];


  const ImgContent = ({ index, imgData }) => (
    <div className={style.imgcontent}>
      <div className={style.processBox}>
        {processData.map((item, idx) => (
          <div
            className={classNames(
              style.processItem,
              idx < index && style.click,
            )}
            key={item}
          >
            {item}
          </div>
        ))}
      </div>
      <div className={style.imgbox}>
        {imgData.map(item => (
          <img src={item} key={item} />
        ))}
      </div>
    </div>
  );


  return (
    <div>
      <Card
        title={t("process_guidance")}
        content={
          <div className={style.step}>
            {stepData.map(item => (
              <div className={style.step_item} key={item.st1}>
                <img src={circles} />
                <div className={style.step_item_line} />
                <div className={style.step_item_content}>
                  <p>{item.st}</p>
                  <p>{item.st1}</p>
                  <p>{item.st2}</p>
                </div>
              </div>
            ))}
          </div>
        }
      />
      <Card
        title={t("operation_example")}
        content={
          <div>
            <Card
              content={
                <ImgContent
                  index={1}
                  imgData={[
                    i18n.language === "cn" ? processGuide1 : enprocessGuide1,
                    i18n.language === "cn" ? processGuide2 : enprocessGuide2,
                  ]}
                />
              }
            />
            <Card
              content={
                <ImgContent
                  index={2}
                  imgData={[
                    i18n.language === "cn" ? processGuide3 : enprocessGuide3,
                  ]}
                />
              }
            />
            <Card
              content={
                <ImgContent
                  index={3}
                  imgData={[
                    i18n.language === "cn" ? processGuide4 : enprocessGuide4,
                  ]}
                />
              }
            />
            <Card
              content={
                <ImgContent
                  index={4}
                  imgData={[
                    i18n.language === "cn" ? processGuide5 : enprocessGuide5,
                  ]}
                />
              }
            />
          </div>
        }
      />
    </div>
  );
};

export default SystemDemon;
