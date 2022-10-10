package com.su.kafka;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.su.kafka.producer.ClipProducer;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    public ApplicationRunner runner(ClipProducer clipProducer){
        return args -> {
            clipProducer.async("cli3", "Hello Clip3-async");
            clipProducer.sync("clip3", "Hello Clip3-sync");
            clipProducer.routingSend("clip3", "Hello Clip3-routing");
            clipProducer.routingSendBytes("clip3-bytes","Hello Clip3-bytes".getBytes(StandardCharsets.UTF_8));
            clipProducer.replyingSend("clip3-request","Ping Clip3");
        };
    }
    private ApplicationRunner amdinMethod(AdminClient adminClient){
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
        };
    }
}
