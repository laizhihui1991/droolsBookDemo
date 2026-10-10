package com.drools.benchmark.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;

/** Drools 并发示例的常量、系统属性及内存 KIE 模块构建工具。 */
public final class BenchmarkConfig {
    /** 小批量事实数。 */ public static final int FACT_COUNT_SMALL = 10;
    /** 中批量事实数。 */ public static final int FACT_COUNT_MEDIUM = 100;
    /** 大批量事实数。 */ public static final int FACT_COUNT_LARGE = 1000;
    /** 默认并发线程数。 */ public static final int THREAD_COUNT_DEFAULT = 8;
    /** 默认约束 JIT 阈值。 */ public static final int JIT_THRESHOLD_DEFAULT = 20;
    /** 禁用约束 JIT 的阈值。 */ public static final int JIT_THRESHOLD_DISABLED = -1;
    /** Drools KieBase 线程安全系统属性。 */ public static final String THREAD_SAFE_PROPERTY = "drools.threadSafe";
    /** Drools 定时器工厂系统属性。 */ public static final String TIMER_FACTORY_PROPERTY = "drools.timerJobFactory";
    /** 初始规则模块版本。 */ private static final String INITIAL_VERSION = "1.0.0";
    /** S4 更新规则模块版本。 */ private static final String UPDATE_VERSION = "1.0.1";
    /** 常规场景模块名。 */ private static final String ARTIFACT_ID = "drools-benchmark-runtime";
    /** Drools 约束 JIT 阈值系统属性。 */ private static final String JIT_PROPERTY = "drools.jittingThreshold";

    /** 禁止构造工具类。 */ private BenchmarkConfig() { }
    /** 在首次 KieServices 初始化前配置线程安全选项。 */ public static void applyThreadSafe(boolean enabled) { System.setProperty(THREAD_SAFE_PROPERTY, Boolean.toString(enabled)); }
    /** 在首次规则编译前配置约束 JIT 阈值。 */ public static void applyJitThreshold(int threshold) { System.setProperty(JIT_PROPERTY, Integer.toString(threshold)); }

    /** 使用初始 order-rules.drl 构建常规场景容器。 */
    public static KieContainer createTestContainer() {
        KieServices services = KieServices.Factory.get();
        return services.newKieContainer(buildRelease(ARTIFACT_ID, INITIAL_VERSION, "benchmark/order-rules.drl"));
    }

    /** 使用只有一条规则的 order-rules-update.drl 构建 S4 的更新版本。 */
    public static ReleaseId buildUpdateRuleRelease() {
        return buildRelease(ARTIFACT_ID, UPDATE_VERSION, "benchmark/order-rules-update.drl");
    }

    /** 将指定 DRL 资源映射到内存模块的标准规则路径并编译。 */
    private static ReleaseId buildRelease(String artifactId, String version, String ruleResourcePath) {
        KieServices services = KieServices.Factory.get();
        ReleaseId releaseId = services.newReleaseId("org.example", artifactId, version);
        KieFileSystem fileSystem = services.newKieFileSystem();
        Resource module = services.getResources().newClassPathResource("kmodule.xml", "UTF-8");
        Resource rules = services.getResources().newClassPathResource(ruleResourcePath, "UTF-8");
        fileSystem.generateAndWritePomXML(releaseId);
        fileSystem.write("src/main/resources/META-INF/kmodule.xml", module);
        // 所有场景均写入相同模块内路径，KieBase 按 benchmark package 加载它。
        fileSystem.write("src/main/resources/benchmark/order-rules.drl", rules);
        KieBuilder builder = services.newKieBuilder(fileSystem).buildAll();
        if (builder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("benchmark 规则构建失败：" + builder.getResults().getMessages());
        }
        services.getRepository().addKieModule(builder.getKieModule());
        return builder.getKieModule().getReleaseId();
    }
}
