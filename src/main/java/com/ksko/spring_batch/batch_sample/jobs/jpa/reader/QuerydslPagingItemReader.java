package com.ksko.spring_batch.batch_sample.jobs.jpa.reader;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {
    //어떤 순서로 개발해야하는지 음 ㅠㅠ 쉽지않네유

    //AbstractPagingItemReader -> 어댑터 패턴 (호환되지 않는 인터페이스를 가진 객체들이 함께 작동할 수 있도록 해주는 구조적 패턴)
    //4 (EntityManager 사용을 위한 data jpa 의존성 주입)
    private EntityManager em;
    private final Function<JPAQueryFactory, JPAQuery<T>> querySupplier;

    private final Boolean alwaysReadFromZero;

    public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory, Function<JPAQueryFactory, JPAQuery<T>> querySupplier, int chunkSize) {
        this(ClassUtils.getShortName(QuerydslPagingItemReader.class), entityManagerFactory, querySupplier, chunkSize, false);
    }

    public QuerydslPagingItemReader(String name, EntityManagerFactory entityManagerFactory, Function<JPAQueryFactory, JPAQuery<T>> querySupplier, int chunkSize, Boolean alwaysReadFromZero) {
        super.setPageSize(chunkSize); //페이징 처리 위한 페이지 크기
        setName(name); //itemReader 구분 위한 이름
        this.querySupplier = querySupplier;
        this.em = entityManagerFactory.createEntityManager();
        this.alwaysReadFromZero = alwaysReadFromZero; //0부터 페이징 읽을지 여부 지정

    }

    @Override
    protected void doClose() throws Exception { //doClose는 기본적으로 AbstractPagingItemReader를 자체 구현되어 있지만 EntityManager자원을 해제하기 위해서 em.close() 를 수행
        if(em != null) {
            em.close();
        }
        super.doClose();
    }

    @Override
    protected void doReadPage() { //실제로 우리가 구현해야할 추상 메소드
        //1
        initQueryResult();

        //3 (사용 위한 querydsl 의존성 주입 필요)
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        long offset = 0;
        if (alwaysReadFromZero) {
            offset = (long) getPage() * getPageSize();
        }

        JPAQuery<T> query = querySupplier.apply(jpaQueryFactory).offset(offset).limit(getPageSize());

        List<T> queryResult = query.fetch();
        for (T entity: queryResult) {
            em.detach(entity);
            results.add(entity);
        }

    }

    private void initQueryResult() {
        //2
        if (CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }


}
