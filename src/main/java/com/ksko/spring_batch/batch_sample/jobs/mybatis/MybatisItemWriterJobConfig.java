package com.ksko.spring_batch.batch_sample.jobs.mybatis;

import com.ksko.spring_batch.batch_sample.jobs.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class MybatisItemWriterJobConfig {

    // CHUNK 크기를 지정한다.
    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String MY_BATIS_ITEM_WRITER = "MY_BATIS_ITEM_WRITER";

    @Autowired
    DataSource dataSource;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    CustomItemWriter customItemWriter;

    @Bean
    public FlatFileItemReader<Customer> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("FlatFileItemReader")
                .resource(new ClassPathResource("./customer.csv"))
                .encoding(ENCODING)
                .delimited().delimiter(",")
                .names("name", "age", "gender", "grade")  //준수님 블로그에 컬럼에 있는 GRADE 추가
                .targetType(Customer.class)
                .build();
    }
/*

    @Bean
    public MyBatisBatchItemWriter<Customer> myBatisBatchItemWriter() {
        return new MyBatisBatchItemWriterBuilder<Customer>()
                .sqlSessionFactory(sqlSessionFactory) // MyBatis의 SqlSessionFactory 설정. 데이터베이스 연결 및 SQL 실행을 관리
                .statementId("com.ksko.spring_batch.batch_sample.jobs.insertCustomers") // 실행할 SQL 문의 ID 설정. 여기서는 Customer 삽입 SQL을 가리키는 완전한 경로 지정
                .build();
    }


	//@Bean
    public MyBatisBatchItemWriter<Customer> myBatisBatchItemWriter2() {
        return new MyBatisBatchItemWriterBuilder<Customer>()
                .sqlSessionFactory(sqlSessionFactory) // MyBatis의 SqlSessionFactory 설정. 데이터베이스 연결 및 SQL 실행을 관리
                .statementId("com.ksko.spring_batch.batch_sample.jobs.insertCustomers") // 실행할 SQL 문의 ID 설정. 여기서는 Customer 삽입 SQL을 가리키는 완전한 경로 지정
                .itemToParameterConverter(item -> {
                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("name", item.getName());
                    parameter.put("age", item.getAge());
                    parameter.put("gender", item.getGender());
					parameter.put("grade", item.getGrade());
                    return parameter;
                })
                .build();
    }
*/

    @Bean
    public Step flatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init flatFileStep -----------------");

        return new StepBuilder("flatFileStep", jobRepository)
                .<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader())
                //.writer(myBatisBatchItemWriter())
                .writer(customItemWriter)
                .build();
    }

    @Bean
    public Job flatFileJob(Step flatFileStep, JobRepository jobRepository) {
        log.info("------------------ Init flatFileJob -----------------");
        return new JobBuilder(MY_BATIS_ITEM_WRITER, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep)
                .build();
    }

}
