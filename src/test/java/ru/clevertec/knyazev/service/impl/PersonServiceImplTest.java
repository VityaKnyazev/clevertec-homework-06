package ru.clevertec.knyazev.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Condition;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.mapper.PersonMapper;
import ru.clevertec.knyazev.mapper.PersonMapperImpl;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {
    @Mock
    private PersonDAO personDAOImplMock;

    @Spy
    private PersonMapper personMapperImplSpy = new PersonMapperImpl();

    @Spy
    private ValidatorFactory validatorFactorySpy = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    @Test
    public void checkGetShouldReturnPersonDTO() {
        Person expectedPerson = Person.builder()
                .id(new UUID(251L, 16L))
                .name("Misha")
                .surname("Nikolaev")
                .email("misha@mail.ru")
                .citizenship("Belarus")
                .age(48)
                .build();


        Mockito.when(personDAOImplMock.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(expectedPerson));

        UUID inputUUID = new UUID(251L, 16L);
        PersonDTO actualPersonDTO = personServiceImpl.get(inputUUID);

        assertThat(actualPersonDTO).isNotNull()
                .has(new Condition<PersonDTO>(p -> p.id().equals(inputUUID), "check id"))
                .has(new Condition<PersonDTO>(p -> p.name().equals(expectedPerson.getName()), "check name"))
                .has(new Condition<PersonDTO>(p ->
                        p.surname().equals(expectedPerson.getSurname()), "check surname"))
                .has(new Condition<PersonDTO>(p -> p.email().equals(expectedPerson.getEmail()), "check email"))
                .has(new Condition<PersonDTO>(p ->
                        p.citizenship().equals(expectedPerson.getCitizenship()), "check citizenship"))
                .has(new Condition<PersonDTO>(p -> p.age().equals(expectedPerson.getAge()), "check age"));
    }
}
