package ru.clevertec.knyazev;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.config.AppConfig;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.service.PersonService;
import ru.clevertec.knyazev.service.exception.ServiceException;

import java.util.UUID;

@Slf4j
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        log.info("Spring context started");

        PersonService personServiceImpl = context.getBean("personServiceImpl", PersonService.class);

        PersonDTO personDTO = PersonDTO.builder()
                .name("Kolya")
                .surname("Petrov")
                .email("petyaI@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        try {
            PersonDTO savedPersonDTO = personServiceImpl.add(personDTO);

            System.out.printf("Person DTO from cache:%n%s", personServiceImpl.get(savedPersonDTO.id()).toXML());

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
        } catch (DAOException | ServiceException e) {
            log.error(e.getMessage(), e);
        }
    }

}
