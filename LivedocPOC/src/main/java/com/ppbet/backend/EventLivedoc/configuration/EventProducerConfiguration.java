package com.ppbet.backend.EventLivedoc.configuration;

import com.ppbet.backend.EventLivedoc.model.EventModel;
import com.ppbet.backend.MeetingLivedoc.constants.KafkaConstants;
import com.ppbet.backend.MeetingLivedoc.model.MeetingModel;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class EventProducerConfiguration {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;
    @Bean
    public ProducerFactory<String, EventModel> userProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EventModel> userKafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }
}