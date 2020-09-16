package ru.kolyasnikovkv.myfinances.dao.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Transaction {
    private int id;
    private int accountFrom;
    private int accountTo;
    private BigDecimal ammount;
    private String  date;
    private int category;
}
