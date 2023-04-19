/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
// 分别未获取用户人信息 获取上传人列表 获取上传列表 是否为管理员
// 国际化
import { getUploadUserList, getUploadUserExist } from "@/server/user";
import { getUploadNameDoneList } from "@/server/path";
import { getUserInfo } from "@/utils";

export default {
  state: {
    username: "",  // 上传人
    userInfo: getUserInfo(),
    token: getUserInfo()?.token,
    isadmin: Number(getUserInfo()?.isadmin ?? 0) === 1,  // 是否为管理员
    userList: [],  // 上传人列表
    option: [],   // 上传列表
    uploadName: "", // 上传文件名
    navList: ["upload", "data_cleansing", "data_slice", "data_query", "statistics", "sankey", "funnel"], // 导航列表
    language: "ALL", // 语言
  },
  actions: {
    // 获取用户信息
    setUserInfo(state, userInfo) {
      localStorage.setItem("userInfo", JSON.stringify(userInfo));
      Object.assign(state, {
        userInfo,
        token: userInfo?.token,
        isadmin: Number(userInfo?.isadmin ?? 0) === 1,
      });
    },
    logOut(state) {
      localStorage.removeItem("userInfo");
      Object.assign(state, {
        userInfo: null,
        username: "",
        token: "",
        isadmin: false,
      });
    },

    // 获取上传人列表及上传人
    async getUserInfo(state, language) {
      const languageUser = language === "cn" ? "demo" : "demo_en";
      let username = state?.userInfo?.username;
      const { code } = await getUploadUserExist({
        f_upload_user: username,
      });
      if (code === 0) {
        username = languageUser;
      }
      if (state.isadmin) {
        this.dispatch("user.getUploadUsers", username);
      } else {
        state.userList = [];
        this.dispatch("user.getUploadNameList", username);
      }
      state.username = username;
    },
    async getUploadUsers(state, username) {
      const { code, data } = await getUploadUserList({
        username: state?.userInfo?.username,
      });
      if (code === 1 && data && data.length) {
        state.userList = data?.map(i => ({
          label: i.f_upload_user,
          value: i.f_upload_user,
        }));
      } else {
        state.userList = [];
      }
      this.dispatch("user.getUploadNameList", username);
    },
    async getUploadNameList(state, username) {
      const { code, data } = await getUploadNameDoneList({
        f_upload_user: username,
      });
      if (code === 0 && data && data.list) {
        state.option = data.list.map(i => ({
          label: i.f_upload_name,
          value: i.f_upload_name,
        }));
        Object.assign(state, {
          uploadName: data?.list[0]?.f_upload_name,
        });
      } else {
        state.option = [];
        Object.assign(state, {
          uploadName: "",
        });
      }
    },
    onChangeFieldsName(state, name) {
      Object.assign(state, {
        uploadName: name,
      });
    },
    onChangeUserName(state, name) {
      state.username = name;
      this.dispatch("user.getUploadNameList", name);
    },
  },
};
