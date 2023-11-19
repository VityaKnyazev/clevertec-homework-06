package ru.clevertec.knyazev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.config.AppConfig;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.entity.Person;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        PersonDAO personDAOImpl = context.getBean("personDAOImpl", PersonDAO.class);
        Person savedPerson = personDAOImpl.save(Person.builder()
                .name("Vano")
                .surname("Patsuk")
                .email("Vano@mail.ru")
                .citizenship("Belarus")
                .age(12)
                .build());

        savedPerson.setAge(14);
        Person updatedPerson = personDAOImpl.update(savedPerson);

        System.out.printf("Hello %s%n", "spring context!");
    }

}
