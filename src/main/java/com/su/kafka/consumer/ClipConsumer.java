package com.su.kafka.consumer;

import java.util.Date;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.su.kafka.model.Animal;

@Service
public class ClipConsumer {

    @KafkaListener(id= "clip4-listener-id", topics= "clip4-listener", concurrency = "2")
    public void listen(
        String message,
        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timeStamp,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        ConsumerRecordMetadata metadata
    ){
        System.out.println("Listener. " +
            " offset="+metadata.offset() +
            " partition="+partition+
            " time="+new Date(timeStamp)+
            " message="+message
        );
    }
    @KafkaListener(id= "clip4-animal-id", topics="clip4-animal", containerFactory = "kafkaJsonContainerFactory")
    public void listenAnimal(Animal animal){
        System.out.println("Animal. animal="+animal);
    }
}
