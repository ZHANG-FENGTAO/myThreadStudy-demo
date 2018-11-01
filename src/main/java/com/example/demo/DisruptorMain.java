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
 * result:
 * 12 :Consumer:--0
 * add data 0
 * 13 :Consumer:--1
 * add data 1
 * 14 :Consumer:--2
 * add data 2
 * 12 :Consumer:--3
 * add data 3
 * 13 :Consumer:--4
 * add data 4
 * 14 :Consumer:--5
 * add data 5
 * 12 :Consumer:--6
 */
public class DisruptorMain {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws InterruptedException {
        DefaultThreadFactory threadFactory = new DefaultThreadFactory("disruptor-thread");
        DisruptorPCDataFactory factory = new DisruptorPCDataFactory();
        // 这里的size必须是2的整数次方
        int buffSize = 1024;
        // 创建disruptor对象
        Disruptor<DisruptorPCData> disruptor = new Disruptor<DisruptorPCData>(factory, buffSize,
                threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());
        // 设置三个消费者实例，系统将为每一个消费者实例进行映射到一个线程中，这里将会有三个消费者线程
        disruptor.handleEventsWithWorkerPool(new DisruptConsumer(), new DisruptConsumer(), new DisruptConsumer());
        // 启动disruptor
        disruptor.start();
        // 创建ringBuffer 并构造生产者进行生产
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
