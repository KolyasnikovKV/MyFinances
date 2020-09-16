package ru.kolyasnikovkv.myfinances.service;

import ru.kolyasnikovkv.myfinances.dao.CurrencyDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Currency;
import ru.kolyasnikovkv.myfinances.service.converters.CurrencyConverter;
import ru.kolyasnikovkv.myfinances.service.dto.CurrencyDto;

import java.sql.Connection;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao;
    private final CurrencyConverter currencyConverter;

    public CurrencyService(CurrencyDao currencyDao, CurrencyConverter currencyConverter){
            this.currencyDao = currencyDao;
            this.currencyConverter = currencyConverter;
        }

    public CurrencyDto createNewCurrency(CurrencyDto currencyDto, Connection connection) {

        if ( currencyDto != null && (! currencyDto.getNameCurrency().isEmpty()) && (checkNameOfCurrency(currencyDto, connection))) {//Если валюта
            //уже существует она не будет создана повторно

            Currency currency = currencyConverter.currencyDtoToCurrency(currencyDto);
            currency = currencyDao.insert(currency, connection);

            if (currency != null) {
                currencyDto = currencyConverter.currencyToCurrencyDto(currency);
                return currencyDto;
            }
        }
        return null;
    }

    public List<CurrencyDto> getAllCurrency() {

        return currencyConverter.listCurrencyToListCurrencyDto(currencyDao.findAll());
    }

    public boolean checkNameOfCurrency(CurrencyDto currencyDto, Connection connection) {
        Currency currency = currencyConverter.currencyDtoToCurrency(currencyDto);
        currency = currencyDao.findByName(currency.getName(), connection);

        if (currency == null) {
            return true;
        }

        return false;
    }

    public CurrencyDto updateCurrency(CurrencyDto currencyDto, Connection connection) {

           Currency updateCurrency = currencyDao.findById(currencyDto.getId(), connection);
            if (updateCurrency != null &&  (checkNameOfCurrency(currencyDto, connection))) {

                updateCurrency = currencyConverter.currencyDtoToCurrency(currencyDto);
                updateCurrency =  currencyDao.update(updateCurrency, connection);

                return currencyConverter.currencyToCurrencyDto(updateCurrency);
            }
            return null;
    }

    public void deleteCurrency(CurrencyDto currencyDto, Connection connection) {

        currencyDao.delete(currencyDto.getId(), connection);
    }
}
