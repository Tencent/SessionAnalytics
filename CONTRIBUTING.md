# Contributing

我们提倡您通过提 issue 和 pull request 方式来促进 SessionAnalytics-用户路径分析框架 的发展。


## Acknowledgements

SessionAnalytics-用户路径分析框架 持续招募贡献者，即使是在 issue 中回答问题，或者做一些简单的 bugfix ，也会给 SessionAnalytics 带来很大的帮助。

SessionAnalytics-用户路径分析框架 已开发近一年，在此感谢所有开发者对于 该框架 的喜欢和支持，希望你能够成为 该框架 的核心贡献者，加入我们 ，共同打造一个更棒的路径分析系统！🍾🎉

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
