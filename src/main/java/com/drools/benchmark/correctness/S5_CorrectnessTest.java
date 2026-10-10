package com.drools.benchmark.correctness;

import com.drools.benchmark.config.BenchmarkConfig;
import com.drools.benchmark.model.Order;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * S5：共享 KieSession 不在 worker 中 dispose，观察历史 Fact 与新 Fact 的 Join 匹配增长。
 *
 * <p>三个线程同时启动，但进入共享 Session 的 insert/fireAllRules 临界区时使用同一把锁。
 * 锁的目的不是测试 Session 并发安全，而是让每轮的 Fact 累积和规则数量可重复观察。</p>
 */
public final class S5_CorrectnessTest {

    /** 后台工作线程数。 */
    private static final int THREAD_COUNT = 3;
    /** 每个线程向共享 Session 新增的 Fact 数。 */
    private static final int FACTS_PER_THREAD = 2;
    /** 主线程等待 worker 完成的最长时间。 */
    private static final long TIMEOUT_SECONDS = 10L;

    /** 订单数据1：三级客户、金额2000，命中单订单规则并参与 Join。 */
    private static final OrderData ORDER_DATA_1 = new OrderData("S5-ORDER-1", 2000.0D, 3);
    /** 订单数据2：三级客户、金额3000，命中单订单规则并参与 Join。 */
    private static final OrderData ORDER_DATA_2 = new OrderData("S5-ORDER-2", 3000.0D, 3);

    /** 禁止创建示例工具类实例。 */
    private S5_CorrectnessTest() {
    }

    /**
     * JVM 主入口。
     *
     * @param args 未使用的启动参数
     * @throws Exception 主线程等待被中断或线程超时时抛出
     */
    public static void main(String[] args) throws Exception {
        runScenario();
    }

    /**
     * 创建一个持久共享 Session；多个线程持续插入新 Fact 并 fireAllRules，直到全部完成后才 dispose。
     *
     * @throws Exception 主线程等待被中断或线程超时时抛出
     */
    public static void runScenario() throws Exception {
        final KieContainer container = BenchmarkConfig.createTestContainer();
        final KieSession session = container.newKieSession("orderSession");
        final Object sessionLock = new Object();
        final CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(THREAD_COUNT);
        final Thread[] workers = new Thread[THREAD_COUNT];
        final Map<String, Integer> firedRuleCounts = new LinkedHashMap<String, Integer>();
        int index;

        // 因为每次 fireAllRules 都在 sessionLock 内运行，此 Map 可安全用于记录当前轮规则数量。
        session.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                String ruleName = event.getMatch().getRule().getName();
                Integer count = firedRuleCounts.get(ruleName);
                firedRuleCounts.put(ruleName, count == null ? 1 : count + 1);
            }
        });

        try {
            for (index = 0; index < THREAD_COUNT; index++) {
                final int workerIndex = index;
                workers[index] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ready.countDown();
                        try {
                            awaitStart(start);
                            synchronized (sessionLock) {
                                // 每个线程按模板创建独立 Fact，避免共享被 modify 的 Order 实例。
                                Order order1 = ORDER_DATA_1.createOrder(workerIndex);
                                Order order2 = ORDER_DATA_2.createOrder(workerIndex);
                                session.insert(order1);
                                session.insert(order2);
                                firedRuleCounts.clear();
                                int fired = session.fireAllRules();
                                printRound(workerIndex, order1, order2, fired, session.getObjects().size(), firedRuleCounts);
                            }
                        } finally {
                            // 本场景故意不在 worker 中 dispose，历史 Fact 保留在 Session 工作内存。
                            done.countDown();
                        }
                    }
                }, "s5-worker-" + workerIndex);
                workers[index].setDaemon(true);
                workers[index].start();
            }
            if (!ready.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException("S5 工作线程未全部就绪");
            }
            // main 线程一次性放行后台 worker；它们随后轮流使用同一个不释放的 Session。
            start.countDown();
            if (!done.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException("S5 工作线程超时");
            }
            for (index = 0; index < THREAD_COUNT; index++) {
                workers[index].join(TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
                if (workers[index].isAlive()) {
                    throw new IllegalStateException("S5 工作线程未结束：" + workers[index].getName());
                }
            }
        } finally {
            // 所有 worker 完成后才最终释放持久 Session。
            session.dispose();
            container.dispose();
        }
    }

    /**
     * 同步输出本轮新增 Fact、共享工作内存数量和两条规则的实际匹配数。
     */
    private static synchronized void printRound(int workerIndex, Order order1, Order order2,
                                                int fired, int workingMemorySize,
                                                Map<String, Integer> firedRuleCounts) {
        System.out.println("[s5-worker-" + workerIndex + "]");
        System.out.println("  本轮新增 Fact         : " + FACTS_PER_THREAD);
        System.out.println("  当前 Working Memory   : " + workingMemorySize);
        System.out.println("  fireAllRules 返回     : " + fired);
        System.out.println("  规则匹配数量          : " + firedRuleCounts);
        System.out.println("  订单1 result          : " + order1.getResult());
        System.out.println("  订单2 result          : " + order2.getResult());
    }

    /** 等待 main 线程的同时启动信号；中断时直接抛出未捕获异常。 */
    private static void awaitStart(CountDownLatch start) {
        try {
            start.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("等待 S5 启动信号被中断", exception);
        }
    }

    /** 在线程外定义的不可变订单数据模板。 */
    private static final class OrderData {
        /** 订单标识前缀。 */ private final String orderIdPrefix;
        /** 订单金额。 */ private final double amount;
        /** 客户等级。 */ private final int customerLevel;

        /** 创建订单数据模板。 */
        private OrderData(String orderIdPrefix, double amount, int customerLevel) {
            this.orderIdPrefix = orderIdPrefix;
            this.amount = amount;
            this.customerLevel = customerLevel;
        }

        /** 基于模板创建属于指定线程的独立 Fact。 */
        private Order createOrder(int workerIndex) {
            return new Order(orderIdPrefix + "-W" + workerIndex, amount, customerLevel);
        }
    }
}
