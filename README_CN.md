这个项目提供了Kafka Producer 和 High level consumer的例子。
所有的例子都提供了两个版本: Java版本和Scala版本。 可以用来用作编写Kafka producer和consumer的模板。

你可以在单机上进行测试。下面是测试参考。 这里Kafka部署成一个节点，在单机上你也可以将Kafka部署成一个集群。

### 启动zookeeper
如果你已经启动了zookeeper,跳过这一步。
否则必须先启动zookeeper。 运行下面的命令:
``` sh
bin/zookeeper-server-start.sh config/zookeeper.properties
```

### 使用默认配置启动kafka
kafka提供了很多配置参数用来调优，但是这里我们使用它的默认设置启动。
``` sh
> bin/kafka-server-start.sh config/server.properties
```

### 创建一个topic
``` sh
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 10 --topic test_topic
```

### 打包代码
``` sh
mvn clean package
```
运行上面的命令会将java代码和scala代码编译打包，并且还会将依赖类打包到一个jar文件中。

### 运行 consumer
这个例子包含两套代码。
运行java编写的consumer:
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ConsumerExample localhost:9092 group1 test_topic 10 0
```

或者运行scala编写的consumer:
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ScalaConsumerExample localhost:9092 group1 test_topic 10 0
```

### 运行 producer
这个例子包含两个producer例子，分别由java和scala编写。
运行java编写的producer:
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ProducerExample 10000 colobu localhost:9092
```
运行scala的例子。
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ScalaProducerExample 10000 colobu localhost:9092
```
