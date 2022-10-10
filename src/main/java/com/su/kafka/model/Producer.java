package com.su.kafka.model;

import java.util.Collections;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Configuration
public class Producer {
    @Bean
    public ApplicationRunner runner(
        KafkaTemplate<String,String> kafkaTemplate,
        AdminClient adminClient
    ){
        return args ->{
            var topics = adminClient.listTopics().namesToListings().get();
            for (var topicName: topics.keySet()){
                var topicListing = topics.get(topicName);
                System.out.println(topicListing);
                var description = adminClient.describeTopics(Collections.singleton(topicName)).all();
                if(!topicListing.isInternal()){
                    adminClient.deleteTopics(Collections.singleton(topicName));
                }
            }
            kafkaTemplate.send("test-topic", "hello-world");


        };
    }
}
