package ru.kolyasnikovkv.myfinances.service;

import ru.titov.s05.dao.PersonDao;
import ru.titov.s05.dao.domain.Person;
import ru.titov.s05.service.converters.UserConverter;
import ru.titov.s05.service.dto.UserDto;

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

            listPersonDto.add(userConverter.personToUserDtoConvert(p));
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

            Person person = userConverter.userDtoToPersonConvert(personDto);
            person = personDao.findByMail(person.getMail(), connection);


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

        Person person = userConverter.userDtoToPersonConvert(personDto);
        person = personDao.findByNickAndPassword(person, connection);

        return userConverter.personToUserDtoConvert(person);
    }

    public UserDto createNewPerson(UserDto personDto, Connection connection) {

        if (checkMail(personDto, connection)) {

            Person person = userConverter.userDtoToPersonConvert(personDto);
            return  userConverter.personToUserDtoConvert(personDao.insert(person, connection));
        }

        return null;
    }

    public boolean deletePerson(UserDto personDto, Connection connection) {

        Person person = userConverter.userDtoToPersonConvert(personDto);

        if (person != null) {
            return personDao.delete(person.getId(), connection);
        }
        return false;
    }

    public UserDto updatePerson(UserDto personDto, Connection connection) {

        Person person = userConverter.userDtoToPersonConvert(personDto);
        person = personDao.findById(person.getId(), connection);

        if (person == null) {
            return null;
        }

         return  userConverter.personToUserDtoConvert(personDao.update(person, connection));
    }
}
