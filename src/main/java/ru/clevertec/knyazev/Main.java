package ru.clevertec.knyazev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.config.AppConfig;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.entity.Person;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        PersonDAO personDAOImpl = context.getBean("personDAOImpl", PersonDAO.class);
        personDAOImpl.save(Person.builder()
                .name("Vano")
                .surname("Patsuk")
                .email("Vano@mail.ru")
                .citizenship("Belarus")
                .age(12)
                .build());

        System.out.printf("Hello %s%n", "spring context!");
    }

}
