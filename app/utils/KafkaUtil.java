package utils;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import model.LogRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class KafkaUtil {

    //todo 需要解决logger不可用问题
    //No appenders could be found for logger (org.apache.kafka.clients.producer.ProducerConfig).
    private static final Logger logger = LoggerFactory.getLogger(KafkaUtil.class);

    //ZK config
    private static final String ZK_CONNECTION = "localhost:2181";
    private static final int SESSION_TIMEOUT = 30000;
    private static final int CONNECTION_TIMEOUT = 30000;

    //kafka config
    //设置实例生产消息的总数
    private static final int MSG_SIZE = 100;
    //主题名称
    private static final String TOPIC_NAME = "kafka-test";
    //kafka stream name
    private static final String STREAM_TOPIC_NAME = "kafka-stream-test";
    //kafka集群
    private static final String BROKER_LIST = "localhost:9092";
    //group id
    private static final String GROUP_ID = "test";
    //client id / consumer id
    private static final String CONSUMER_ID = "test";
    //stream id
    private static final String STREAM_APPLICATION_ID = "kafka-stream";
    //生产者
    private static KafkaProducer<String, String> producer = null;
    //消费者
    private static KafkaConsumer<String, String> consumer = null;
    //kafka stream builder
    private static KStreamBuilder streamBuilder = null;


    //init
    static {
        /*
            No appenders could be found for logger . 解决有如下两种方案:
                1.配置文件  classpath下配制log4j.properties
                2.执行BasicConfigurator.configure();
         */
        //该方法的作用是配置kafka的log4j日志， 配置类型为控制台输出ConsoleAppender
        BasicConfigurator.configure(); //防止出现log4j: WARN no appender警告，也可配制解决。

        //建议使用 如下的方法设置日志。 网址：https://www.programcreek.com/java-api-examples/org.apache.log4j.BasicConfigurator
        //setupLog4j();

        Properties producerConfigs = initProducerConfig();
        producer = new KafkaProducer<String, String>(producerConfigs);

        Properties consumerConfigs = initConsumerConfig();
        consumer = new KafkaConsumer<String, String>(consumerConfigs);
        //订阅主题
        subscribeTopics(consumer, Arrays.asList(TOPIC_NAME));
        //提交订阅
        consumer.poll(1000);
        //subscribe() and assign() are lazy -- thus, you also need to do a "dummy call" to poll() before you can use seek().
        //设置偏移量到最新消息处
        consumer.seekToEnd(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
        //consumer.seekToBeginning(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));


        //kafka stream builder
        streamBuilder = new KStreamBuilder();
    }



    public static void createTopic(String topic, int partition, int replication, Properties properties){

        ZkUtils zkUtils = null;
        try{
            zkUtils = ZkUtils.apply(ZK_CONNECTION, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
            if(!AdminUtils.topicExists(zkUtils, topic)){
                //主题不存在，则创建主题
                AdminUtils.createTopic(zkUtils, topic, partition, replication, properties, AdminUtils.createTopic$default$6());
            }else{
                logger.error("kafka topic is already exist. topic: {}, partition: {}, replication: {}, properties: {}",
                        topic, partition, replication, properties);
            }
        } catch (Exception e){
            e.printStackTrace();
            logger.error("create kafka topic exception. topic: {}, partition: {}, replication: {}, properties: {}",
                        topic, partition, replication, properties);
        }finally {
            if(null != zkUtils) {
                zkUtils.close();
            }
        }

    }


    public static void sendMessage(){
        ProducerRecord<String, String> producerRecord = null;
        LogRecord logRecord = null;
        try{
            for(int i=0; i<MSG_SIZE; i++){
                logRecord = getLogRecord(i);
                producerRecord = new ProducerRecord<String, String>(TOPIC_NAME, null, System.currentTimeMillis(), i+"", logRecord.toString());
                //异步发送消息
                //Future<RecordMetadata> task = producer.send(producerRecord);
                //如果想要同步发送消息，则可以调用Future.get()方法
                //RecordMetadata recordMetadata = task.get();

                //如果消息发送可以以回调接口的形式查看消息的元数据
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        //如果exception==null, 则消息发送成功
                        //如果metadata==null，则消息发送失败

                        if(null != exception){
                            logger.error("send message exception. message: {}", exception.getMessage());
                        }

                        if(null != metadata){
                            logger.info(String.format("send message success. offset: %s, partition: %s",
                                    metadata.offset(), metadata.partition()));
                            System.out.println(String.format("send message success. offset: %s, partition: %s",
                                    metadata.offset(), metadata.partition()));
                        }
                    }
                });

                if( (i+1) % 10 == 0){
                    Thread.sleep(500);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            logger.error("send message exception. logRecord: {}, message: {}", logRecord, e.getMessage());
        }finally {
            if(null != producer) {
                producer.close();
            }
        }
    }


    public static void consumeMessage(){
        try{
            while (true){
                //长轮询拉取消息
                ConsumerRecords<String, String> records = consumer.poll(100);
                for(ConsumerRecord<String, String> record : records){
                    String msg = String.format("partition: %s, offset: %s, key: %s, value: %s",
                                record.partition(), record.offset(), record.key(), record.value());
                    logger.info("consume kafka message. msg: {}", msg);
                }

                Thread.sleep(2000);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("consume kafka message exception.");
        }finally {
            if(null != consumer){
                consumer.close();
            }
        }
    }


    public static void streamConsumer(){
        try {
            while(true) {
                //构造KStream日志流
                //KStream<String, String> textLine = streamBuilder.stream(STREAM_TOPIC_NAME);
                //构造KTable
                // storeName the state store name used if this KTable is materialized, can be null if materialization not expected
                KTable<String, String> textLine = streamBuilder.table(TOPIC_NAME, "ktable-test");
                //输入日志流中数据
                textLine.print();

                //开始流
                KafkaStreams streams = new KafkaStreams(streamBuilder, initStreamConfig());
                streams.start();

                Thread.sleep(50000L);
                streams.close();
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private static LogRecord getLogRecord(int i){
        LogRecord logRecord = new LogRecord();

        logRecord.setDatatime(new Date(System.currentTimeMillis()));
        logRecord.setLevel("info");
        logRecord.setThreadName(Thread.currentThread().getName());
        logRecord.setClassName(KafkaUtil.class.getName());
        logRecord.setLoggerContent("message:" + i);

        return logRecord;
    }

    //消费者订阅主题，然后才可以消费消息
    private static void subscribeTopics(KafkaConsumer kafkaConsumer, List<String> topics){
        kafkaConsumer.subscribe(topics);
    }



    private static Properties initProducerConfig(){
        Properties properties = new Properties();
        //kafka broker list 服务器列表
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        //设置序列化类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }


    private static Properties initConsumerConfig(){
        Properties properties = new Properties();
        //kafka broker list 服务器列表
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        //group id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        //client id (consumer id)
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, CONSUMER_ID);
        //设置序列化类
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        return properties;
    }


    private static Properties initStreamConfig(){
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, STREAM_APPLICATION_ID);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }



    // 一般的设置kafka日志的方式
    private static void setupLog4j(String appName) {
        // InputStream inStreamLog4j = getClass().getResourceAsStream("/log4j.properties");
        String propFileName = appName + ".log4j.properties";
        File f = new File("./" + propFileName);
        if (f.exists()) {

            try {
                InputStream inStreamLog4j = new FileInputStream(f);
                Properties propertiesLog4j = new Properties();

                propertiesLog4j.load(inStreamLog4j);
                PropertyConfigurator.configure(propertiesLog4j);
            } catch (Exception e) {
                e.printStackTrace();
                BasicConfigurator.configure();
            }
        } else {
            BasicConfigurator.configure();
        }

        // logger.setLevel(Level.TRACE);
        logger.debug("log4j configured");

    }



}
