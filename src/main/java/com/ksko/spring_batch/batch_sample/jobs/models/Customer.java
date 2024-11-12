package com.ksko.spring_batch.batch_sample.jobs.models;

import lombok.Data;

@Data
public class Customer {
    private String name;
    private int age;
    private String gender;
}
