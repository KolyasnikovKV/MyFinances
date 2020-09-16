package service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.DigestService;
import ru.kolyasnikovkv.myfinances.service.SecurityService;
import ru.kolyasnikovkv.myfinances.service.converters.UserConverter;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.sql.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceTest {

   @InjectMocks
   SecurityService subj;
    @Mock
    PersonDao personDao;
    @Mock
    DigestService digestService;
    @Mock
    UserConverter userConverter;
    @Mock Connection connectionMock;


    @Test
    public void authorize_userNotFoundByEmail() {

        when(personDao.findByMail("popov@mail.ru", connectionMock)).thenReturn(null);
        UserDto userDto = subj.authorize("popov@mail.ru", "pass", connectionMock);

        assertNull(userDto);
    }

    @Test
    public void authorize_passwordWrong() {
        Person person = new Person();
        person.setPassword("769d372e15ea74d8b644fe7e5ae62b79");

        when(personDao.findByMail("popov@mail.ru", connectionMock)).thenReturn(person);
        when(digestService.hash("password")).thenReturn("777777777777");

        UserDto userDto = subj.authorize("popov@mail.ru", "password", connectionMock);

        assertNull(userDto);
    }

    @Test
    public void authorize_OK() {
        Person person = new Person();
        person.setId(100L);
        person.setPassword("some password");


        UserDto userDto = new UserDto();
        userDto.setId(100L);

        when(personDao.findByMail("popov@mail.ru", connectionMock)).thenReturn(person);
        when(digestService.hash("password")).thenReturn("some password");
        when(userConverter.personToUserDto(person)).thenReturn(userDto);


        UserDto userDtoFromService = subj.authorize("popov@mail.ru", "password", connectionMock);
        assertEquals(userDto, userDtoFromService);

    }
}