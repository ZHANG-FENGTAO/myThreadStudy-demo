package com.example.demo;

import com.lmax.disruptor.WorkHandler;

/**
 * @author zft
 * @date 2018/11/1.
 */
public class DisruptConsumer implements WorkHandler<DisruptorPCData> {
    /**
     * 消费者的作用是读取数据进行处理，这里数据的读取已经有Disruptor封装,onEvent为框架的回调方法
     *
     * @param pcEvent event
     * @throws Exception exception
     */
    @Override
    public void onEvent(DisruptorPCData pcEvent) throws Exception {
        System.out.println(Thread.currentThread().getId() + " :Consumer:--" + pcEvent.getIntData());
    }
}
