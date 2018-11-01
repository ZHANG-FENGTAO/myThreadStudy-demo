package com.example.demo;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zft
 * @date 2018/10/18.
 */
public class CountDownLatchDemo implements Runnable {

    static CountDownLatch countDownLatch = new CountDownLatch(6);

    static CountDownLatchDemo demo = new CountDownLatchDemo();

    @Override
    public void run() {

        try {
            Thread.sleep(new Random().nextInt(10)*1000);
            System.out.println(Thread.currentThread().getId()+"complete");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) {
        ExecutorService execu = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 6; i++) {
            execu.submit(demo);
        }
        try {
            countDownLatch.await();
            // 只有当所有线程都complete 主线程才执行
            System.out.println("main------>continue");
            execu.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
