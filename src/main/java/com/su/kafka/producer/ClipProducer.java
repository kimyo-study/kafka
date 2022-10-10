package com.su.kafka.producer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClipProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void async(String topic, String message){
        var future = kafkaTemplate.send(topic,message);
        future.addCallback(new KafkaSendCallback<>(){
            @Override
            public void onFailure(KafkaProducerException ex){
                var record = ex.getFailedProducerRecord();
                System.out.println("Fail to send messge. record = " + record);
            }
            @Override
            public void onSuccess(SendResult<String,String> result){
                var record = result.getProducerRecord();
                System.out.println("Success to send message. record = " + record);

            }
        });
    }
    public void sync(String topic, String message){
        var future = kafkaTemplate.send(topic, message);
        try {
            System.out.println("Success to send message sync");
            future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
