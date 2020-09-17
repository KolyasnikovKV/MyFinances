package ru.kolyasnikovkv.myfinances.dao.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Transaction extends AbstractEntity {
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal ammount;
    private String  date;
    private Long category;
}
