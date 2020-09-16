package ru.kolyasnikovkv.myfinances.service.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountDto {
    private Long id = -1L;
    private int numberAccount;
    private Long personId;
    private BigDecimal balance;
    private Long currencyId;
    private String description;

    public String toString() {
        return  "id = " + id + "; number account = " + numberAccount + "; person ID = " + personId + "; balance = " + balance +
                "; currency = " + currencyId + "; description = " + description + ";";
    }
}
