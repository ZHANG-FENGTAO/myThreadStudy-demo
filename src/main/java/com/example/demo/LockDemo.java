package com.example.demo;

import com.taobao.tair.ResultCode;
import com.taobao.tair.TairManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zft
 * @date 2018/11/2.
 */
public class LockDemo {

    private TairManager tairManager;

    private int tairNamespace;

    private Logger logger = LoggerFactory.getLogger(com.example.demo.LockDemo.class);

    public static int LOCK_INIT_VERSION = 10000;

    public TairManager getTairManager() {
        return tairManager;
    }

    public void setTairManager(TairManager tairManager) {
        this.tairManager = tairManager;
    }

    public int getTairNamespace() {
        return tairNamespace;
    }

    public void setTairNamespace(int tairNamespace) {
        this.tairNamespace = tairNamespace;
    }

    /**
     * 分布式锁
     * 使用方式：
     * Lock lock = Lock.of(xx,xx,xx);
     * try{
     * if(tryLock(lock)){
     * do something
     * }
     * }catch(TairException e){
     * handler tairException
     * }
     * finaly{
     * releaseLock(lock);
     * }
     *
     */
    public boolean tryLock(Lock lock) throws TairException {

        long waitTime = 0;
        String key = generateLockKey(lock.getLockKey());

        while (true) {

            ResultCode result = tairManager.put(tairNamespace, key, "true", LOCK_INIT_VERSION, (int)lock.getLockTime());

            if (result == null) {
                throw new TairException(null, "tair result is null, key:[" + key + "]");
            }

            if (!result.isSuccess() && result != ResultCode.VERERROR) {
                throw new TairException(result, "tair result is fail, key:[" + key + "]");
            }

            if (result.isSuccess()) {
                lock.isLocked = true;
                lock.setVersion(1);
                return true;
            }

            if (waitTime >= lock.getTimeout()) {
                break;
            }

            /**
             * sleep 阻塞
             */
            try {
                Thread.sleep(100);
                waitTime += 100;
            } catch (Exception e) {
                logger.error("locker sleep interrupt", e);
                return false;
            }
        }

        return false;

    }

    /**
     * 延长lock 时间;必须已获取锁的前提下可以成功
     *
     * @param lock
     * @return
     */
    public boolean extendLockTime(Lock lock) {
        if (!lock.isLocked()) {
            return true;
        }
        String key = generateLockKey(lock.getLockKey());
        ResultCode result = tairManager.put(tairNamespace, key, "true", lock.getVersion(), (int)lock.getLockTime());
        if (!result.isSuccess()) {
            return false;
        }

        lock.setVersion(lock.getVersion() + 1);
        return true;
    }

    /**
     * 释放锁
     *
     * @param lock 锁
     * @return 是否释放成功
     */
    public boolean releaseLock(Lock lock) {
        if (!lock.isLocked()) {
            return true;
        }
        /**
         * 日常环境测试发现 invalid 无效，而delete有效; 理论上 ldb 的delete 和 invalid 是等同的
         */
        ResultCode resultCode = tairManager.delete(tairNamespace, generateLockKey(lock.getLockKey()));

        return resultCode.isSuccess();
    }

    private String generateLockKey(String lockKey) {
        return "lock_" + lockKey;
    }

    public static class Lock {

        /**
         * 加锁的业务key
         */
        private String lockKey;

        /**
         * 等待的超时时间(单位毫秒) ， 如果 timeout <= 0 ,则不阻塞,立即返回
         */
        private long timeout;

        /**
         * 加锁的最大时间(单位秒)，一般设置为最大业务执行时间
         */
        private long lockTime;

        /**
         * 是否锁定
         */
        private boolean isLocked;

        /**
         * 锁版本
         */
        private int version = 0;

        /**
         * @param lockKey  锁的key
         * @param timeout  阻塞等待的时间,单位毫秒
         * @param lockTime 锁的有效时长,单位秒
         * @return
         */
        public static Lock of(String lockKey, long timeout, long lockTime) {
            Lock lock = new Lock();
            lock.lockKey = lockKey;
            lock.timeout = timeout;
            lock.lockTime = lockTime;
            lock.isLocked = false;
            return lock;
        }

        public String getLockKey() {
            return lockKey;
        }

        public void setLockKey(String lockKey) {
            this.lockKey = lockKey;
        }

        public boolean isLocked() {
            return isLocked;
        }

        public long getLockTime() {
            return lockTime;
        }

        public void setLockTime(long lockTime) {
            this.lockTime = lockTime;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }

    class TairException extends RuntimeException {

        private static final long serialVersionUID = 8654988155165141489L;

        private ResultCode resultCode;

        private String errorMsg;


        public TairException(ResultCode resultCode, String errorMsg) {
            this.resultCode = resultCode;
            this.errorMsg = errorMsg;
        }

        @Override
        public String getMessage() {
            if (resultCode == null) {
                return "errorMsg:" + errorMsg;
            }
            return "tair:[code:" + resultCode.getCode() + ";msg:" + resultCode.getMessage() + "]; errorMsg:" + errorMsg;
        }

        public ResultCode getResultCode() {
            return this.resultCode;
        }

        public String getErrorMsg() {
            return this.errorMsg;
        }
    }

}
