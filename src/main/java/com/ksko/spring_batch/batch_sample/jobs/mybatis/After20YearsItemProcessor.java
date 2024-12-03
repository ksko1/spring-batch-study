package com.ksko.spring_batch.batch_sample.jobs.mybatis;

import com.ksko.spring_batch.batch_sample.jobs.models.Customer;
import org.springframework.batch.item.ItemProcessor;

/**
 * 나이에 20년 더하는 ItemProcessor
 */
public class After20YearsItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {
        item.setAge(item.getAge() + 20);
        return item;
    }
}
