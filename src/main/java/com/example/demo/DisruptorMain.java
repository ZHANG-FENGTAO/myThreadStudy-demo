package com.example.demo;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.nio.ByteBuffer;

/**
 * @author zft
 * @date 2018/11/1.
 */
public class DisruptorMain {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws InterruptedException {
        DefaultThreadFactory threadFactory = new DefaultThreadFactory("disruptor-thread");
        DisruptorPCDataFactory factory = new DisruptorPCDataFactory();

        int buffSize = 1024;
        Disruptor<DisruptorPCData> disruptor = new Disruptor<DisruptorPCData>(factory, buffSize,
                threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(new DisruptConsumer(), new DisruptConsumer(), new DisruptConsumer());
        disruptor.start();
        RingBuffer<DisruptorPCData> ringBuffer = disruptor.getRingBuffer();
        DisruptProducer producer = new DisruptProducer(ringBuffer);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        long i = 0;
        do {
            buffer.putLong(0, i);
            producer.pushData(buffer);
            Thread.sleep(100);
            System.out.println("add data " + i);
            i++;
        } while (true);
    }
}
