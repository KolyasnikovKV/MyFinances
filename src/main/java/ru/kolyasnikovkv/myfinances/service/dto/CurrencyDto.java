package ru.kolyasnikovkv.myfinances.service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDto {
    private Long id = -1L;
    private String nameCurrency;
}
