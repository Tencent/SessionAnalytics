module.exports = {
  // 环境
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  // 依赖
  extends: [
    "eslint:recommended",
    "plugin:react/recommended",
    "@tencent/eslint-config-tencent",
  ],
  overrides: [],
  parserOptions: {
    ecmaVersion: "latest",
    sourceType: "module",
  },
  // 插件
  plugins: ["react"],
  globals: {
    process: true,
  },
  // 规则
  rules: {
    indent: ["error", 2, { SwitchCase: 1 }],
    "linebreak-style": ["error", "windows"],
    quotes: ["error", "double"],
    semi: ["error", "always"],
    "react/prop-types": 0,
    "no-mixed-spaces-and-tabs": "off",
    camelcase: "off",
    "no-useless-escape": "off",
    "react/display-name": "off",
    "no-dupe-keys": "off",
    "max-len": ["error", { code: 300 }],
  },
};
