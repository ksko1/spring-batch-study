package com.ksko.spring_batch.batch_sample.jobs.mybatis;

import com.ksko.spring_batch.batch_sample.jobs.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CustomService {

    public Map<String, String> processToOtherService(Customer item) {
        log.info("Call API to OtherService....");

        return Map.of("code", "200", "message", "OK");
    }
}
