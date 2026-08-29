# ChatGPT Project Brief

> 本文件只保存长期稳定、仓库级的信息。当前任务、临时分支、SHA、测试状态和执行进度应保存在当前 Pull Request 正文中。

## 1. Project

- 项目名称：FlyVar
- GitHub 仓库：`ychenracing/flyvar`
- 默认分支：`master`
- 系统定位：面向果蝇研究群体的遗传变异数据库、查询界面和在线过滤/注释工具平台。
- 项目最终目标：让研究人员通过 Web 应用查询果蝇遗传变异，并对测序结果进行过滤和注释以辅助候选突变分析。

## 2. Purpose and Non-Goals

FlyVar 以 Java 8、Spring/Spring MVC Web 应用提供数据库查询和交互工具，使用 MySQL 保存变异数据、Redis 缓存、MongoDB 保存访问日志，并依赖 Perl/R 外部分析环境；最终以 WAR 部署到 Tomcat。

长期非目标未在仓库文档中明确。该项目不应被描述为临床诊断系统、通用物种变异平台或无需外部数据库/工具的数据自包含应用。

## 3. Architecture and Module Boundaries

- `src/main/java/cn/edu/fudan/iipl/flyvar/controller/`：HTTP 请求和页面/API 控制器。
- `service/`：业务编排。
- `dao/`：MySQL、Redis、MongoDB 等数据访问边界。
- `model/`、`vo/`、`form/`：持久模型、视图对象和输入模型。
- `security/`：Spring Security 相关配置和逻辑。
- `scheduler/`：后台调度任务。
- `src/main/resources/`：Spring、数据库、缓存、日志和应用配置。
- `src/main/webapp/`：Web 页面、静态资源和 `WEB-INF`。
- `src/test/java/`：测试代码。
- `pom.xml`：Java 版本、WAR packaging、依赖和 Maven 构建配置。

Controller 负责传输边界，service 负责业务编排，DAO 负责数据访问，Maven POM 是依赖/构建 Owner，外部数据与 ANNOVAR/R 程序不由本仓库提供。不得绕过 service/DAO 分层建立并行数据真相。

## 4. Non-Negotiable Constraints

- 项目以 Java 8 和 WAR packaging 为当前构建契约；升级必须评估 Spring、Tomcat、Servlet 和依赖兼容性。
- 变异数据库、缓存和访问日志分别依赖 MySQL、Redis 和 MongoDB；不得把缓存或日志当作变异主数据。
- ANNOVAR 需要 Perl，结果组合依赖 R；外部工具、数据库内容和数据文件不由本仓库自包含。
- 输入、查询、上传、外部进程和数据库内容应视为不可信数据，并保留 Spring Security 与验证边界。
- 仓库面向研究数据分析；不得把结果描述为临床诊断或医学建议。
- 凭据、个人数据、未授权研究数据或外部工具授权内容不得写入治理文件和 PR 模板。

## 5. Authoritative Sources

- 项目定位、功能、外部依赖和部署说明：`README.md`
- 工程约定：`AGENTS.md`
- Java、依赖、WAR 和构建：`pom.xml`
- 应用代码：`src/main/java/cn/edu/fudan/iipl/flyvar/`
- 应用配置：`src/main/resources/`
- Web 页面和部署描述符：`src/main/webapp/`
- 测试：`src/test/java/`
- 变异数据、数据库 schema、外部程序和生产配置权威来源：未完整包含或未在仓库文档中明确
- 版本和发布：`pom.xml` 版本与 Git 标签（如适用）

## 6. Standard Commands

README 指定 Maven 用于导入依赖和构建，`pom.xml` 定义 WAR packaging。标准 Maven 命令：

```bash
mvn test
mvn package
```

构建产物为 WAR，按 README 部署到 Tomcat `webapps`。MySQL、Redis、MongoDB、Perl、R、ANNOVAR 和应用配置必须另行准备；仓库未定义容器、迁移、种子数据或一键环境命令。

lint、类型检查、格式检查和独立集成测试命令未在仓库中定义。

## 7. Important Paths

- `pom.xml`：Java 8、依赖、WAR 和 Maven 构建。
- `src/main/java/cn/edu/fudan/iipl/flyvar/controller/`：Web 控制器。
- `src/main/java/cn/edu/fudan/iipl/flyvar/service/`：业务服务。
- `src/main/java/cn/edu/fudan/iipl/flyvar/dao/`：数据访问。
- `src/main/java/cn/edu/fudan/iipl/flyvar/security/`：安全边界。
- `src/main/java/cn/edu/fudan/iipl/flyvar/scheduler/`：后台任务。
- `src/main/resources/`：应用配置和资源。
- `src/main/webapp/`：页面和静态资源。
- `src/test/java/`：测试。
- `README.md`：外部依赖和部署说明。

## 8. CI and Acceptance Entry Points

- 仓库没有 `.github/workflows/`，未定义 GitHub Actions 门。
- 本地验证应遵循 `AGENTS.md` 的影响范围驱动原则。
- Java/依赖变更至少需要 `mvn test` 和 `mvn package`；数据/外部工具变更还需对应 MySQL、Redis、MongoDB、Perl、R 和 Tomcat 环境验证。
- Web 或安全变更需验证请求输入、授权、数据访问和错误路径。
- 纯文档治理只需验证 Markdown、路径、引用和 diff 范围。

## 9. Prohibited Actions

- 不得把研究平台输出描述为临床诊断或医学建议。
- 不得让 Redis 缓存或 MongoDB 访问日志成为 MySQL 变异主数据的替代 Owner。
- 不得提交凭据、个人数据、未授权研究数据或外部工具授权内容。
- 不得把未实际构建、测试或部署的 WAR 写成已验证。
- 不得擅自改写 Git 历史或 force push。
- 不得丢弃未知或未提交工作，也不得覆盖无关改动。
- 不得把计划执行写成已验证完成。
- 不得根据旧聊天猜测当前分支、SHA、PR 或 CI 状态。

## 10. Context Loading Protocol

1. 新开发任务可以直接使用自然语言提出，不要求预先填写固定 Prompt。
2. 开始任务时先读取本文件。
3. 搜索与任务相关的开放 PR、分支和 Issue。
4. 如果存在匹配工作，从现有现场原地继续。
5. 当前动态任务状态默认维护在 Pull Request 正文。
6. 不强制普通单 PR 任务创建 Issue。
7. 优先读取目标代码、直接调用者、相关测试和直接相关配置。
8. 只有证据不足、状态冲突或影响范围扩大时才扩大读取。
9. 不默认加载完整仓库、完整聊天、完整日志或全部 GitHub Actions 历史。
10. 长对话交接使用 `conversation-continuity-guard`，但 GitHub 当前现场仍是状态权威来源。

## 11. References

- `README.md`
- `AGENTS.md`
- `pom.xml`
- `src/main/java/cn/edu/fudan/iipl/flyvar/`
- `src/main/resources/`
- `src/main/webapp/`
- `src/test/java/`
