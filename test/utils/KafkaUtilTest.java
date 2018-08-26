package utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUtilTest.class);

    @Test
    public void createTopic(){
        String topic = "kafka-stream-test";
        int partition = 1;
        int replication = 1;

        KafkaUtil.createTopic(topic, partition, replication, new Properties());
    }


    @Test
    public void sendMessage(){
        KafkaUtil.sendMessage();
    }

    @Test
    public void consumeMessage(){
        KafkaUtil.consumeMessage();
    }


    @Test
    public void streamConsume(){
        KafkaUtil.streamConsumer();
    }


    @Test
    public void simulationLog() throws Exception{
        int slowCount = 6;
        int fastCount = 15;
        while (true)        {
            for(int i = 0; i < slowCount; i++){
                logger.info("This is a warning (slow state).");
                Thread.sleep(5000);
            }
            for(int i = 0; i < fastCount; i++){
                logger.warn("This is a warning (rapid state).");
                Thread.sleep(1000);
            }
            for(int i = 0; i < slowCount; i++){
                logger.error("This is a warning (slow state).");
                Thread.sleep(5000);
            }
        }


    }


}
