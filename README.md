# Drools 规则引擎技术内幕：示例工程

本工程是《**Drools 规则引擎技术内幕：核心算法、源码实现与架构全景**》的配套示例代码。

它不是单一业务应用，而是一个面向学习和实验的 Maven 工程，包含：

- 手写的 Rete、面向对象 Rete 和 Phreak 惰性求值模拟；
- Drools KIE、DRL、查询、约束、Global、DSL、规则模板、规则流等功能示例；
- DMN、PMML、日历、通道、规则监听器等扩展示例；
- `KieSession` 生命周期、并发访问、池化、KieBase 更新和 Fact 累积的 S1–S5 场景程序。

## 1. 环境与版本

| 项目 | 版本/要求 |
|---|---|
| Maven 坐标 | `org.example:droolsBookDemo:1.0-SNAPSHOT` |
| Java 编译目标 | Java 8 |
| 源码编码 | UTF-8 |
| Drools | `7.40.0.Final` |
| JUnit | `4.11` |
| JMH 依赖 | `1.36` |

主要依赖定义在 [`pom.xml`](pom.xml)：

- `drools-compiler`：DRL 编译与规则运行；
- `drools-decisiontables`：决策表支持；
- `kie-dmn-api` / `kie-dmn-core` / `kie-dmn-feel`：DMN 支持；
- `kie-pmml`：PMML 支持；
- `kie-ci`：KIE 模块与版本更新能力；
- `jmh-core` / `jmh-generator-annprocess`：JMH 依赖和注解处理器。

> 建议使用 JDK 8 运行。工程将 Maven 的 `source`、`target` 设置为 8；请避免使用 Java 9+ 的语言/API 特性编写新的示例。

## 2. 阅读路线

推荐按以下顺序阅读。

```text
1. 手写算法实现
   Rete → ReteOO → Phreak

2. Drools 基础 API 与 DRL
   Hello World → 规则文件 → 查询/Global/Function → 约束

3. 扩展能力
   DSL → 规则模板/决策表 → 规则流 → 日历/通道/监听器 → DMN/PMML

4. 会话生命周期与并发场景
   S1 → S2 → S3 → S4 → S5
```

## 3. 工程结构

```text
.
├── pom.xml
├── src/main/java/
│   ├── com/drools/
│   │   ├── algorithm/
│   │   │   ├── rete/          # 手写 Rete 网络
│   │   │   ├── reteOO/        # 面向对象的 Rete 网络
│   │   │   ├── pherak/        # Phreak 链接、分段内存、惰性求值模拟
│   │   │   └── ReteNetworkPrinter.java
│   │   └── benchmark/
│   │       ├── config/        # 内存 KIE 模块构建与场景配置
│   │       ├── correctness/  # S1-S5 可直接运行的场景程序
│   │       └── model/         # 并发场景使用的 Order Fact
│   └── com/test/drools/
│       ├── calendar/          # 日历
│       ├── channel/           # 通道
│       ├── commonly/          # DSL、规则模板、规则流等
│       ├── constraint/        # DRL 约束与操作符
│       ├── dmn/               # DMN
│       ├── filewhole/         # 规则文件、查询、Global、Declare 等
│       ├── helloworld/        # KIE 基础示例
│       ├── listener/          # 规则运行事件监听器
│       └── pojo/              # 示例 Fact/POJO
└── src/main/resources/
    ├── META-INF/kmodule.xml   # 常规 Drools 教学示例的 KIE 配置
    ├── kmodule.xml            # S1-S5 内存模块使用的 KIE 配置
    ├── benchmark/
    │   ├── order-rules.drl          # S1-S5 初始规则
    │   └── order-rules-update.drl   # S4 更新后的规则集
    ├── dmn/                   # DMN 模型
    ├── pmml/                  # PMML 模型
    └── rules/                 # 常规 DRL、DSL、决策表、规则流等资源
```

## 4. 核心模块说明

