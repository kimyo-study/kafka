package com.su.kafka;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import com.su.kafka.model.Animal;
import com.su.kafka.producer.ClipProducer;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public ApplicationRunner runner(
        ClipProducer clipProducer,
        KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer
    ){
        return args -> {
            clipProducer.async("clip4-animal", new Animal("puppy",1));
            Thread.sleep(3000);
        };
    }
}
