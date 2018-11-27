package com.example.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author zft
 * @date 2018/11/27.
 */
public class CompletableFutureDemo implements Runnable {

    private CompletableFuture<Integer> future = null;

    private CompletableFutureDemo(CompletableFuture<Integer> future) {
        this.future = future;
    }

    @Override
    public void run() {
        int myFuture = 0;
        try {
            myFuture = future.get() * future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(myFuture);
    }

    public static void main(String[] args) throws InterruptedException {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(new CompletableFutureDemo((future))).start();
        Thread.sleep(1000);
        future.complete(60);
    }
}


