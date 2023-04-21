/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const webpack = require("webpack");
// rem布局插件
const px2rem = require("postcss-plugin-px2rem");
// 判断环境
const isDev = process.env.NODE_ENV === "development";

// rem布局包含文件及最小size
const px2remOpts = {
  mediaQuery: true,
  minPixelValue: 0,
  // 不包含user文件夹
  exclude: "src/page/user/index.less",
};

module.exports = {
  performance: {
    hints: false,
  },
  // 入口文件
  entry: path.resolve(__dirname, "../src/index.jsx"),
  // 打包出口文件
  output: {
    filename: "js/[name].[chunkhash:8].js", // 每个输出js的名称
    path: path.resolve(__dirname, "../dist"), // 打包的出口文件夹路径
    clean: true, // webpack4需要配置clean-webpack-plugin删除dist文件，webpack5内置了。
    publicPath: "/", // 打包后文件的公共前缀路径
  },
  resolve: {
    extensions: [".js", ".jsx"],
    alias: {
      "@": path.resolve(__dirname, "../src"),
      "@pub": path.resolve(__dirname, "../public"),
    },
    fallback: {
      buffer: require.resolve("buffer"),
      path: require.resolve("path-browserify"),
    },
    // modules: [path.resolve(__dirname, '../node_modules')],
  },
  // 插件
  plugins: [
    // html 文件模板 及网站icon
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, "../public/index.html"),
      inject: true,
      favicon: path.resolve(__dirname, "../public/favicon.ico"),
    }),
    // 内置变量
    new webpack.DefinePlugin({
      "process.env": {
        NODE_ENV: JSON.stringify(process.env.NODE_ENV),
      },
    }),
    // node变量
    new webpack.ProvidePlugin({
      Buffer: ["buffer", "Buffer"],
    }),
  ],
  // module rules
  module: {
    rules: [
      {
        test: /\.css$/, // 匹配所有的 css 文件
        use: [
          isDev ? "style-loader" : MiniCssExtractPlugin.loader,
          "css-loader",
          {
            loader: "postcss-loader",
            options: {
              postcssOptions: {
                ident: "postcss",
                config: false,
                plugins: [px2rem(px2remOpts)],
              },
            },
          },
        ],
      },
      {
        test: /\.less$/i,
        use: [
          isDev ? "style-loader" : MiniCssExtractPlugin.loader,
          {
            loader: "css-loader",
            options: {
              modules: true,
              sourceMap: true,
            },
          },
          {
            loader: "less-loader",
            options: {
              lessOptions: {
                javascriptEnabled: true,
              },
            },
          },
          {
            loader: "postcss-loader",
            options: {
              postcssOptions: {
                ident: "postcss",
                config: false,
                plugins: [px2rem(px2remOpts)],
              },
            },
          },
        ],
      },
      {
        test: /\.(png|jpg|jpeg|gif|svg)$/,
        type: "asset",
        generator: {
          filename: "img/[hash][ext][query]", // 局部指定输出位置
        },
      },
      {
        test: /\.(jsx|js)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ["@babel/preset-env"],
            plugins: ["@babel/plugin-transform-runtime"],
          },
        },
      },
    ],
  },
  cache: {
    type: "filesystem", // 使用文件缓存
  },
};
