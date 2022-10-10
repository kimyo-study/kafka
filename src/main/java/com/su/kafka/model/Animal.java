package com.su.kafka.model;

import javax.validation.constraints.Max;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Animal {
    private final String name;

    @Max(10)
    private final int age;

    @JsonCreator
    public Animal(
        @JsonProperty("name") String name,
        @JsonProperty("age") int age
    ) {
        this.name = name;
        this.age = age;
    }
}
