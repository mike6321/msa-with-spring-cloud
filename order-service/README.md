# CREATE QUERY

```sql
create table orders (
       id bigint not null auto_increment,
        order_id varchar(255) not null,
        product_id varchar(120) not null,
        qty integer not null,
        total_price integer not null,
        unit_price integer not null,
        user_id varchar(255) not null,
        primary key (id)
    ) engine=InnoDB
;
alter table orders
       add constraint UK_hmsk25beh6atojvle1xuymjj0 unique (order_id)
;
alter table orders
       add constraint UK_306w8sghgvp5hmjrqo21dscv7 unique (product_id)
```



# Kafka Sink Connect 실행

### Execute

zookeeper, kafka

```shell
cd kaf*
```

```shell
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```

```shell
./bin/kafka-server-start.sh ./config/server.properties
```

sink connect

```shell
cd confluent-6.1.0
```

```shell
./bin/connect-distributed ./etc/kafka/connect-distributed.properties
```



# Sink Connect 생성

```http
### post sink connect
POST http://127.0.0.1:8083/connectors
Content-Type: application/json

{
  "name" : "my-order-sink-connect",
  "config" : {
    "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url": "jdbc:mysql://localhost:3306/msa_order",
    "connection.user": "root",
    "connection.password": "",
    "auto.create": "true",
    "auto.evolve": "true",
    "delete.enabled": "false",
    "tasks.max": "1",
    "topics": "orders"
  }
}
```