### 4.1 `com.drools.algorithm`：算法教学实现

| 模块 | 说明 |
|---|---|
| `rete` | 手写对象类型节点、Alpha 节点、Beta 节点、规则与动作，帮助理解 Rete 基本传播过程。 |
| `reteOO` | 用 `Fact`、`FactTuple`、`JoinNode`、`TerminalNode`、`WorkingMemory` 表达面向对象的 Rete 网络。 |
| `pherak` | 包名沿用工程现状（`pherak`），演示 Phreak 的链接管理、分段内存和惰性求值思想。 |
| `ReteNetworkPrinter` | 使用 Drools 内部实现打印 ReteOO 网络，并生成网络 dump。 |

可直接运行的算法入口：

```text
com.drools.algorithm.rete.ReteExample
com.drools.algorithm.reteOO.ReteExample
com.drools.algorithm.pherak.PhreakDemo
com.drools.algorithm.ReteNetworkPrinter
```

### 4.2 `com.test.drools`：Drools API 与 DRL 功能示例

该包按功能划分，典型主题包括：

| 目录 | 主题 |
|---|---|
| `helloworld` | `KieServices`、`KieContainer`、命名 `KieSession`、事实插入。 |
| `filewhole` | DRL 文件结构、Function、Query、Declare、Global。 |
| `constraint` | 字段约束、集合约束、`from`、Entry Point、关键字、规则名称等。 |
| `commonly` | DSL、规则模板、决策表、规则流。 |
| `calendar` | 日历规则。 |
| `channel` | Drools Channel。 |
| `listener` | Fact 与规则运行事件监听器。 |
| `dmn` | DMN 模型执行。 |
| `pojo` | 示例 Fact 对象。 |

其中部分示例采用 `main`，部分在源码中采用 JUnit 4 的 `@Test` 方法。由于这些类位于 `src/main/java`，请在 IDE 中直接运行指定 `main` 或指定 JUnit 方法，不要假定 Maven Surefire 会自动发现它们。

### 4.3 资源与 KIE 配置

#### 常规教学示例

[`src/main/resources/META-INF/kmodule.xml`](src/main/resources/META-INF/kmodule.xml) 是常规教学示例的类路径 KIE 配置，声明了多组 `KieBase` / `KieSession`，覆盖：

```text
Hello World、完整规则文件、Query、Declare、Global、约束、Calendar、Channel、
DSL、规则模板、规则流、Fact、Stateless Session、Rule Unit。
```

#### S1-S5 场景模块

[`src/main/resources/kmodule.xml`](src/main/resources/kmodule.xml) 专门供并发场景使用，定义：

```xml
<kbase name="benchmarkKieBase" packages="benchmark">
    <ksession name="orderSession"/>
    <ksession name="orderSessionPool"/>
</kbase>
```

它位于资源根目录，不是 Drools 自动查找的标准 `META-INF/kmodule.xml` 路径。场景配置类 [`BenchmarkConfig`](src/main/java/com/drools/benchmark/config/BenchmarkConfig.java) 会显式读取它，再写入内存 KIE 模块的标准路径后编译。因此 S1-S5 使用的是隔离的内存规则模块，不会把普通教学规则混入场景执行。

## 5. S1-S5：KieSession 生命周期与并发场景

场景入口都在：

```text
src/main/java/com/drools/benchmark/correctness/
```

均可通过 `main` 直接运行。

### S1：共享 KieSession 被关闭后的并发访问

入口：`com.drools.benchmark.correctness.S1_CorrectnessTest`

流程：

```text
normal worker：共享 Session 中插入 R1 订单 → fireAllRules → dispose
        ↓
7 个 follow worker：并发访问同一个已关闭的 Session
```

预期观察：

```text
先输出：触发规则：R1-VIP
随后 follow worker 可能直接输出：
IllegalStateException: This session was previously disposed
```

该场景用于说明：共享 Stateful `KieSession` 的生命周期必须由唯一拥有者协调；关闭后继续调用 `insert` / `fireAllRules` 会失败。

