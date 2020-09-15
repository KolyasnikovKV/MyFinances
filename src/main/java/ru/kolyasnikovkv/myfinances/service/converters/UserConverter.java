package ru.kolyasnikovkv.myfinances.service.converters;


import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    public Person userDtoToPersonConvert(UserDto userDto) {
        if (userDto != null) {

            Person person = new Person();

            person.setMail(userDto.getMail());
            person.setPassword(userDto.getPassword());
            person.setFullName(userDto.getFullName());
            person.setNick(userDto.getNick());
            person.setId(userDto.getId());

            return person;
        }

        return null;
    }

    public UserDto  personToUserDtoConvert(Person person) {

       if (person != null) {

           UserDto user = new UserDto();
           user.setId(person.getId());
           user.setMail(person.getMail());
           user.setPassword(person.getPassword());
           user.setNick(person.getNick());
           user.setFullName(person.getFullName());
           return user;
       }

        return null;
    }

    public List<UserDto> listPersonToListUserDtoConvert(List<Person> person) {

        if (person == null) {
            return null;
        }

        List<UserDto> listUserDto = new ArrayList<>();
        UserConverter converter = new UserConverter();

        for (Person pers : person) {

            listUserDto.add(converter.personToUserDtoConvert(pers));
        }

        return listUserDto;
    }



}
