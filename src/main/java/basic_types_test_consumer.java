import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import kafka_schema_test.BasicTypes;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class basic_types_test_consumer {

    public static void main(String[] args) {

        // Create properties
        Properties properties = new Properties();

        // Normal consumer properties
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.put("group.id", "basic-types-group");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // Avro properties
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        properties.setProperty("specific.avro.reader", "true");

        // Create consumer
        KafkaConsumer<String, BasicTypes> consumer = new KafkaConsumer<>(properties);

        // Subscribe to a topic
        String topic = "basic-avro";
        consumer.subscribe(Collections.singleton(topic));

        // Print to screen
        System.out.println("Waiting for data...");

        // Poll for new data
        while (true) {
            System.out.println("Polling");
            ConsumerRecords<String, BasicTypes> records = consumer.poll(1000);
            for (ConsumerRecord<String, BasicTypes> record : records) {
                BasicTypes test_record = record.value();
                System.out.println(test_record);
            }
            consumer.commitSync();
        }
    }
}
