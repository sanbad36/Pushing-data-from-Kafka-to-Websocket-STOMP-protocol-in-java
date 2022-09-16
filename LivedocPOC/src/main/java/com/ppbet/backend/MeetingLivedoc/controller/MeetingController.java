package com.ppbet.backend.MeetingLivedoc.controller;

import com.ppbet.backend.MeetingLivedoc.constants.KafkaConstants;
import com.ppbet.backend.MeetingLivedoc.model.MeetingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
public class MeetingController {

    @Autowired
    private KafkaTemplate<String, MeetingModel> kafkaTemplate;

    @PostMapping(value = "/api/sendmeeting", consumes = "application/json", produces = "application/json")
    public void sendMessage(@RequestBody MeetingModel meetingModel) {
        meetingModel.setTimestamp(LocalDateTime.now().toString());
        try {
            //Sending the message to kafka topic queue
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, meetingModel).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    //    -------------- WebSocket API ----------------
    @MessageMapping("/sendmeeting")
    @SendTo("/topicmeeting/meeting")
    public MeetingModel getAllMeetings(@Payload MeetingModel meetingModel) {
        //Sending this message to all the subscribers
        return meetingModel;
    }

//    @MessageMapping("/meeting/{meetingid}")
//    @SendTo("/topicmeeting/meeting")
//    public MeetingModel meetingwithId( @PathVariable String meetingId) {
//        //TODO: get the meeting with the meeting Id from the database
//        return null;
//    }
}
