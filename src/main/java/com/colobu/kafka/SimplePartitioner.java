package com.colobu.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * 简单分区类.
 * 得到ip地址的最后一个数，然后对分区数取余. 
 */
public class SimplePartitioner implements Partitioner {
	public SimplePartitioner(VerifiableProperties props) {

	}

	public int partition(Object key, int numPartitions) {
		int partition = 0;
		String stringKey = (String) key;
		int offset = stringKey.lastIndexOf('.');
		if (offset > 0) {
			partition = Integer.parseInt(stringKey.substring(offset + 1)) % numPartitions;
		}
		return partition;
	}

}