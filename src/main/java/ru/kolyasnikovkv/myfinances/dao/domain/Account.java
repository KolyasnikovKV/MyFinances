package ru.kolyasnikovkv.myfinances.dao.domain;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account {
    private Long id;
    private int numberAccount;
    private int person;
    private BigDecimal balance;
    private int currency;
    private String description;
}
