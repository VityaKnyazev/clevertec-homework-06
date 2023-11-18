package ru.clevertec.knyazev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private UUID id;

    private String name;

    private String surname;

    private String email;

    private String citizenship;

    private Integer age;
}
