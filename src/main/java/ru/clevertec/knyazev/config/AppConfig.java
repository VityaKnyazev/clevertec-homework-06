package ru.clevertec.knyazev.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.cache.AbstractCacheFactory;
import ru.clevertec.knyazev.cache.Cache;
import ru.clevertec.knyazev.cache.impl.DefaultCacheFactory;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.util.YAMLParser;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@ComponentScan(basePackages = {"ru.clevertec.knyazev.dao.impl",
        "ru.clevertec.knyazev.dao.proxy",
        "ru.clevertec.knyazev.mapper",
        "ru.clevertec.knyazev.service.impl"})
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
    Cache<UUID, Person> personCache() {
        return defaultCacheFactory().initCache();
    }

    @Bean
    ValidatorFactory validatorFactory() {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
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
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }
}
