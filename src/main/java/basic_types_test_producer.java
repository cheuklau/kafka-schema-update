import io.confluent.kafka.serializers.KafkaAvroSerializer;
import kafka_schema_test.BasicTypes;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class basic_types_test_producer {

    public static void main(String[] args) {

        // Create properties
        Properties properties = new Properties();

        // Normal producer properties
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "10");

        // Avro properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        // Create producer
        Producer<String, BasicTypes> producer = new KafkaProducer<>(properties);

        // Topic to write to
        String topic = "basic-avro";

        // Create a record using primitive type schema
        // This schema will be automatically added to the registry
        BasicTypes test_record = BasicTypes.newBuilder()
                .setBooleanTest(true)
                .setFloatTest(0.0f)
                .setIntTest(0)
                .setLongTest(0)
                .setNullTest(null)
                .setStringTest("foo")
                .build();
        ProducerRecord<String, BasicTypes> producerrecord = new ProducerRecord<>(topic, test_record);

        // Print to screen
        System.out.println(producerrecord);

        // Send to Kafka
        producer.send(producerrecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println(metadata);
                } else {
                    exception.printStackTrace();
                }
            }
        });

        // Flush and close producer
        producer.flush();
        producer.close();

    }
}
