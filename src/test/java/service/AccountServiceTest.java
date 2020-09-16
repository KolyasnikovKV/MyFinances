package service;

import javafx.beans.binding.When;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.AccountDao;
import ru.kolyasnikovkv.myfinances.dao.CurrencyDao;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Account;
import ru.kolyasnikovkv.myfinances.dao.domain.Currency;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.AccountService;
import ru.kolyasnikovkv.myfinances.service.converters.AccountConverter;
import ru.kolyasnikovkv.myfinances.service.dto.AccountDto;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    AccountService subj;
    @Mock
    AccountDao accountDao;
    @Mock
    AccountConverter accountConverter;
    @Mock AccountService accountService;
    @Mock
    CurrencyDao currencyDao;
    @Mock
    PersonDao personDao;
    @Mock Connection connectionMock;



    @Test
    public void createNewAccount_ok() {
        Account account = new Account();
        account.setId(3L);
        account.setPerson(13L);

        Person person = new Person();
        person.setId(13L);
        List<Account> list = new ArrayList<>();

        AccountDto accountDto = new AccountDto();
        accountDto.setPersonId(13L);
        accountDto.setId(3L);
        accountDto.setCurrencyId(123L);
        accountDto.setNumberAccount(9L);
        Currency currency = new Currency();


        when(accountConverter.accountDtoToAccount(accountDto)).thenReturn(account);
        when(accountConverter.accountToAccountDto(account)).thenReturn(accountDto);
        when(personDao.findById(13L, connectionMock)).thenReturn(person);
        when(accountDao.findByNumberAccount(9L, connectionMock)).thenReturn(list);
        when(accountDao.countAccountPerson(13L, connectionMock)).thenReturn(0);
        when(accountService.checkCurrencyId(accountDto, connectionMock)).thenReturn(true);
        when(accountService.checkNumberAccount(accountDto, connectionMock)).thenReturn(true);
        when(currencyDao.findById(123L, connectionMock)).thenReturn(currency);


        when(accountDao.insert(account, connectionMock)).thenReturn(account);


        AccountDto accountDtoFromService = subj.createNewAccount(accountDto, connectionMock);
        assertEquals(accountDto, accountDtoFromService);
    }

    @Test
    public void findByPesonId_ok() {
        UserDto userDto = new UserDto();
        userDto.setId(12L);
        Account account = new Account();
        AccountDto accountDto = new AccountDto();
        List<AccountDto> list = new ArrayList<>();
        list.add(accountDto);
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);



        when(accountDao.findByPerson(12L, connectionMock)).thenReturn(accountList);
        when(accountConverter.listAccountToListAccountDto(accountList)).thenReturn(list);

        List<AccountDto>  listFromService = subj.findByPesonId(userDto, connectionMock);
        assertEquals(list, listFromService);
    }

    @Test
    public void findByPesonId_idWrong() {
        UserDto userDto = new UserDto();
        userDto.setId(12L);
        Account account = new Account();
        AccountDto accountDto = new AccountDto();
        List<AccountDto> list = new ArrayList<>();
        list.add(accountDto);
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);



        when(accountDao.findByPerson(120L, connectionMock)).thenReturn(accountList);
        when(accountConverter.listAccountToListAccountDto(accountList)).thenReturn(list);

        List<AccountDto>  listFromService = subj.findByPesonId(userDto, connectionMock);
        assertNotEquals(list, listFromService);
    }


    @Test
    public void updateAccount_ok() {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(45L);
        Account account = new Account();
        account.setId(3L);
        when(accountConverter.accountDtoToAccount(accountDto)).thenReturn(account);
        when(accountDao.findById(3L, connectionMock)).thenReturn(account);
        when(accountDao.update(account, connectionMock)).thenReturn(account);
        when(accountConverter.accountToAccountDto(account)).thenReturn(accountDto);

        AccountDto deleteAccount = subj.updateAccount(accountDto, connectionMock);

        assertEquals(accountDto, deleteAccount);

    }

    @Test
    public void updateAccount_accountIdWrong() {
        AccountDto accountDto = new AccountDto();

        Account account = new Account();
        account.setId(30L);

        when(accountConverter.accountDtoToAccount(accountDto)).thenReturn(account);
        when(accountDao.findById(3L, connectionMock)).thenReturn(account);
        when(accountDao.update(account, connectionMock)).thenReturn(account);
        when(accountConverter.accountToAccountDto(account)).thenReturn(accountDto);


        AccountDto deleteAccount = subj.updateAccount(accountDto, connectionMock);

        assertNotEquals(accountDto, deleteAccount);

    }

    /*@Test
    public void deleteAccount_ok() {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(23L);

        when(accountDao.delete(23L, connectionMock)).thenReturn(true);

        Boolean deleteAccount = subj.deleteAccount(accountDto, connectionMock);

        assertTrue(deleteAccount);
    }

    @Test
    public void deleteAccount_accountDtoIdWrong() {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(23);

        when(accountDao.delete(230, connectionMock)).thenReturn(true);

        Boolean deleteAccount = subj.deleteAccount(accountDto, connectionMock);

        assertFalse(deleteAccount);
    }
*/
    @Test
    public void checkCurrencyId_ok() {
        AccountDto accountDto = new AccountDto();
        accountDto.setCurrencyId(102L);
        Currency currency = new Currency();

        when(currencyDao.findById(102L, connectionMock)).thenReturn(currency);

        Boolean result = subj.checkCurrencyId(accountDto, connectionMock);
        assertTrue(result);

    }



}