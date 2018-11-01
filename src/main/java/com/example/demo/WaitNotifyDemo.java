package com.example.demo;

/**
 * @author zft
 * @date 2018/10/24.
 */
public class WaitNotifyDemo {

    final static Object object = new Object();

    public static class T1 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ": T1 start! ");

                try {
                    System.out.println(System.currentTimeMillis() + ": T1 wait for object! ");
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + ": T1 end! ");
            }
        }
    }

    /**
     * 执行结果
     * 1540372566024: T1 start!
     * 1540372566024: T1 wait for object!
     * 1540372566024: T2 start!
     * 1540372566024: T2 end!
     * 1540372576024: T1 end!
     */
    public static class T2 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ": T2 start! ");
                object.notify();
                System.out.println(System.currentTimeMillis() + ": T2 end! ");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new T1();
        Thread thread2 = new T2();
        thread1.start();
        thread2.start();
    }


}
