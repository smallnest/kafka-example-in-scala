package com.colobu.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * 处理KafkaStream， pull messages并处理.
 * 
 *
 */
public class ConsumerTest implements Runnable {
	private ConsumerConnector consumer;
	private KafkaStream<byte[], byte[]> stream;
	private int threadNumber;
	private long delay;
	
	public ConsumerTest(ConsumerConnector consumer, KafkaStream<byte[], byte[]> stream, int threadNumber, long delay) {
		this.consumer = consumer;
		this.threadNumber = threadNumber;
		this.stream = stream;
		this.delay = delay;
	}

	public void run() {
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		
		while (it.hasNext()) {
			//格式： 处理时间,线程编号:消息
			String msg = new String(it.next().message());
			long t = System.currentTimeMillis() - Long.parseLong(msg.substring(0, msg.indexOf(",")));
			
			if (t < delay) {
				try {
					Thread.currentThread().sleep(delay -t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(System.currentTimeMillis() + ",Thread " + threadNumber + ": " + msg);
		}
		
		System.out.println("Shutting down Thread: " + threadNumber);
		
	}
	
	public void run0() {
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		
		while (it.hasNext()) {
			//格式： 处理时间,线程编号:消息
			
			System.out.println(System.currentTimeMillis() + ",Thread " + threadNumber + ": " + new String(it.next().message()));
		}
		
		System.out.println("Shutting down Thread: " + threadNumber);
		
	}
	
	
	public void run1() {
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		int count = 0;
		while (it.hasNext()) {
			//格式： 处理时间,线程编号:消息
			System.out.println(System.currentTimeMillis() + ",Thread " + threadNumber + ": " + new String(it.next().message()));
			count++;
			
			if (count == 100) {
				consumer.commitOffsets();
				count = 0;
			}
		}
		
		System.out.println("Shutting down Thread: " + threadNumber);
		
	}
	
}