package com.ppbet.backend.EventLivedoc.controller;

import com.ppbet.backend.EventLivedoc.constants.KafkaConstants;
import com.ppbet.backend.EventLivedoc.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
public class EventController {

    @Autowired
    private KafkaTemplate<String, EventModel> kafkaTemplate;

    @PostMapping(value = "/api/sendevent", consumes = "application/json", produces = "application/json")
    public void sendEvent(@RequestBody EventModel eventModel) {
        eventModel.setTimestamp(LocalDateTime.now().toString());
        try {
            //Sending the message to kafka topic queue
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, eventModel).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    //    -------------- WebSocket API ----------------
    @MessageMapping("/event")
    @SendTo("/topicevent/event")
    public EventModel getAllEvents(@Payload EventModel eventModel) {
        //Sending this message to all the subscribers
        return eventModel;
    }

//    @MessageMapping("/event/{eventId}")
//    @SendTo("/topicevent/event")
//    public EventModel EventwithId(@PathVariable String eventId) {
//        //TODO: get the event with the event Id from the database
//        return null;
//    }
}
