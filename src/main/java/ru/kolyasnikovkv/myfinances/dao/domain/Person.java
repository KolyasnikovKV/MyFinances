package ru.kolyasnikovkv.myfinances.dao.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Person extends AbstractEntity{
    private String email;
    private String password;
    private String nick;
    private String fullName;
    private List<Account> listAccount; //Список счетов!
}
