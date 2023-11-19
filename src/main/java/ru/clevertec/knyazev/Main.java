package ru.clevertec.knyazev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.config.AppConfig;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.service.PersonService;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        PersonService personServiceImpl = context.getBean("personServiceImpl", PersonService.class);

        PersonDTO personDTO = PersonDTO.builder()
                .name("Kolya")
                .surname("Petrov")
                .email("petyaI@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        PersonDTO savedPersonDTO = personServiceImpl.add(personDTO);

        System.out.println(savedPersonDTO.toXML());

        PersonDTO updatingPersonDTO = PersonDTO.builder()
                .id(savedPersonDTO.id())
                .name(savedPersonDTO.name())
                .surname("Sidorov")
                .email(savedPersonDTO.email())
                .citizenship(savedPersonDTO.citizenship())
                .age(savedPersonDTO.age())
                .build();

        personServiceImpl.update(updatingPersonDTO);

        personServiceImpl.remove(updatingPersonDTO.id());
    }

}
