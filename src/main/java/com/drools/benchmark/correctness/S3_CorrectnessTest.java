package com.drools.benchmark.correctness;

import com.drools.benchmark.config.BenchmarkConfig;
import com.drools.benchmark.model.Order;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieContainerSessionsPool;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * S3：100 个线程从初始容量为 5 的 KieSession 池借用 Session、执行规则并归还。
 *
 * <p>池容量 5 表示初始池大小；100 个并发借用请求下，Drools 池可按负载创建更多 Session。
 * 每个线程借到的 Session 必须在 finally 中调用 dispose，表示重置并归还池。</p>
 */
public final class S3_CorrectnessTest {

    /** KieSession 池的初始容量。 */
    private static final int POOL_SIZE = 5;
    /** 并发工作线程数。 */
    private static final int THREAD_COUNT = 100;
    /** 主线程等待 worker 完成的最长时间。 */
    private static final long TIMEOUT_SECONDS = 20L;

    /** 订单数据1：一级客户、金额4000，初始规则应命中 R3-Normal。 */
    private static final OrderData ORDER_DATA_1 = new OrderData("S3-ORDER-1", 4000.0D, 1);
    /** 订单数据2：三级客户、金额2000，初始规则应命中 R1-VIP。 */
    private static final OrderData ORDER_DATA_2 = new OrderData("S3-ORDER-2", 2000.0D, 3);

    /** 禁止创建示例工具类实例。 */
    private S3_CorrectnessTest() {
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
     * 同时启动 100 个线程；每个线程借用池化 Session，处理两条订单后 dispose 归还池。
     *
     * @throws Exception 主线程等待被中断或线程超时时抛出
     */
    public static void runScenario() throws Exception {
        final KieContainer container = BenchmarkConfig.createTestContainer();
        final KieContainerSessionsPool pool = container.newKieSessionsPool(POOL_SIZE);
        final CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(THREAD_COUNT);
        final Thread[] workers = new Thread[THREAD_COUNT];
        int index;

        try {
            for (index = 0; index < THREAD_COUNT; index++) {
                final int workerIndex = index;
                workers[index] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ready.countDown();
                        try {
                            awaitStart(start);
                            // 从共享池借出一个当前线程独占的 KieSession。
                            KieSession session = pool.newKieSession("orderSessionPool");
                            final List<String> firedRules = new ArrayList<String>();
                            try {
                                session.addEventListener(new DefaultAgendaEventListener() {
                                    @Override
                                    public void afterMatchFired(AfterMatchFiredEvent event) {
                                        // 当前列表仅属于本线程借用的 Session。
                                        firedRules.add(event.getMatch().getRule().getName());
                                    }
                                });
                                // 两条模板数据在线程外定义；这里创建独立 Fact，避免共享可变 result。
                                Order order1 = ORDER_DATA_1.createOrder(workerIndex);
                                Order order2 = ORDER_DATA_2.createOrder(workerIndex);
                                session.insert(order1);
                                session.insert(order2);
                                int fired = session.fireAllRules();
                                printFire(workerIndex, System.identityHashCode(session),
                                        order1, order2, fired, firedRules);
                            } finally {
                                // 池化 Session 的 dispose 表示清理并归还池。
                                session.dispose();
                            }
                        } finally {
                            done.countDown();
                        }
                    }
                }, "s3-worker-" + workerIndex);
                workers[index].setDaemon(true);
                workers[index].start();
            }

            if (!ready.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException("S3 工作线程未全部就绪");
            }
            // main 线程一次性放行 100 个池化 Session 借用请求。
            start.countDown();
            if (!done.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException("S3 工作线程超时");
            }
            for (index = 0; index < THREAD_COUNT; index++) {
                if (workers[index].isAlive()) {
                    throw new IllegalStateException("S3 工作线程未结束：" + workers[index].getName());
                }
            }
        } finally {
            pool.shutdown();
            container.dispose();
        }
    }

    /**
     * 同步输出一个池化 Session 的规则执行结果，避免 100 个线程的输出交错。
     */
    private static synchronized void printFire(int workerIndex, int sessionIdentity,
                                               Order order1, Order order2,
                                               int fired, List<String> firedRules) {
        System.out.println("[s3-worker-" + workerIndex + "]");
        System.out.println("  Session identity     : " + sessionIdentity);
        System.out.println("  订单1                : " + order1.getOrderId()
                + "，amount=" + order1.getAmount() + "，level=" + order1.getCustomerLevel()
                + "，result=" + order1.getResult());
        System.out.println("  订单2                : " + order2.getOrderId()
                + "，amount=" + order2.getAmount() + "，level=" + order2.getCustomerLevel()
                + "，result=" + order2.getResult());
        System.out.println("  fireAllRules 返回     : " + fired);
        System.out.println("  实际执行规则          : " + firedRules);
    }

    /** 等待 main 线程的同时启动信号；中断时直接抛出未捕获异常。 */
    private static void awaitStart(CountDownLatch start) {
        try {
            start.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("等待 S3 启动信号被中断", exception);
        }
    }

    /**
     * 在线程外定义的不可变订单数据模板。
     */
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

        /** 根据模板创建属于指定线程的独立 Fact。 */
        private Order createOrder(int workerIndex) {
            return new Order(orderIdPrefix + "-W" + workerIndex, amount, customerLevel);
        }
    }
}
