package co.gov.igac.snc.structureXtf.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDTOKafka;


@Service
public class KafkaProducerServiceImpl {
	
    private final Logger LOG = LoggerFactory.getLogger(KafkaProducerServiceImpl.class);
    
    @Autowired
    private KafkaTemplate<String, ResponseArchivoDTOKafka> kafkaTemplate;

    private final String KAFKA_TOPIC = "validacion-xtf";

    public void send(ResponseArchivoDTOKafka user) {
        LOG.info("Sending User Json Serializer Kafka: ", user);
        kafkaTemplate.send(KAFKA_TOPIC, user);
    }
}
