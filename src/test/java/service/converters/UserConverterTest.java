package service.converters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.converters.UserConverter;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserConverterTest {


    @Mock
    UserConverter userConverter;

    @Before
    public void startTest() {

    }

    @Test
    public void userDtoToPersonConvert_ok() {

        UserDto userDto = new UserDto();
        Person person = new Person();

        when(userConverter.userDtoToPerson(userDto)).thenReturn(person);

        Person personFromService = userConverter.userDtoToPerson(userDto);
        assertEquals(person, personFromService);
    }

    @Test
    public void personToUserDtoConvert() {
        UserDto userDto = new UserDto();
        Person person = new Person();

        when(userConverter.personToUserDto(person)).thenReturn(userDto);

        UserDto userDtoFromService = userConverter.personToUserDto(person);
        assertEquals(userDto, userDtoFromService);
    }

}