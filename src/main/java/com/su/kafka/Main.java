package com.su.kafka;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // producer
    @Bean
    public ApplicationRunner runner(KafkaTemplate<String,String> kafkaTemplate){
        return args ->{
            kafkaTemplate.send("test-topic", "hello-world");
        };
    }


}
