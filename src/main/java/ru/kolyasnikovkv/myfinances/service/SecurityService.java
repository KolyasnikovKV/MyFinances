package ru.kolyasnikovkv.myfinances.service;

import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.converters.UserConverter;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.sql.Connection;

public class SecurityService {
    private final PersonDao personDao;
    private final DigestService digestService;
    private final UserConverter userConverter;

    public SecurityService(PersonDao  personDao, DigestService digestService, UserConverter userConverter) {
        this.personDao = personDao;
        this.digestService = digestService;
        this.userConverter = userConverter;
    }

    public UserDto authorize(String email, String password, Connection connection) {

        Person person = personDao.findByMail(email, connection);
        if (person != null) {
            String passwordHash = digestService.hash(password);
            if (passwordHash.equals(person.getPassword())) {
                return userConverter.personToUserDto(person);
            }
        }
        return null;
    }

}
