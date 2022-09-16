package com.ppbet.backend.MeetingLivedoc.consumer;

import com.ppbet.backend.MeetingLivedoc.constants.KafkaConstants;
import com.ppbet.backend.MeetingLivedoc.model.MeetingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MeetingListener {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID,
            containerFactory = "meetingKafkaListenerContainerFactory"
    )
    public void listen(MeetingModel meetingModel) {

        System.out.println("sending via kafka listener..");
        System.out.println("Message meeting: " + meetingModel);
        template.convertAndSend("/topicmeeting/meeting", meetingModel);
    }
}
