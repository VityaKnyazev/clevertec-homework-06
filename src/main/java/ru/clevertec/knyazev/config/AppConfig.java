package ru.clevertec.knyazev.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.cache.AbstractCacheFactory;
import ru.clevertec.knyazev.cache.impl.DefaultCacheFactory;
import ru.clevertec.knyazev.util.YAMLParser;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = { "ru.clevertec.knyazev.dao.impl" })
public class AppConfig {

    private static final String PROPERTY_FILE = "application.yaml";

    @Bean
    YAMLParser yamlParser() {
        return new YAMLParser(PROPERTY_FILE);
    }

    @Bean
    AbstractCacheFactory defaultCacheFactory() {
        return new DefaultCacheFactory(yamlParser());
    }

    @Bean
    DataSource hikariDataSource() {
        YAMLParser yamlParser = yamlParser();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(yamlParser.getProperty("datasource", "driverClassName"));
        hikariConfig.setJdbcUrl(yamlParser.getProperty("datasource", "jdbcUrl"));
        hikariConfig.setUsername(yamlParser.getProperty("datasource", "username"));
        hikariConfig.setPassword(yamlParser.getProperty("datasource", "password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(yamlParser.getProperty("datasource", "maxPoolSize")));
        hikariConfig.setConnectionTimeout(Long.parseLong(yamlParser.getProperty("datasource", "connectionTimeout")));

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }
}
