package com.drools.benchmark.correctness;

import com.drools.benchmark.config.BenchmarkConfig;
import com.drools.benchmark.model.Order;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * S1：多个线程共享同一个 KieSession，先正常触发规则并关闭，再并发访问已关闭的 Session。
 *
 * <p>第一个线程保证完成一次正常的 R1 触发并调用 dispose。随后七个线程同时使用同一个
 * 已关闭 Session，模拟业务线程在共享 Session 生命周期结束后仍继续调用的风险。工作线程
 * 不捕获异常，JVM 会直接打印异常堆栈。</p>
 */
public final class S1_CorrectnessTest {

    /** 总工作线程数：一个正常线程加七个后续访问线程。 */
    private static final int THREAD_COUNT = BenchmarkConfig.THREAD_COUNT_DEFAULT;
    /** 后续访问已关闭 Session 的线程数。 */
    private static final int FOLLOW_UP_THREAD_COUNT = THREAD_COUNT - 1;
    /** 主线程等待工作线程结束的最长时间。 */
    private static final long TIMEOUT_SECONDS = 10L;

    /** 禁止创建示例工具类实例。 */
    private S1_CorrectnessTest() {
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
     * 先正常触发 R1 并关闭共享 Session，再让七个线程并发访问该已关闭 Session。
     *
     * @throws Exception 主线程等待被中断或工作线程超时时抛出
     */
    public static void runScenario() throws Exception {
        final KieContainer container = BenchmarkConfig.createTestContainer();
        final KieSession sharedSession = container.newKieSession("orderSession");
        final CountDownLatch followUpReady = new CountDownLatch(FOLLOW_UP_THREAD_COUNT);
        final CountDownLatch sessionDisposed = new CountDownLatch(1);
        final Thread[] followUpWorkers = new Thread[FOLLOW_UP_THREAD_COUNT];

        // 只打印正常完成触发的规则名称。
        sharedSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                System.out.println("触发规则：" + event.getMatch().getRule().getName());
            }
        });

        int index;
        for (index = 0; index < FOLLOW_UP_THREAD_COUNT; index++) {
            final int workerIndex = index;
            followUpWorkers[index] = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 确保七个 worker 都已准备好，再等待正常线程关闭 Session。
                    followUpReady.countDown();
                    awaitOrThrow(sessionDisposed, "等待共享 KieSession 被 dispose 超时");
                    try {
                        // 七个线程同时操作同一个已关闭的 Session；异常不捕获。
                        Order order=new Order("S1-FOLLOW-" + workerIndex, workerIndex + 1,3);
                        sharedSession.insert(order);
                        sharedSession.fireAllRules();
                    } finally {
                        // 每个后续线程也按场景要求调用 dispose。
                        sharedSession.dispose();
                    }
                }
            }, "s1-follow-worker-" + workerIndex);
            followUpWorkers[index].setDaemon(true);
            followUpWorkers[index].start();
        }

        if (!followUpReady.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            throw new IllegalStateException("S1 后续工作线程未全部就绪");
        }

        Thread normalWorker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 订单满足初始规则 R1-VIP，保证先有一条正常规则触发日志。
                    sharedSession.insert(new Order("S1-NORMAL", 1000.0D, 3));
                    sharedSession.fireAllRules();
                } finally {
                    // 正常线程结束时关闭共享 Session，并释放后续七个并发线程。
                    try {
                        sharedSession.dispose();
                    } finally {
                        sessionDisposed.countDown();
                    }
                }
            }
        }, "s1-normal-worker");
        normalWorker.setDaemon(true);
        normalWorker.start();

        normalWorker.join(TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
        if (normalWorker.isAlive()) {
            throw new IllegalStateException("S1 正常触发线程超时");
        }
        for (index = 0; index < FOLLOW_UP_THREAD_COUNT; index++) {
            followUpWorkers[index].join(TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
            if (followUpWorkers[index].isAlive()) {
                throw new IllegalStateException("S1 后续工作线程超时：" + followUpWorkers[index].getName());
            }
        }
        container.dispose();
    }

    /** 等待 Session 已关闭信号；中断或超时时直接抛出异常。 */
    private static void awaitOrThrow(CountDownLatch latch, String message) {
        try {
            if (!latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException(message);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(message, exception);
        }
    }
}