### S2：共享 KieBase、每线程独立 KieSession

入口：`com.drools.benchmark.correctness.S2_CorrectnessTest`

流程：

```text
8 个线程共享 KieBase
        ↓
每个线程：
    kieBase.newKieSession()
    insert 一条 level=3、amount=2000 的订单
    fireAllRules
    dispose
```

每个线程应独立输出：

```text
fireAllRules 返回 : 1
实际执行规则      : [R1-VIP]
订单 result       : VIP-优先处理
```

### S3：KieSession 池化

入口：`com.drools.benchmark.correctness.S3_CorrectnessTest`

流程：

```text
初始池容量：5
并发线程数：100
        ↓
每个线程：
    从 KieContainerSessionsPool 借用 orderSessionPool
    insert 两条订单
    fireAllRules
    session.dispose()
```

两条线程外定义的业务数据模板：

```text
订单1：level=1，amount=4000 → R3-Normal
订单2：level=3，amount=2000 → R1-VIP
```

对于池化 Session：

```text
session.dispose()
    = 清理当前 Session 状态并归还池
    ≠ 普通独立 Session 的永久销毁
```

> 初始容量 5 不是硬性最大容量。100 个线程并发借用时，池可按负载创建更多 Session，因此日志中可能出现超过 5 个不同的 Session identity。

### S4：两次 fireAllRules 中间更新 KieBase

入口：`com.drools.benchmark.correctness.S4_CorrectnessTest`

初始规则文件：[`order-rules.drl`](src/main/resources/benchmark/order-rules.drl)

更新规则文件：[`order-rules-update.drl`](src/main/resources/benchmark/order-rules-update.drl)

流程：

```text
订单1：level=1，amount=4000
    → 第一次 fireAllRules
    → 初始规则命中 R3-Normal

可选：container.updateToVersion(...)

订单2：level=3，amount=2000
    → 第二次 fireAllRules
```

启动模式：

```text
默认：不更新 KieBase，订单2命中原始 R1-VIP。
开启：-Ds4.updateRules=true，更新为只要求 level=3 且 amount>=3000 的规则，
      订单2不会命中，第二次 fireAllRules 返回 0。
```

该场景用于观察：**在不重新创建当前 KieSession 的情况下，KieBase 规则更新会如何影响后续新插入的业务 Fact。**

### S5：不 dispose 导致历史 Fact 参与 Join 组合

入口：`com.drools.benchmark.correctness.S5_CorrectnessTest`

流程：

```text
3 个后台线程共享同一个 KieSession
        ↓
每个线程插入两条相同业务数据的独立 Order Fact
        ↓
不在 worker 中 dispose
不 retract、不清空 Working Memory
        ↓
fireAllRules
```

S5 使用主规则文件中的：

```text
R1-VIP
S5-历史订单组合匹配
```

后者只匹配 `orderId` 以 `S5-` 开头的订单，避免影响 S1-S4。

典型观察：

| 工作内存 Fact 数 | R1-VIP 匹配数 | S5 Join 匹配数 | fireAllRules 总数 |
|---:|---:|---:|---:|
| 2 | 2 | 2 | 4 |
| 4 | 2 | 10 | 12 |
| 6 | 2 | 18 | 20 |

说明：Fact 数量按每轮新增数据线性增长；但两个 Order 的有序 Join 组合会近似按 `N × (N - 1)` 增长。若规则包含更多 Join 条件，组合规模可能进一步放大。

> S5 虽由多个后台线程模拟业务到达，但使用同一把锁串行执行共享 Session 的 `insert + fireAllRules`。这样可以隔离“历史 Fact 累积 / Join 组合增长”现象，避免把 KieSession 的无锁并发异常混入结果；无锁共享访问的风险由 S1 展示。

## 6. Windows PowerShell：构建与运行

从项目根目录运行：

