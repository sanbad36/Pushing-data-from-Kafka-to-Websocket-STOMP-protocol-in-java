package com.ppbet.backend.EventLivedoc.consumer;

import com.ppbet.backend.EventLivedoc.constants.KafkaConstants;
import com.ppbet.backend.EventLivedoc.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventListener {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID,
            containerFactory = "eventKafkaListenerContainerFactory"
    )
    public void listen(EventModel eventModel) {

        System.out.println("sending via kafka listener..");
        System.out.println("Message Events: " + eventModel);
        template.convertAndSend("/topicevent/event", eventModel);
    }
}
