package ru.kolyasnikovkv.myfinances.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {
    private Long id = -1L;
    private  int accountID;
    private BigDecimal sum;
    private String date;
    private int categorieID;

}
