package com.ppbet.backend.MeetingLivedoc.configuration;

import com.ppbet.backend.EventLivedoc.model.EventModel;
import com.ppbet.backend.MeetingLivedoc.model.MeetingModel;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class MeetingListenerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${event.topic.group.id}")
    private String eventGroupId;



    public ConsumerFactory<String, MeetingModel> eventConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, eventGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(MeetingModel.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MeetingModel>
    meetingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MeetingModel> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventConsumerFactory());
        return factory;
    }
}