```powershell
Set-Location 'C:\Users\kangz\Desktop\drools\drools-book-code\droolsBookDemo'
mvn -version
mvn clean compile
```

首次准备运行 main 类时，构建编译期 classpath：

```powershell
mvn dependency:build-classpath `
  '-Dmdep.includeScope=compile' `
  '-Dmdep.outputFile=target/main-classpath.txt'

$classpath = "target\classes;$(Get-Content -Raw target\main-classpath.txt)"
```

运行典型入口：

```powershell
# 手写算法实现
java -cp $classpath com.drools.algorithm.rete.ReteExample
java -cp $classpath com.drools.algorithm.reteOO.ReteExample
java -cp $classpath com.drools.algorithm.pherak.PhreakDemo
java -cp $classpath com.drools.algorithm.ReteNetworkPrinter

# 常规 KIE 示例
java -cp $classpath com.test.drools.helloworld.DroolsHelloWorld
java -cp $classpath com.test.drools.filewhole.RuleFileWholeTest

# S1-S5 场景
java -cp $classpath com.drools.benchmark.correctness.S1_CorrectnessTest
java -cp $classpath com.drools.benchmark.correctness.S2_CorrectnessTest
java -cp $classpath com.drools.benchmark.correctness.S3_CorrectnessTest
java -cp $classpath com.drools.benchmark.correctness.S4_CorrectnessTest
java -cp $classpath com.drools.benchmark.correctness.S5_CorrectnessTest
```

运行 S4 的规则更新分支：

```powershell
java '-Ds4.updateRules=true' -cp $classpath `
  com.drools.benchmark.correctness.S4_CorrectnessTest
```

在 IntelliJ IDEA 中，可直接打开任意含 `main` 方法的类并运行；S4 需要在 Run Configuration 的 VM options 中添加：

```text
-Ds4.updateRules=true
```

## 7. 运行注意事项

1. **S1 的异常是教学现象。** 后续 worker 对已关闭 Session 的未捕获 `IllegalStateException` 是场景输出的一部分；不要只根据进程退出码判断该场景含义。
2. **S3 输出顺序不固定。** 100 个线程会并发借用池化 Session；每个输出块内部通过同步打印保持完整，但 worker 编号顺序不保证。
3. **S5 不是无锁 Session 并发测试。** 它重点展示不 dispose 后历史 Fact 与 Join 组合增长。
4. **部分资源用于错误诊断教学。** `rules/constraint/error.drl` 等资源可能故意包含错误规则；不要假定所有常规教学规则都适合一次性全部装载。
5. **常规资源的 package 与 kmodule 配置应逐例阅读。** 这类示例用于理解包过滤、规则加载和错误诊断；运行前应确认目标 `KieBase` 的 `packages` 与 DRL 的 `package` 一致。
6. **JMH 依赖已配置，但本工程当前重点是可运行的 S1-S5 场景。** 在使用 JMH 产出性能结论前，应确认被测类、输入 Fact 生命周期、Session/Container 释放策略与场景目标一致。

## 8. 维护建议

新增示例时建议保持以下规则：

```text
1. Fact 放在明确的 model 包；
2. DRL 的 package 与目标 KieBase 的 packages 保持一致；
3. Stateful KieSession 使用完毕必须 dispose；
4. 池化 Session 使用完毕调用 dispose 归还池，最终调用 pool.shutdown；
5. 不 dispose 的示例必须明确说明 Fact 累积与内存风险；
6. 并发场景中先明确“测试无锁风险”还是“测试受控生命周期”，不要混合两种目标；
7. 对规则版本更新，记录旧 Session 是否重建、Fact 是否重新插入、以及每次 fireAllRules 的实际规则列表。
```

---

如果你正在按书阅读，建议先运行 `ReteExample`、`ReteOO.ReteExample`、`PhreakDemo`，再依次运行 S1–S5，并结合控制台输出理解规则网络、会话生命周期和历史 Fact 的行为。
