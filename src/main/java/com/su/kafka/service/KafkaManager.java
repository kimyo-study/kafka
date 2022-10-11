package com.su.kafka.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DeleteRecordsResult;
import org.apache.kafka.clients.admin.DeletedRecords;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
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
    public void describeTopicConfigs() throws ExecutionException, InterruptedException {
        Collection<ConfigResource> resources = List.of(
            new ConfigResource(ConfigResource.Type.TOPIC, "clip4-listener")
        );
        var result = adminclient.describeConfigs(resources);
        System.out.println(result.all().get());
    }
    public void changeConfig() throws ExecutionException, InterruptedException {
        var resource = new ConfigResource(ConfigResource.Type.TOPIC, "clip4-listener");
        Map<ConfigResource, Collection<AlterConfigOp>> ops = new HashMap<>();
        ops.put(resource, List.of(
            new AlterConfigOp((new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG,"6000")), AlterConfigOp.OpType.SET))
        );
        adminclient.incrementalAlterConfigs(ops);
        describeTopicConfigs();
    }
    public void deleteRecords() throws ExecutionException, InterruptedException {
        TopicPartition tp = new TopicPartition("clip4-listener",0);
        Map<TopicPartition, RecordsToDelete> target = new HashMap<>();
        target.put(tp, RecordsToDelete.beforeOffset(1));

        DeleteRecordsResult deleteRecordsResult = adminclient.deleteRecords(target);
        Map<TopicPartition, KafkaFuture<DeletedRecords>> result = deleteRecordsResult.lowWatermarks();

        Set<Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>>> entries = result.entrySet();
        for (Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>> entry: entries){
            System.out.println("topic= "+entry.getKey().topic()
                + ", partition "+ entry.getKey().partition()
                + " , "+entry.getValue().get().lowWatermark());
        }
    }
    public void findAllConsumerGroups() throws ExecutionException, InterruptedException {
        var result = adminclient.listConsumerGroups();
        var groups = result.valid().get();
        for(var group: groups){
            System.out.println(group);
        }
    }
    public void deleteConsumerGroup() throws ExecutionException, InterruptedException {
        adminclient.deleteConsumerGroups(List.of("clip3-id","clip4-animal-listener")).all().get();
    }
    public void findAllOffsets(){
        Map<TopicPartition, OffsetSpec> target = new HashMap<>();
        target.put(new TopicPartition("clip4-listener", 0), OffsetSpec.latest());
        var result = adminclient.listOffsets(target);
        for (TopicPartition tp: target.keySet()){
            System.out.println("topic= "+tp.topic()+", partition= "+tp.partition()+", offset= "+result.partitionResult(tp));
        }
    }
}
