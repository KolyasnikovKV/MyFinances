package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.CurrencyDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Currency;
import ru.kolyasnikovkv.myfinances.service.CurrencyService;
import ru.kolyasnikovkv.myfinances.service.converters.CurrencyConverter;
import ru.kolyasnikovkv.myfinances.service.dto.CurrencyDto;


import java.sql.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceTest {

    @InjectMocks
    CurrencyService subj;
    @Mock
    CurrencyDao currencyDao;
    @Mock
    CurrencyConverter currencyConverter;
    @Mock Connection connectionMock;

    @Test
    public void checkNameOfCurrency_returnTrue() {
        Currency currency = new Currency();
        CurrencyDto currencyDto = new CurrencyDto();


        when(currencyConverter.currencyDtoToCurrency(currencyDto)).thenReturn(currency);
        when(currencyDao.findByName(currencyDto.getNameCurrency(), connectionMock)).thenReturn(null);


        Boolean trueFromSubj = subj.checkNameOfCurrency(currencyDto, connectionMock);


        assertTrue(trueFromSubj);
    }

    @Test
    public void  checkNameOfCurrency_currencyIsNull() {
        Currency currency = new Currency();
        CurrencyDto currencyDto = new CurrencyDto();

        when(currencyConverter.currencyDtoToCurrency(currencyDto)).thenReturn(currency);
        when(currencyDao.findByName(currencyDto.getNameCurrency(), connectionMock)).thenReturn(null);

        Currency currencyFromDao  = currencyDao.findByName(currencyDto.getNameCurrency(), connectionMock);

        assertNull(currencyFromDao);
    }

    @Test
    public void updateCurrency_ok() {
        Currency currency = new Currency();
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(15L);

        when(currencyConverter.currencyDtoToCurrency(currencyDto)).thenReturn(currency);
        when(currencyDao.findById(15L, connectionMock)).thenReturn(currency);
        when(currencyDao.update(currency, connectionMock)).thenReturn(currency);
        when(currencyConverter.currencyToCurrencyDto(currency)).thenReturn(currencyDto);

        CurrencyDto currencyDtoFromService = subj.updateCurrency(currencyDto, connectionMock);
        assertEquals(currencyDto, currencyDtoFromService);
    }

    @Test
    public void updateCurrency_idIsWrong() {
        Currency currency = new Currency();
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(15L);

        when(currencyConverter.currencyDtoToCurrency(currencyDto)).thenReturn(currency);
        when(currencyDao.findById(25L, connectionMock)).thenReturn(currency);
        when(currencyDao.update(currency, connectionMock)).thenReturn(currency);
        when(currencyConverter.currencyToCurrencyDto(currency)).thenReturn(currencyDto);

        CurrencyDto currencyDtoFromService = subj.updateCurrency(currencyDto, connectionMock);
        assertNull(currencyDtoFromService);
    }

/*
    @Test
    public void deleteCurrency_ok() {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(120L);

        when(currencyDao.delete(120L, connectionMock)).thenReturn(true);
        Boolean deleteCurrency = subj.deleteCurrency(currencyDto, connectionMock);

        assertTrue(deleteCurrency);
    }

    @Test
    public void deleteCurrency_idIsWrong() {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(120);

        when(currencyDao.delete(121, connectionMock)).thenReturn(true);
        Boolean deleteCurrency = subj.deleteCurrency(currencyDto, connectionMock);

        assertFalse(deleteCurrency);
    }

*/
    @Test
    public void createNewCurrency_currencyIsNull() {

        CurrencyDto currencyDto = null;

        CurrencyDto currencyDtoFromService = subj.createNewCurrency(currencyDto, connectionMock);

        assertNull(currencyDtoFromService);
    }

    @Test
    public void createNewCurrency_ok() {

        Currency currency = new Currency();
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setNameCurrency("Валютный");

        when(currencyConverter.currencyDtoToCurrency(currencyDto)).thenReturn(currency);
        when(currencyDao.findByName(currencyDto.getNameCurrency(), connectionMock)).thenReturn(null);
        when(currencyDao.insert(currency, connectionMock)).thenReturn(currency);
        when(currencyConverter.currencyToCurrencyDto(currency)).thenReturn(currencyDto);

        CurrencyDto currencyDtoFromService = subj.createNewCurrency(currencyDto, connectionMock);
        assertEquals(currencyDto, currencyDtoFromService);
    }
}
