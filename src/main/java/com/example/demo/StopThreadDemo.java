package com.example.demo;

/**
 * @author zft
 * @date 2018/10/17.
 */
public class StopThreadDemo {

    public static User user = new User();

    public static class User {

        private int id;

        private String name;

        public User() {
            id=0;
            name="0";
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class WriteThread extends Thread {
//        volatile boolean stop = false;
//
//        public void stopThread(){
//            stop = true;
//        }
        @Override
        public void run() {
            while (true) {
//                if (stop){
//                    System.out.println("stop thread");
//                    break;
//                }
                synchronized (user) {
                    int id = (int) System.currentTimeMillis();
                    user.setId(id);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setName(String.valueOf(id));
                }
                Thread.yield();
            }
        }
    }

    public static class ReadThread extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (user){
                    if (user.getId()!=Integer.parseInt(user.getName())){
                        System.out.println(user.toString());
                    }
                }
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReadThread().start();
        while (true){
            WriteThread writeThread = new WriteThread();
            writeThread.setName("writeThread");
            writeThread.start();
            Thread.sleep(150);
//            writeThread.stopThread();
        }
    }

}
