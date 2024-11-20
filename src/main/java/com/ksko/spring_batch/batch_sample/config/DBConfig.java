package com.ksko.spring_batch.batch_sample.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    
    @Autowired
    private DataSource dataSource; // Spring 컨테이너가 관리하는 DataSource 빈을 자동으로 주입.
    
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        // MyBatis 설정 파일을 읽어옴. "classpath:mybatis-config.xml" 경로에서 설정 파일을 찾습니다.
        Resource mybatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
        SqlSessionFactoryBean sqlSession = new SqlSessionFactoryBean();

        // 데이터베이스 연결 정보를 MyBatis에 셋팅하기 위해 DataSource를 설정.
        sqlSession.setDataSource(dataSource);

        // 매퍼 XML 파일들을 읽어서, "classpath:mapper/*.xml" 패턴에 따라 모든 매퍼 파일을 읽어옴.
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml");

        // MyBatis에서 사용할 매퍼 XML 파일들의 위치를 설정합니다.
        sqlSession.setMapperLocations(resources);

        // MyBatis의 전체 설정 파일 위치를 설정
        sqlSession.setConfigLocation(mybatisConfig);

        //SqlSessionFactoryBean 객체를 반환하여 Spring 컨테이너가 관리하도록
        return sqlSession;
    }
}


