package com.example.demo;

import com.lmax.disruptor.EventFactory;

/**
 * @author zft
 * @date 2018/11/1.
 */
public class DisruptorPCDataFactory implements EventFactory {
    @Override
    public Object newInstance() {
        return new DisruptorPCData();
    }
}
