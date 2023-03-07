import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumidor {
    public static void main(String[] args){
        Properties props = new Properties();

        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        props.put("group.id","grupo1");
        props.put("enable.auto.commit","true");
        props.put("auto.commit.interval.ms","1000");

        //Cantidad mínima de datos que quiere recibir del broker cuando lee registros.
        //Si el consumidor usa mucha CPU se recomienda aumentar este valor por encima del de por defecto
        props.put("fetch.min.bytes", "1");

        //Cuanto espera en el caso de que el broker no tenga suficiente datos.
        props.put("fetch.max.wait.ms", "500");

        //Controla el máximo número de byte que se devolverá por cada partición asignada al consumidor
        props.put("max.partition.fetch.bytes","1048576");

        //Tiempo que un consumidor se considera vivo sin enviar un mensaje de estado a los brokers
        props.put("session.timeout.ms","10000");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        try{
            consumer.subscribe(Collections.singletonList("topic-test"));

            while(true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

                for(ConsumerRecord<String, String> record: records){
                    System.out.print("Topic: " + record.topic() + ", ");
                    System.out.print("Partition: " + record.partition() + ", ");
                    System.out.print("Key: " + record.key() + ", ");
                    System.out.print("Value: " + record.value() + ", ");
                }
            }
        }catch (Exception e){e.printStackTrace();}

        finally {
            consumer.close();
        }
    }
}
