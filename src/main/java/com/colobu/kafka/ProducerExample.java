package com.colobu.kafka;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * Producer例子，可以往topic上发送指定数量的消息.
 * 消息格式为: 发送时间,编号,网址,ip
 *
 */
public class ProducerExample {
	public static void main(String[] args) {
        long events = Long.parseLong(args[0]);
        String topic = args[1];
        String brokers = args[2];
        Random rnd = new Random();
 
        Properties props = new Properties();
        props.put("metadata.broker.list", brokers);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", "com.colobu.kafka.SimplePartitioner");
        props.put("producer.type", "async");
        //props.put("request.required.acks", "1");
 
        ProducerConfig config = new ProducerConfig(props);
 
        Producer<String, String> producer = new Producer<String, String>(config);
 
        long t = System.currentTimeMillis();
        for (long nEvents = 0; nEvents < events; nEvents++) { 
               long runtime = new Date().getTime();  
               String ip = "192.168.2." + rnd.nextInt(255); 
               String msg = runtime + "," + nEvents + ",www.example.com," + ip; 
               KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, ip, msg);
               producer.send(data);
        }
        
        System.out.println("sent per second: " + events * 1000/ (System.currentTimeMillis() - t));
        producer.close();
    }
}