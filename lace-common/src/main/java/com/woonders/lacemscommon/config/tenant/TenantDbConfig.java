package com.woonders.lacemscommon.config.tenant;

import javax.sql.DataSource;

import com.woonders.lacemscommon.db.tenantrepository.SmsTenant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.woonders.lacemscommon.db.tenantrepository.Tenant;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Created by Emanuele on 24/03/2017.
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = TenantDbConfig.TENANT_DB_ENTITY_MANAGER,
		transactionManagerRef = TenantDbConfig.TENANT_DB_TRANSACTION_MANAGER,
		basePackages = {"com.woonders.lacemscommon.db.tenantrepository" })
@EnableTransactionManagement
public class TenantDbConfig {

	// questi nomi sono gli stessi assegnati ai bean sottostanti tramite il nome
	// del metodo
	public static final String TENANT_DB_ENTITY_MANAGER = "tenantDbEntityManager";
	public static final String TENANT_DB_TRANSACTION_MANAGER = "tenantDbTransactionManager";
	private final String KEY_RDS_USERNAME = "RDS_USERNAME";
	private final String KEY_RDS_PASSWORD = "RDS_PASSWORD";
	private final String KEY_RDS_HOSTNAME = "RDS_HOSTNAME";
	private final String KEY_RDS_PORT = "RDS_PORT";

	@Bean
	public DataSource tenantDbDataSource() {
		final HikariConfig config = new HikariConfig();
		final String username = System.getProperty(KEY_RDS_USERNAME);
		final String password = System.getProperty(KEY_RDS_PASSWORD);
		final String hostname = System.getProperty(KEY_RDS_HOSTNAME);
		final String port = System.getProperty(KEY_RDS_PORT);
		config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/tenant");
		config.setUsername(username);
		config.setPassword(password);
		config.setDriverClassName("net.bull.javamelody.JdbcDriver");
		config.addDataSourceProperty("driver", "com.mysql.jdbc.Driver");
		config.addDataSourceProperty("useSSL", false);
		config.setPoolName("TenantDbDataSource");
		return new HikariDataSource(config);
	}


	@Bean
	public JpaVendorAdapter tenantDbJpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	// http://stackoverflow.com/questions/32164430/spring-jpa-repository-is-not-working-with-two-transaction-managers
	@Bean
	public JpaTransactionManager tenantDbTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(tenantDbEntityManager().getObject());
		return transactionManager;
	}

	@Bean
	public JpaTransactionManager smsTenantDbTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(smsTenantDbEntityManager().getObject());
		return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean tenantDbEntityManager() {
		final LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
		emfBean.setDataSource(tenantDbDataSource());
		emfBean.setPackagesToScan(Tenant.class.getPackage().getName());
		emfBean.setJpaVendorAdapter(tenantDbJpaVendorAdapter());

		emfBean.afterPropertiesSet();
		return emfBean;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean smsTenantDbEntityManager() {
		final LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
		emfBean.setDataSource(tenantDbDataSource());
		emfBean.setPackagesToScan(SmsTenant.class.getPackage().getName());
		emfBean.setJpaVendorAdapter(tenantDbJpaVendorAdapter());

		emfBean.afterPropertiesSet();
		return emfBean;
	}
}
