import io.confluent.kafka.serializers.KafkaAvroSerializer;
import kafka_schema_test.AllTypes01020;
import kafka_schema_test.enum_test;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class all_types_test_producer {

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
        Producer<String, AllTypes01020> producer = new KafkaProducer<>(properties);

        // Topic to write to
        String topic = "topic01020";

        // Create a record using primitive type schema
        // This schema will be automatically added to the registry
        List<String> array_test_value = new ArrayList<>();
        array_test_value.add("array");
        array_test_value.add("of");
        array_test_value.add("strings");
        AllTypes01020 test_record = AllTypes01020.newBuilder()
                .setArrayTest(array_test_value)
                .setEnumTest(enum_test.enum1)
                .setUnionTest("not a null")
                .setBooleanTest(true)
                .setFloatTest(0.0f)
                .setIntTest(0)
                .setLongTest(0)
                .setNullTest(null)
                .setStringTest("foo")
                .build();
        ProducerRecord<String, AllTypes01020> producerrecord = new ProducerRecord<>(topic, test_record);

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
