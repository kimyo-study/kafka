package com.su.kafka.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
public class KafkaManager {

    private final KafkaAdmin kafkaAdmin;
    private final AdminClient adminclient;

    public KafkaManager(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
        this.adminclient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }

    public void decribeTopicConfigs() throws ExecutionException, InterruptedException {
        Collection<ConfigResource> resources = List.of(
            new ConfigResource(ConfigResource.Type.TOPIC, "clip4-listener")
        );
        var result = adminclient.describeConfigs(resources);
        System.out.println(result.all().get());
    }

}
