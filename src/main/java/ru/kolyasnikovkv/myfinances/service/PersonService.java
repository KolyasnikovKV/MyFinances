package ru.kolyasnikovkv.myfinances.service;

import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.converters.UserConverter;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonService {
    private final PersonDao personDao;
    private final UserConverter userConverter;
    private final SecurityService securityService;

    public PersonService(PersonDao personDao, UserConverter userConverter) {
        this.personDao = personDao;
        this.userConverter = userConverter;
        securityService = ServiceFactory.getSecurityService();
    }

    private List<UserDto> getAllUserDto() {

        List<Person> person = personDao.findAll();
        List<UserDto> listPersonDto = new ArrayList<>();

        for (Person p : person) {
            listPersonDto.add(userConverter.personToUserDto(p));
        }

        return  listPersonDto;
    }

    public boolean checkMail(UserDto personDto, Connection connection) {

         String mail = personDto.getMail();

        if (mail.length() > 60) {
            return false;
        }

        Pattern p = Pattern.compile("[a-zA-Z0-9\\-_]+@[a-zA-Z]+\\.[a-zA-Z]{2,4}");
        Matcher m = p.matcher(mail);

        if (m.matches()) {

            Person person = userConverter.userDtoToPerson(personDto);
            person = personDao.findByMail(person.getEmail(), connection);


            if (person == null) {
                return true;
            }

        }
        return false;
    }

   public UserDto authorize(UserDto personDto, Connection connection) {
        return securityService.authorize(personDto.getMail(), personDto.getPassword(), connection);
    }

    public UserDto findNickNameAndPassword(UserDto personDto, Connection connection) {

        if (personDto == null) {
            return null;
        }

        //Person person = userConverter.userDtoToPerson(personDto);
        Person person = personDao.findByNickAndPassword(personDto.getNick(), personDto.getPassword(), connection);
        return userConverter.personToUserDto(person);
    }

    public UserDto createNewPerson(UserDto personDto, Connection connection) {

        if (checkMail(personDto, connection)) {

            Person person = userConverter.userDtoToPerson(personDto);
            return  userConverter.personToUserDto(personDao.insert(person, connection));
        }
        return null;
    }

    public void deletePerson(UserDto personDto, Connection connection) {

        Person person = userConverter.userDtoToPerson(personDto);

        if (person != null) {
            personDao.delete(person.getId(), connection);
        }

    }

    public UserDto updatePerson(UserDto personDto, Connection connection) {

        Person person = userConverter.userDtoToPerson(personDto);
        person = personDao.findById(person.getId(), connection);

        if (person == null) {
            return null;
        }

         return  userConverter.personToUserDto(personDao.update(person, connection));
    }
}
