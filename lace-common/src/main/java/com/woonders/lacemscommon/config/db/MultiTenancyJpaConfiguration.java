package com.woonders.lacemscommon.config.db;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Amministrazione;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emanuele on 23/12/16.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableJpaRepositories(entityManagerFactoryRef = MultiTenancyJpaConfiguration.MULTI_TENANT_ENTITY_MANAGER, transactionManagerRef = MultiTenancyJpaConfiguration.MULTI_TENANT_TRANSACTION_MANAGER, basePackages = {
        Constants.REPOSITORY_PACKAGE})
@EnableTransactionManagement
public class MultiTenancyJpaConfiguration {

    // questi nomi sono gli stessi assegnati ai bean sottostanti tramite il nome
    // del metodo
    public static final String MULTI_TENANT_ENTITY_MANAGER = "multiTenantEntityManager";
    public static final String MULTI_TENANT_TRANSACTION_MANAGER = "multiTenantTransactionManager";
    private final String KEY_RDS_USERNAME = "RDS_USERNAME";
    private final String KEY_RDS_PASSWORD = "RDS_PASSWORD";
    private final String KEY_RDS_HOSTNAME = "RDS_HOSTNAME";
    private final String KEY_RDS_PORT = "RDS_PORT";

    @Primary
    @Bean
    public DataSource multiTenantDataSource() {
        final HikariConfig config = new HikariConfig();
        final String username = System.getProperty(KEY_RDS_USERNAME);
        final String password = System.getProperty(KEY_RDS_PASSWORD);
        final String hostname = System.getProperty(KEY_RDS_HOSTNAME);
        final String port = System.getProperty(KEY_RDS_PORT);
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("net.bull.javamelody.JdbcDriver");
        config.addDataSourceProperty("driver", "com.mysql.jdbc.Driver");
        config.addDataSourceProperty("useSSL", false);
        config.setPoolName("MultiTenantDataSource");
        return new HikariDataSource(config);
    }

    @Primary
    @Bean
    public JpaVendorAdapter multiTenantJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Primary
    @Bean
    public JpaTransactionManager multiTenantTransactionManager(final EntityManagerFactory multiTenantEntityManager) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(multiTenantEntityManager);
        return transactionManager;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean multiTenantEntityManager(
            final MultiTenantConnectionProvider multiTenantConnectionProvider,
            final CurrentTenantIdentifierResolver tenantIdentifierResolver) {
        final LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(multiTenantDataSource());
        emfBean.setPackagesToScan(Amministrazione.class.getPackage().getName());
        emfBean.setJpaVendorAdapter(multiTenantJpaVendorAdapter());

        final Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                multiTenantConnectionProvider);
        jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        emfBean.setJpaPropertyMap(jpaProperties);
        emfBean.afterPropertiesSet();
        return emfBean;
    }

}
