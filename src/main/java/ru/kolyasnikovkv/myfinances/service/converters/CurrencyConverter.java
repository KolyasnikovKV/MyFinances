package ru.kolyasnikovkv.myfinances.service.converters;

import ru.kolyasnikovkv.myfinances.dao.domain.Currency;
import ru.kolyasnikovkv.myfinances.service.dto.CurrencyDto;

import java.util.ArrayList;
import java.util.List;

public class CurrencyConverter {

    public Currency currencyDtoToCurrency(CurrencyDto currencyDto) {

        if (currencyDto != null) {
            Currency currency = new Currency();
            currency.setId(currencyDto.getId());
            currency.setName(currencyDto.getNameCurrency());
            return currency;
        }
        return null;
    }

    public CurrencyDto currencyToCurrencyDto(Currency currency) {

        if (currency != null) {
            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setId(currency.getId());
            currencyDto.setNameCurrency(currency.getName());
            return currencyDto;
        }
        return null;
    }

    public List<CurrencyDto> listCurrencyToListCurrencyDto(List<Currency> currency) {

        if (currency == null) {
            return null;
        }

        List<CurrencyDto> listCurrencyDto = new ArrayList<>();
        CurrencyConverter converter = new CurrencyConverter();

        for (Currency c : currency) {
            listCurrencyDto.add(converter.currencyToCurrencyDto(c));
        }

        return listCurrencyDto;
    }




}
