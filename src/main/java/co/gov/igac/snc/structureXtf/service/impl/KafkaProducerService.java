package co.gov.igac.snc.structureXtf.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDtoKafka;


@Service
public class KafkaProducerService {
	
    private final Logger LOG = LoggerFactory.getLogger(KafkaProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, ResponseArchivoDtoKafka> kafkaTemplate;

    private final String KAFKA_TOPIC = "validacion-xtf";

    public void send(ResponseArchivoDtoKafka user) {
        LOG.info("Sending User Json Serializer Kafka: ", user);
        kafkaTemplate.send(KAFKA_TOPIC, user);
    }
}
