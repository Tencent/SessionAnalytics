# Contributing

我们提倡您通过提 issue 和 pull request 方式来促进 SessionAnalytics-用户路径分析框架 的发展。


## Acknowledgements

基于海量数据的互联网用户流量运营和数据分析挖掘，一直是数据工程和数据科学团队最重要的工作之一，也长期是团队小伙伴们的难题。我们调研了国内外数据挖掘相关框架和系统，大多基于用户事件，缺少了针对用户路径相关智能分析的闭环和落地。基于此，我们开发了SessionAnalytics用户路径分析框架，也衷心希望有越来越多的小伙伴能使用和共同开发，一起打造一个更智能更好用的分析系统。

感谢项目核心成员ericyliu(刘源)、janebai(白洁)、qilinfu(付其林)、v_vzcbzhang(章赐彬) 在需求分析、产品设计、挖掘算法、系统开发等关键环节的深度技术探索和优化。

感谢数据科学家haleyyan(闫慧茹)、lucianowang(王君毅)、lauraji(纪鸿璐) 提供宝贵的算法和模型优化建议。

感谢binxiwu(吴彬熙)、ellaxiong(熊海亚)、niniplwu(吴佩琳)、leohylan(兰宏泳)、janicezhu(朱秋红) 等同学在项目过程中提出的宝贵意见。
🍾🎉

​                       

## Issue 提交

#### 对于贡献者

在提 issue 前请确保满足一下条件：

- 必须是一个 bug 或者功能新增。
- 必须是 SessionAnalytics 相关问题，已经在 issue 中搜索过，并且没有找到相似的 issue 或者解决方案。
- 完善下面模板中的信息

如果已经满足以上条件，我们提供了 issue 的标准模版，请按照模板填写。

​             

##  Pull request

我们除了希望听到您的反馈和建议外，我们也希望您接受代码形式的直接帮助，对我们的 GitHub 发出 pull request 请求。

以下是具体步骤：

#### Fork仓库

点击 `Fork` 按钮，将需要参与的项目仓库 fork 到自己的 Github 中。

#### Clone 已 fork 项目

在自己的 github 中，找到 fork 下来的项目，git clone 到本地。

```bash
$ git clone git@github.com:<yourname>/SessionAnalytics.git
```

#### 添加 SessionAnalytics-用户路径分析框架 仓库

将 fork 源仓库连接到本地仓库：

```bash
$ git remote add <name> <url>
```

#### 保持与 SessionAnalytics-用户路径分析框架 仓库的同步

更新上游仓库：

```bash
$ git pull --rebase <name> <branch>
# 等同于以下两条命令
$ git fetch <name> <branch>
$ git rebase <name>/<branch>
```

#### commit 信息提交

commit 信息请遵循[commit消息约定](CONTRIBUTING_COMMIT.md)，以便可以自动生成 `CHANGELOG` 。具体格式请参考 commit 文档规范。



#### 开发调试代码

```bash
# Build code
$ npm run build:pub

# Watch
$ npm install
$ npm run dev


#java
#后端数据库建表:
sql:script/session.sql 
#数据库信息添加：
application.yml #本地开发环境   
application_prod.yml #正式环境   
application_test.yml #测试环境
```
