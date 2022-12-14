# Kafka



## Command



### Zookeeper 실행

```sh
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```

### Kafka 실행

```sh
./bin/kafka-server-start.sh ./config/server.properties
```

### 

### 토픽 생성

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic quickstart-events --partitions 1
```

### 토픽 확인

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

### 토픽 상세 확인

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic quickstart-events
```



### Producer 실행

```sh
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic quickstart-events
```

### Consumer 실행

```sh
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning
```



