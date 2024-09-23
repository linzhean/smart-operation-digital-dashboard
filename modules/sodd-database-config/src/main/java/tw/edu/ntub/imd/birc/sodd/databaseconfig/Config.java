package tw.edu.ntub.imd.birc.sodd.databaseconfig;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.*;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration("databaseConfig")
@EnableJpaRepositories(
        basePackages = "tw.edu.ntub.imd.birc.sodd.databaseconfig.dao",
        entityManagerFactoryRef = "mysqlEntityManagerFactory",
        transactionManagerRef = "mysqlTransactionManager")
@EnableTransactionManagement
@EntityScan(basePackages = "tw.edu.ntub.imd.birc.sodd.databaseconfig.entity")
public class Config {
    public static final String DATABASE_NAME = "113-smart_operation_digital_dashboard";


    @Primary
    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mysqlDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("tw.edu.ntub.imd.birc.sodd.databaseconfig.entity")
                .persistenceUnit("mysql")
                .build();
    }

    @Primary
    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(
            @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name = "mssqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mssqlEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mssqlDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp")
                .persistenceUnit("mssql")
                .build();
    }

    @Bean(name = "mssqlTransactionManager")
    public PlatformTransactionManager mssqlTransactionManager(
            @Qualifier("mssqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public TransactionInterceptor transactionInterceptor(TransactionManager transactionManager) {
        NameMatchTransactionAttributeSource attributeSource = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute requiredAttribute = new RuleBasedTransactionAttribute();
        RollbackRuleAttribute rollbackRuleAttribute = new RollbackRuleAttribute(RuntimeException.class);
        requiredAttribute.setRollbackRules(Collections.singletonList(rollbackRuleAttribute));
        requiredAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        DefaultTransactionAttribute readOnlyTransactionAttributes =
                new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        readOnlyTransactionAttributes.setReadOnly(true);
        Map<String, TransactionAttribute> namedMap = new HashMap<>();
        namedMap.put("add*", requiredAttribute);
        namedMap.put("save*", requiredAttribute);
        namedMap.put("create*", requiredAttribute);
        namedMap.put("update*", requiredAttribute);
        namedMap.put("logout", requiredAttribute);
        namedMap.put("delete*", requiredAttribute);
        namedMap.put("find*", readOnlyTransactionAttributes);
        namedMap.put("get*", readOnlyTransactionAttributes);
        namedMap.put("search*", readOnlyTransactionAttributes);
        namedMap.put("getCount*", readOnlyTransactionAttributes);
        namedMap.put("*", readOnlyTransactionAttributes);
        attributeSource.setNameMap(namedMap);
        return new TransactionInterceptor(transactionManager, attributeSource);
    }
}

