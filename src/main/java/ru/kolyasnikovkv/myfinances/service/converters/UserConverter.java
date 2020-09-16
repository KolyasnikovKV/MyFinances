package ru.kolyasnikovkv.myfinances.service.converters;

import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    public Person userDtoToPerson(UserDto userDto) {
        if (userDto != null) {
            Person person = new Person();
            person.setEmail(userDto.getMail());
            person.setPassword(userDto.getPassword());
            person.setFullName(userDto.getFullName());
            person.setNick(userDto.getNick());
            person.setId(userDto.getId());
            return person;
        }
        return null;
    }

    public UserDto  personToUserDto(Person person) {

       if (person != null) {
           UserDto user = new UserDto();
           user.setId(person.getId());
           user.setMail(person.getEmail());
           user.setPassword(person.getPassword());
           user.setNick(person.getNick());
           user.setFullName(person.getFullName());
           return user;
       }
        return null;
    }

    public List<UserDto> listPersonToListUserDto(List<Person> person) {

        if (person == null) {
            return null;
        }

        List<UserDto> listUserDto = new ArrayList<>();
        UserConverter converter = new UserConverter();

        for (Person pers : person) {
            listUserDto.add(converter.personToUserDto(pers));
        }

        return listUserDto;
    }



}
