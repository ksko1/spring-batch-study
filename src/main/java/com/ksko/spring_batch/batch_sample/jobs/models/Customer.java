package com.ksko.spring_batch.batch_sample.jobs.models;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private String grade; //준수님 DB 깔 맞추기위해 추가
}
