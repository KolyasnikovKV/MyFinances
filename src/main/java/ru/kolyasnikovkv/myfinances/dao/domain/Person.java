package ru.kolyasnikovkv.myfinances.dao.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Person {
    private int id;
    private String mail;
    private String password;
    private String nick;
    private String fullName;
    private List<Account> listAccount; //Список счетов!
}
