# SessionAnalytics-数据分析框架

## 导语
SessionAnalytics是一个基于互联网用户Session会话的用户路径分析和挖掘框架，综合利用OLAP、数据挖掘、数据可视化等前沿技术，在互联网业务的用户流量和路径分析中，为产品、运营、商业化等企业数据用户提供强大和友好的数据洞察功能。在数据治理、数据分析、数据挖掘等场景，大幅提升数据科学家和工程师的工作效率。

![image](https://user-images.githubusercontent.com/34958046/234150610-4acb2e96-2738-4e0a-bada-32c95d40862f.png)


感谢项目核心成员Eric@ericliuyuan、Jane@jane、Qilin@sessionpath、Cibin@zcb1008 在需求分析、产品设计、挖掘算法、系统开发等关键环节的深度技术探索和优化。

感谢数据科学家Haley@yanhuiru33、Luciano@BigPlato、Laura@jihl97 提供宝贵的算法和模型优化建议。

感谢Binxi、Ella、Nini、Leo、Janice 等同学在项目过程中提出的宝贵意见。

## 项目特点一：覆盖挖掘、治理、洞察的全链路

### 智能数据挖掘：
支持Kmeans、DTW、中心性分析等多种机器学习算法，为用户提供一站式建模及可视化体验，适用于多种业务场景，助力用户挖掘数据价值；

### 标准数据治理：
实现了数据治理方法论和产品功能的结合，自动进行数据质量校验，解决原始数据脏、乱、差的问题，助力用户沉淀高质量的数据资产；

### 丰富的可视化：
深度定制和优化桑基图、漏斗图、和弦图、树状图等多种可视化图表，灵活应对各种分析场景；



## 项目特点二：灵活高效的交互和操作

### 一站式数据操作：
打通了数据上传、数据处理、数据建模、数据分析全链路，提供全能型数据分析解决方案，轻松应对复杂的用户路径全链路分析需求；

### 多样化数据交互：
支持路径层级、路径节点、Session数量等多种维度，用户可自主筛选，探索式分析数据；


### 多种数据源连接：
支持MySQL、Clickhouse等多种数据源，打通数据孤岛，保障数据查询效率，让更多数据应用于数据分析和数据管控；



## 项目特点三：优化的计算和查询性能

### 查询高性能：
数据库设计采用了读写分离、分库分表和冷热分离的方式，并引入了多级缓存架构；同时支持多种数据引擎，实现亿级数据毫秒级响应；

### 系统高可用：
基础设施层采用了多IDC+异地多活部署方式；同时针对服务治理，设计了超时、熔断、降级、限流等措施，能在最大程度上保障系统可用性；

### 支持高并发：
采用分布式架构设计，并在应用层引入多线程和异步操作方式，保证系统高并发调用；


## 框架整体架构：

<img width="900" alt="db29ac61-55f4-4f03-968a-b8e021662fbd" src="https://user-images.githubusercontent.com/34958046/226226307-b2d88d5f-c0e1-41a4-91d6-59c7b2fee8e7.png">

### 用户路径分析框架模块功能：

![e2e96b06-1399-4481-bd2d-960348dc4e45](https://user-images.githubusercontent.com/34958046/233818678-8c4899e7-67b8-4460-b65c-f3e12bbaab9d.png)


### 用户行为分析：Event(事件) vs Session(会话)

![image](https://user-images.githubusercontent.com/34958046/233310766-888425d9-b772-4655-9d6b-9b0212b5e0f6.png)


### 接口设计：

![image](https://user-images.githubusercontent.com/34958046/226226390-57b3e81d-73a3-40a7-bd21-d5addb863dbc.png)



## 环境

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


## 未来规划：

### 算法提升：
结合chatGPT等LLM大语言模型的推理能力，基于数据治理模块生成的高质量真实数据，应用业界领先的机器学习算法，不断提升系统的智能化程度；

### 性能优化：
支持多种高性能数据引擎，不断提升数据计算和查询效率；

### 功能迭代：
引入更多数据挖掘模型和功能模块，不断提升数据科学家、数据工程师等数据用户的工作效率；

