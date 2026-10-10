package com.drools.benchmark.correctness;

import com.drools.benchmark.config.BenchmarkConfig;
import com.drools.benchmark.model.Order;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

/**
 * S4：两次 fireAllRules 之间可选更新 KieBase 的简单业务流程示例。
 *
 * <p>第一次加载 order-rules.drl，订单1触发 R3；两次 fire 中间由开关选择是否
 * 更新为只有一条规则的 order-rules-update.drl；订单2再插入并执行第二次 fire。</p>
 */
public final class S4_CorrectnessTest {

    /** true 表示在两次 fireAllRules 中间更新规则；false 表示保持初始规则。 */
    private static final String UPDATE_PROPERTY = "s4.updateRules";

    /** 禁止创建示例工具类实例。 */
    private S4_CorrectnessTest() {
    }

    /**
     * JVM 主入口。
     *
     * <p>不更新规则：直接运行。更新规则：增加 {@code -Ds4.updateRules=true} 参数。</p>
     *
     * @param args 未使用的启动参数
     */
    public static void main(String[] args) {
        runScenario();
    }

    /** 执行“订单1 → 第一次 fire → 可选更新规则 → 订单2 → 第二次 fire”的业务流程。 */
    public static void runScenario() {
        boolean updateRules = Boolean.parseBoolean(System.getProperty(UPDATE_PROPERTY, "true"));
        KieContainer container = BenchmarkConfig.createTestContainer();
        KieSession session = container.newKieSession("orderSession");
        List<String> firedRules = new ArrayList<String>();
        int sessionIdentity = System.identityHashCode(session);

        session.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                // 每次 fireAllRules 后由 main 线程读取实际执行过的规则名。
                firedRules.add(event.getMatch().getRule().getName());
            }
        });
        try {
            // 订单1：level=1、amount=4000，初始 order-rules.drl 中应命中 R3-Normal。
            Order order1 = new Order("S4-ORDER-1", 4000.0D, 1);
            session.insert(order1);
            int firstFired = session.fireAllRules();
            List<String> firstRules = copyAndClear(firedRules);
            printFirstFire(order1, firstFired, firstRules);

            String updateLog;
            if (updateRules) {
                // 更新 KieBase，但不重新创建当前 KieSession。
                updateLog = container.updateToVersion(BenchmarkConfig.buildUpdateRuleRelease()).getMessages().toString();
            } else {
                updateLog = "未更新规则，继续使用 order-rules.drl";
            }
            System.out.println("[两次 fireAllRules 中间]");
            System.out.println("  是否更新规则          : " + updateRules);
            System.out.println("  更新操作消息          : " + updateLog);
            System.out.println("  KieSession identity   : " + sessionIdentity + "（未重新创建）");

            // 订单2：level=3、amount=2000。原规则中命中 R1；更新规则中要求 amount>=3000，不命中。
            Order order2 = new Order("S4-ORDER-2", 2000.0D, 3);
            session.insert(order2);
            int secondFired = session.fireAllRules();
            List<String> secondRules = copyAndClear(firedRules);
            printSecondFire(order2, secondFired, secondRules);
        } finally {
            session.dispose();
            container.dispose();
        }
    }

    /** 输出订单1的第一次 insert/fire 结果。 */
    private static void printFirstFire(Order order, int fired, List<String> rules) {
        System.out.println();
        System.out.println("========== S4：KieBase 更新是否影响当前 KieSession ==========");
        System.out.println("[第一次 insert + fireAllRules]");
        System.out.println("  订单                 : " + order.getOrderId()
                + "，amount=" + order.getAmount() + "，level=" + order.getCustomerLevel());
        System.out.println("  fireAllRules 返回     : " + fired);
        System.out.println("  实际执行规则          : " + rules);
        System.out.println("  订单 result           : " + order.getResult());
    }

    /** 输出订单2的第二次 insert/fire 结果。 */
    private static void printSecondFire(Order order, int fired, List<String> rules) {
        System.out.println();
        System.out.println("[第二次 insert + fireAllRules]");
        System.out.println("  订单                 : " + order.getOrderId()
                + "，amount=" + order.getAmount() + "，level=" + order.getCustomerLevel());
        System.out.println("  fireAllRules 返回     : " + fired);
        System.out.println("  实际执行规则          : " + rules);
        System.out.println("  订单 result           : " + order.getResult());
        System.out.println("================================================================");
    }

    /** 复制当前 fire 周期内的规则名，并清空列表供下一次 fire 使用。 */
    private static List<String> copyAndClear(List<String> firedRules) {
        List<String> copied = new ArrayList<String>(firedRules);
        firedRules.clear();
        return copied;
    }
}
