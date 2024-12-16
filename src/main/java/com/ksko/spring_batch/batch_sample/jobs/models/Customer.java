package com.ksko.spring_batch.batch_sample.jobs.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private int age;
    private String gender;
    private String grade; //준수님 DB 깔 맞추기위해 추가
}
