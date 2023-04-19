/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
const path = require("path");
const { merge } = require("webpack-merge");
const commConfig = require("./webpack.comm.js");
const chalk = require("chalk");
const ProgressBarPlugin = require("progress-bar-webpack-plugin");

module.exports = merge(commConfig, {
  mode: "development", // 开发模式，不会压缩最终代码
  devServer: {
    static: {
      directory: path.join(__dirname, "public"),
    },
    compress: false,
    port: 8018,
    client: {
      overlay: {
        errors: true, // 错误
        warnings: false, // 警告
      },
      progress: true, // 编译进度
    },
    historyApiFallback: true,
    proxy: {
      "/analytics": {
        target: "http://localhost:7004",
      },
    },
  },
  plugins: [
    new ProgressBarPlugin({
      format: `  :msg [:bar] ${chalk.green.bold(":percent")} (:elapsed s)`,
    }),
  ],
  devtool: "eval-cheap-module-source-map",
});
