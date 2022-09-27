package co.gov.igac.snc.structureXtf.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;

import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDtoKafka;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.user}")
    private String kafkaUser;

    @Value("${kafka.pwd}")
    private String kafkaPwd;

    @Bean
     public ProducerFactory<String, ResponseArchivoDtoKafka> userProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        //configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT"); 
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
       configProps.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-512");
        configProps.put("acks", "all");
     configProps.put("retries", 0);
    configProps.put("batch.size", 16384);
    configProps.put("linger.ms", 1);
    configProps.put("buffer.memory", 33554432);
    configProps.put("sasl.jaas.config","org.apache.kafka.common.security.scram.ScramLoginModule required username='"+kafkaUser+"' password='"+kafkaPwd+"';");
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public KafkaTemplate<String, ResponseArchivoDtoKafka> userKafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }
}