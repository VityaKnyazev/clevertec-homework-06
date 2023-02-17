package ru.clevertec.knyazev.dao.connection;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource(value = { "classpath:applicationClevertec.properties", "classpath:jpa.properties" })
public class AppConnectionConfig {
	@Autowired
	Environment environment;
	
	@Bean
	DataSource hikariDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(environment.getProperty("db.driverClassName"));
		hikariConfig.setJdbcUrl(environment.getProperty("db.jdbcUrl"));
		hikariConfig.setUsername(environment.getProperty("db.username"));
		hikariConfig.setPassword(environment.getProperty("db.password"));
		hikariConfig.setMaximumPoolSize(Integer.valueOf(environment.getProperty("db.maxPoolSize")));
		hikariConfig.setConnectionTimeout(Long.valueOf(environment.getProperty("db.connectionTimeout")));
		
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}
	
	@Bean
	JpaVendorAdapter hibernateJpaVendorAdapter() {
		JpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		return hibernateJpaVendorAdapter;
	}
	
	@Bean
	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource hikariDataSource, JpaVendorAdapter hibernateJpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setDataSource(hikariDataSource);
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
		localContainerEntityManagerFactoryBean.setPackagesToScan("ru.clevertec.knyazev.entity");
		
		final HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("hibernate.show.sql", Boolean.valueOf(environment.getProperty("hibernate.show.sql")));
		properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
		
		return localContainerEntityManagerFactoryBean;		
	}
	
	@Bean
	TransactionManager jpaTransactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
		TransactionManager jpaTransactionManager = new JpaTransactionManager(
				localContainerEntityManagerFactoryBean.getObject());

		return jpaTransactionManager;
	}
}