package ru.kolyasnikovkv.myfinances.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {
    private Long id = -1L;
    private Long accountIdFrom;
    private Long accountIdTo;
    private BigDecimal ammount;
    private String date;
    private Long categoryId;

}
