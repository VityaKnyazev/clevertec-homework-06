package ru.clevertec.knyazev.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.clevertec.knyazev.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PersonDAOImplTest {

    @Mock
    JdbcTemplate jdbcTemplateMock;

    @InjectMocks
    PersonDAOImpl personDAOImpl;

    @Test
    public void checkFindByIdShouldReturnOptionalPerson() {
        Person expectedPerson = Person.builder()
                .id(new UUID(24L, 18L))
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.any()))
                .thenReturn(expectedPerson);

        UUID inputUUID = new UUID(24L, 18L);
        Optional<Person> actualPerson = personDAOImpl.findById(inputUUID);

        assertThat(actualPerson).isNotNull()
                .isNotEmpty()
                .containsSame(expectedPerson);
    }

    @Test
    public void checkFindByIdShouldReturnOptionalEmpty() {

        Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.any()))
                .thenThrow(new DataAccessException("Id not exists") {
                });

        UUID inputUUID = new UUID(15L, 12L);
        Optional<Person> actualPerson = personDAOImpl.findById(inputUUID);

        assertThat(actualPerson).isEmpty();
    }

    @Test
    public void checkFindAllShouldReturnPersons() {

        Person expectedPerson1 = Person.builder()
                .id(new UUID(24L, 18L))
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();
        Person expectedPerson2 = Person.builder()
                .id(new UUID(14L, 128L))
                .name("Masha")
                .surname("Ivanova")
                .email("masha@mail.ru")
                .citizenship("France")
                .age(25)
                .build();

        List<Person> expectedPersons = List.of(expectedPerson1, expectedPerson2);

        Mockito.when(jdbcTemplateMock.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
                .thenReturn(expectedPersons);

        List<Person> actualPersons = personDAOImpl.findAll();

        assertThat(actualPersons).isNotEmpty()
                .isNotNull()
                .containsExactly(expectedPerson1, expectedPerson2);
    }

    @Test
    public void checkFindAllShouldReturnEmptyList() {

        Mockito.when(jdbcTemplateMock.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
                .thenThrow(new DataAccessException("empty") {
                });

        List<Person> actualPersons = personDAOImpl.findAll();

        assertThat(actualPersons).isEmpty();
    }




}
