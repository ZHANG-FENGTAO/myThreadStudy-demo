package com.example.demo.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author zft
 * @date 2018/11/1.
 */
public class DisruptProducer {
    private final RingBuffer<DisruptorPCData> ringBuffer;

    public DisruptProducer(RingBuffer<DisruptorPCData> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 有DisruptorMain 将数据push进来
     * @param bb byteBuffer
     */
    public void pushData(ByteBuffer bb) {
        // 获取ringBuffer的下一个可用序列号，然后从中取下一个可用的PCData并进行赋值
        long sequence = ringBuffer.next();
        try {
            DisruptorPCData event = ringBuffer.get(sequence);
            event.setIntData(bb.getLong(0));
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}
