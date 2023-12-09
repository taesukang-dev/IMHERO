package com.imhero.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Profile("local")
@Configuration
@RequiredArgsConstructor
public class ReplicationDatasourceConfig {
    private final JpaProperties jpaProperties;
    private final ReplicationDataSourceProperties dataSourceProperties;

    @Bean
    public DataSource routingDataSource() {
        Map<Object, Object> targetDataSources = new LinkedHashMap<>();

        DataSource masterDataSource = createDataSource(
                dataSourceProperties.getDriverClassName(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword(),
                dataSourceProperties.getUrl()
        );
        targetDataSources.put(DataSourceType.MASTER.getName(), masterDataSource);

        for (ReplicationDataSourceProperties.Slave slave : dataSourceProperties.getSlaves().values()) {
            DataSource slaveDataSource = createDataSource(
                    slave.getDriverClassName(),
                    slave.getUsername(),
                    slave.getPassword(),
                    slave.getUrl()
            );
            targetDataSources.put(slave.getName(), slaveDataSource);
        }

        ReplicationRoutingSource routingDataSource = new ReplicationRoutingSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    private DataSource createDataSource(String driverClassName, String userName, String password, String uri) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(driverClassName)
                .username(userName)
                .password(password)
                .url(uri)
                .build();
    }

    @Bean
    public DataSource lazyRoutingDataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("lazyRoutingDataSource") DataSource dataSource) {
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
        return entityManagerFactoryBuilder.dataSource(dataSource)
                .packages("com.imhero")
                .build();
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
