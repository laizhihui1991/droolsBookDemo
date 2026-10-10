package com.drools.benchmark.correctness;

import com.drools.benchmark.config.BenchmarkConfig;
import com.drools.benchmark.model.Order;
import org.kie.api.KieBase;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * S2：八个线程共享同一个 KieBase，但每个线程创建并释放自己的 KieSession。
 *
 * <p>本场景不使用 support 包做数据生成或结果对比。每个线程直接创建一条满足 R1 的订单，
 * 通过 {@link KieBase#newKieSession()} 创建独立工作内存，执行规则后在 finally 中 dispose。</p>
 */
public final class S2_CorrectnessTest {

    /** 并发工作线程数。 */
    private static final int THREAD_COUNT = BenchmarkConfig.THREAD_COUNT_DEFAULT;
    /** 主线程等待 worker 完成的最长时间。 */
    private static final long TIMEOUT_SECONDS = 10L;

    /** 禁止创建示例工具类实例。 */
    private S2_CorrectnessTest() {
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
     * 同时启动八个线程；每个线程通过同一 KieBase 创建独立 KieSession，执行规则并 dispose。
     *
     * @throws Exception 主线程等待被中断或线程超时时抛出
     */
    public static void runScenario() throws Exception {
        final KieContainer container = BenchmarkConfig.createTestContainer();
        final KieBase kieBase = container.getKieBase("benchmarkKieBase");
        final CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
        final CountDownLatch start = new CountDownLatch(1);
        final Thread[] workers = new Thread[THREAD_COUNT];
        int index;

        try {
            for (index = 0; index < THREAD_COUNT; index++) {
                final int workerIndex = index;
                workers[index] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ready.countDown();
                        awaitStart(start);
                        // 每个线程从共享 KieBase 创建自己的独立 KieSession。
                        KieSession session = kieBase.newKieSession();
                        final List<String> firedRules = new ArrayList<String>();
                        try {
                            session.addEventListener(new DefaultAgendaEventListener() {
                                @Override
                                public void afterMatchFired(AfterMatchFiredEvent event) {
                                    // 此集合只被当前 worker 使用，无需额外同步。
                                    firedRules.add(event.getMatch().getRule().getName());
                                }
                            });
                            // 直接构造 Fact：三级客户、金额 2000，满足 R1-VIP。
                            Order order = new Order("S2-ORDER-" + workerIndex, 2000.0D, 3);
                            session.insert(order);
                            int fired = session.fireAllRules();
                            printFire(workerIndex, order, fired, firedRules);
                        } finally {
                            // 独立 Session 用完后由创建它的线程释放。
                            session.dispose();
                        }
                    }
                }, "s2-worker-" + workerIndex);
                workers[index].setDaemon(true);
                workers[index].start();
            }

            if (!ready.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException("S2 工作线程未全部就绪");
            }
            // main 线程一次性放行全部 worker。
            start.countDown();

            for (index = 0; index < THREAD_COUNT; index++) {
                workers[index].join(TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
                if (workers[index].isAlive()) {
                    throw new IllegalStateException("S2 工作线程超时：" + workers[index].getName());
                }
            }
        } finally {
            container.dispose();
        }
    }

    /**
     * 同步输出一个独立 Session 的规则执行结果，避免多线程日志内容交错。
     */
    private static synchronized void printFire(int workerIndex, Order order,
                                               int fired, List<String> firedRules) {
        System.out.println("[s2-worker-" + workerIndex + "]");
        System.out.println("  订单                 : " + order.getOrderId()
                + "，amount=" + order.getAmount() + "，level=" + order.getCustomerLevel());
        System.out.println("  fireAllRules 返回     : " + fired);
        System.out.println("  实际执行规则          : " + firedRules);
        System.out.println("  订单 result           : " + order.getResult());
    }

    /** 等待 main 线程的同时启动信号；中断时直接抛出未捕获异常。 */
    private static void awaitStart(CountDownLatch start) {
        try {
            start.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("等待 S2 启动信号被中断", exception);
        }
    }
}
