package com.colobu.kafka;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消费者例子.
 *
 */
public class ConsumerExample {
	private final ConsumerConnector consumer;
	private final String topic;
	private ExecutorService executor;
	private long delay;

	/**
	 * 
	 * @param zookeeper zookeeper连接字符串
	 * @param groupId 组名
	 * @param topic topic
	 */
	public ConsumerExample(String zookeeper, String groupId, String topic, long delay) {
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper, groupId));
		this.topic = topic;
		this.delay = delay;
	}

	public void shutdown() {
		if (consumer != null)
			consumer.shutdown();
		if (executor != null)
			executor.shutdown();
	}

	/**
	 * 运行consumer.
	 * 
	 * @param numThreads 线程数
	 */
	public void run(int numThreads) {
		
		//根据topic, thread数得到KafkaStream
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(numThreads));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		//提交到线程池中处理
		executor = Executors.newFixedThreadPool(numThreads);
		int threadNumber = 0;
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			executor.submit(new ConsumerTest(consumer, stream, threadNumber, delay));
			threadNumber++;
		}
	}

	/**
	 * consumer 配置.
	 * 
	 * @param zookeeper zookeeper连接
	 * @param groupId 组名
	 * @return
	 */
	private static ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeper);
		props.put("auto.offset.reset", "largest");
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		//props.put("auto.commit.enable", "false");
		
		return new ConsumerConfig(props);
	}

	public static void main(String[] args) throws InterruptedException {
		String zooKeeper = args[0];
		String groupId = args[1];
		String topic = args[2];
		int threads = Integer.parseInt(args[3]);
		long delay = Long.parseLong(args[4]);
		ConsumerExample example = new ConsumerExample(zooKeeper, groupId, topic,delay);
		example.run(threads);

		Thread.sleep(24*60*60*1000);
		
		example.shutdown();
	}
}