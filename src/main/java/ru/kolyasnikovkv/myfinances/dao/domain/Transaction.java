package ru.kolyasnikovkv.myfinances.dao.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Transaction {
    private int id;
    private int accountID;
    private BigDecimal sum;
    private String  date;
    private int categorieID;
}
