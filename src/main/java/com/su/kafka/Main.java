package com.su.kafka;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

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
            clipProducer.async("clip4", "Hello, Clip4 Container!");
            kafkaMessageListenerContainer.start();

            Thread.sleep(1000);
            System.out.println(" -- pause -- ");
            kafkaMessageListenerContainer.pause();
            Thread.sleep(3000);
            clipProducer.async("clip4", "Hello Secondly Clip4 Conainer!");

            System.out.println(" -- resume --");
            kafkaMessageListenerContainer.resume();
            Thread.sleep(1000);
            System.out.println(" -- stop --");
            kafkaMessageListenerContainer.stop();

        };
    }
}
