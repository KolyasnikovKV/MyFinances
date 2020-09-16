package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.PersonService;
import ru.kolyasnikovkv.myfinances.service.converters.UserConverter;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.sql.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @InjectMocks
    PersonService subj;
    @Mock
    UserConverter userConverter;
    @Mock
    PersonDao personDao;
    @Mock Connection connectionMock;

    @Test
    public void checkEmail_ok() {
        UserDto userDto = new UserDto();
        userDto.setMail("ui@mail.ru");

        Person person = new Person();
        person.setEmail("ui@mail.ru");

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.findByMail("ui@mail.ru", connectionMock)).thenReturn(person);

        Boolean mailFromService = subj.checkMail(userDto, connectionMock);
        assertTrue(mailFromService);
    }

    @Test
    public void checkEmail_badEmail() {
        UserDto userDto = new UserDto();
        userDto.setMail("uimail.ru");

        Person person = new Person();
        person.setEmail("ui@mail.ru");

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.findByMail("ui@mail.ru", connectionMock)).thenReturn(person);

        Boolean mailFromService = subj.checkMail(userDto, connectionMock);
        assertFalse(mailFromService);
    }

    @Test
    public void findNickNameAndPassword_ok() {
        UserDto userDto = new UserDto();
        Person person = new Person();

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.findByNickAndPassword(userDto.getNick(), userDto.getPassword(), connectionMock)).thenReturn(person);
        when(userConverter.personToUserDto(person)).thenReturn(userDto);

        UserDto userFromService = subj.findNickNameAndPassword(userDto, connectionMock);

        assertEquals(userDto, userFromService);

    }

    @Test
    public void createNewPerson_ok() {
        UserDto userDto = new UserDto();
        userDto.setMail("bf@vr.ru");
        Person person = new Person();


        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.insert(person, connectionMock)).thenReturn(person);
        when(personDao.findByMail("bf@vr.ru", connectionMock)).thenReturn(null);
        when(userConverter.personToUserDto(person)).thenReturn(userDto);

        UserDto userFromService = subj.createNewPerson(userDto, connectionMock);
        assertEquals(userDto, userFromService);
    }

    @Test
    public void createNewPerson_badEmail() {
        UserDto userDto = new UserDto();
        userDto.setMail("bfvr.ru");
        Person person = new Person();


        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.insert(person, connectionMock)).thenReturn(person);
        when(userConverter.personToUserDto(person)).thenReturn(userDto);

        UserDto userFromService = subj.createNewPerson(userDto, connectionMock);
        assertNotEquals(userDto, userFromService);
    }
/*
    @Test
    public void deletePerson_ok() {
        UserDto userDto = new UserDto();
        Person person = new Person();
        person.setId(89);

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.delete(89, connectionMock)).thenReturn(true);

        Boolean deletePerson = subj.deletePerson(userDto, connectionMock);

        assertTrue(deletePerson);
    }

    @Test
    public void deletePerson_personIdWrong() {
        UserDto userDto = new UserDto();
        Person person = new Person();
        person.setId(89);

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.delete(91, connectionMock)).thenReturn(true);

        Boolean deletePerson = subj.deletePerson(userDto, connectionMock);

        assertFalse(deletePerson);
    }
*/
    @Test
    public void updatePerson_ok() {

        UserDto userDto = new UserDto();
        Person person = new Person();
        person.setId(89L);

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.findById(89L, connectionMock)).thenReturn(person);
        when(personDao.update(person, connectionMock)).thenReturn(person);
        when(userConverter.personToUserDto(person)).thenReturn(userDto);

        UserDto personFromService = subj.updatePerson(userDto, connectionMock);

        assertEquals(userDto, personFromService);
    }

    @Test
    public void updatePerson_personIdWrong() {

        UserDto userDto = new UserDto();
        Person person = new Person();
        person.setId(89L);

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);
        when(personDao.findById(9L, connectionMock)).thenReturn(person);
        when(personDao.update(person, connectionMock)).thenReturn(person);
        when(userConverter.personToUserDto(person)).thenReturn(userDto);

        UserDto personFromService = subj.updatePerson(userDto, connectionMock);

        assertNotEquals(userDto, personFromService);
    }
}