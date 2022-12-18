# Kafka Basic

#### Zookeeper 실행

```sh
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```

#### Kafka 실행

```sh
./bin/kafka-server-start.sh ./config/server.properties
```

#### 토픽 생성

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic quickstart-events --partitions 1
```

#### 토픽 확인

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

#### 토픽 상세 확인

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic quickstart-events
```

#### Producer 실행

```sh
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic quickstart-events
```

#### Consumer 실행

```sh
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning
```



# Kafka Connect



### Kafka Connect 설치

```sh
curl -O http://packages.confluent.io/archive/6.1/confluent-community-6.1.0.tar.gz
```

```sh
tar xvf confluent-community-6.1.0.tar.gz
```



### connect 실행

```sh
./bin/connect-distributed ./etc/kafka/connect-distributed.properties
```



#### 토픽 조회

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

<img width="1273" alt="image" src="https://user-images.githubusercontent.com/33277588/208297718-153b9e25-9cd2-49ff-9e0f-9ceeb0460c86.png">



### Jdbc Connecntor 설치

https://docs.confluent.io/5.5.1/connect/kafka-connect-jdbc/index.html

```sh
/Users/nhn/IdeaProjects/repository/confluentinc-kafka-connect-jdbc-10.6.1/lib
```



### 플러그인 설정

```sh
vi ./etc/kafka/connect-distributed.properties
```

* confluentinc-kafka-connect-jdbc-10.6.1/lib 경로로 설정

<img width="1428" alt="image" src="https://user-images.githubusercontent.com/33277588/208298295-ab008f29-0be1-442f-92ef-0e167ce293f7.png">



### mysql connector jar 복사

```sh
cp /Users/nhn/.m2/repository/mysql/mysql-connector-java/8.0.30/mysql-connector-java-8.0.30.jar /Users/nhn/IdeaProjects/repository/confluent-6.1.0/share/java/kafka	
```



# Kafka Source Connect

### connect 실행

```sh
./bin/connect-distributed ./etc/kafka/connect-distributed.properties
```

### post source connect

```http
### post source connect
POST http://127.0.0.1:8083/connectors
Content-Type: application/json

{
  "name" : "my-source-connect2",
  "config" : {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url": "jdbc:mysql://localhost:3306/msa_order",
    "connection.user": "root",
    "connection.password": "Rownsdn91@",
    "mode": "incrementing",
    "incrementing.column.name": "id",
    "table.whitelist": "msa_order.users",
    "topic.prefix": "my_topic_",
    "tasks.max": "1"
  }
}
```

### get source connect

```http
### get source connect
GET http://127.0.0.1:8083/connectors/my-source-connect2/status
Accept: application/json
```

### DB INSERT

```sql
insert into users (user_id, pwd, name) values ('user1', 'test1111', 'user name');
insert into users (user_id, pwd, name) values ('user2', 'test1111', 'user name');
insert into users (user_id, pwd, name) values ('user3', 'test1111', 'user name');
```

### 토픽 확인

```sh
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

#### <img width="1379" alt="image" src="https://user-images.githubusercontent.com/33277588/208301269-d7e1b8f3-b158-4a6e-9cdd-16139e94346a.png">

### Consumer 확인

```sh
 ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my_topic_users --from-beginning
```

<img width="2546" alt="image" src="https://user-images.githubusercontent.com/33277588/208301445-302ddae4-437b-476d-b7a2-01f927b3d897.png">

