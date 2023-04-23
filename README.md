# SessionAnalytics-数据分析框架

## 功能描述
SessionAnalytics是一个基于session的用户路径分析开源项目，该框架能够减少分析师在分析前的数据开发工作，提高工作效率；
包含数据统计、桑基图分析、漏斗图分析等模块，帮助大家进行更灵活的统计分析，并进一步提升数据质量。


## 功能清单

### 用户路径分析框架模块功能：

![e2e96b06-1399-4481-bd2d-960348dc4e45](https://user-images.githubusercontent.com/34958046/233818678-8c4899e7-67b8-4460-b65c-f3e12bbaab9d.png)



### 用户行为分析：Event(事件) vs Session(会话)

![image](https://user-images.githubusercontent.com/34958046/233310766-888425d9-b772-4655-9d6b-9b0212b5e0f6.png)


### 接口设计：

![image](https://user-images.githubusercontent.com/34958046/226226390-57b3e81d-73a3-40a7-bd21-d5addb863dbc.png)

### 框架整体架构：

<img width="900" alt="db29ac61-55f4-4f03-968a-b8e021662fbd" src="https://user-images.githubusercontent.com/34958046/226226307-b2d88d5f-c0e1-41a4-91d6-59c7b2fee8e7.png">

## Demo

### 1.环境配置
安装相关的所需环境
```bash
node 14+
java 8
mysql5.7 
```
### 2.获取源码
使用git工具获取源代码。

```bash
git clone https://github.com/Tencent/SessionAnalytics.git
```
### 3..编译及运行

```bash
#react
npm i 
npm run dev
npm run build

#java
#后端数据库建表:
sql:script/session.sql 
#数据库信息添加：
application.yml #本地开发环境   
application_prod.yml #正式环境   
application_test.yml #测试环境
```

