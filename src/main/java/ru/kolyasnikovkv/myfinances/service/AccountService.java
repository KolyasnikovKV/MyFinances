package ru.kolyasnikovkv.myfinances.service;

import ru.kolyasnikovkv.myfinances.dao.AccountDao;
import ru.kolyasnikovkv.myfinances.dao.CurrencyDao;
import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Account;
import ru.kolyasnikovkv.myfinances.service.converters.AccountConverter;
import ru.kolyasnikovkv.myfinances.service.dto.AccountDto;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.sql.Connection;
import java.util.List;

public class AccountService {
    private final AccountDao accountDao;
    private final AccountConverter accountConverter;
    private final CurrencyDao currencyDao;
    private final PersonDao personDao;

    public AccountService(AccountDao accountDao, AccountConverter accountConverter, CurrencyDao currencyDao, PersonDao personDao) {
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
        this.currencyDao = currencyDao;
        this.personDao = personDao;
    }

    public AccountDto createNewAccount(AccountDto accountDto, Connection connection) {

        if (accountDto == null) {
             return null;
         }

         Account account = accountConverter.accountDtoToAccount(accountDto);
         if (personDao.findById(account.getPerson(), connection) != null) {
             int count = accountDao.countAccountPerson(account.getPerson(), connection);
             if (count < DaoFactory.maxCountAccount && checkCurrencyId(accountDto, connection) && checkNumberAccount(accountDto, connection)) { // Не более 5 счетов в одни руки!
                 return accountConverter.accountToAccountDto(accountDao.insert(account, connection));
             }
         }
        return null;
    }

    public List<AccountDto> getAllAccount() {
        return accountConverter.listAccountToListAccountDto(accountDao.findAll());
    }

    public List<AccountDto> findByPesonId(UserDto personDto, Connection connection) {
        return accountConverter.listAccountToListAccountDto(accountDao.findByPerson(personDto.getId(), connection));
    }

    public boolean checkNumberAccount(AccountDto accountDto, Connection connection) {
        if (accountDao.findByNumberAccount(accountDto.getNumberAccount(), connection).size() != 0) {
            return false;
        }
        return true;
    }

    private boolean checkPersonId(AccountDto accountDto, Connection connection) {
        if (DaoFactory.getPersonDao().findById(accountDto.getPersonId(), connection) != null) {
            return true;
        }
        return false;
    }

    public boolean checkCurrencyId(AccountDto accountDto, Connection connection) {
        if (currencyDao.findById(accountDto.getCurrencyId(), connection) != null) { //Существует
            return true;
        }
        return false; //Не существует
    }

    public AccountDto updateAccount(AccountDto accountDto, Connection connection) {
        Account account = accountConverter.accountDtoToAccount(accountDto);
        if (accountDao.findById(account.getId(), connection) != null) {
               return accountConverter.accountToAccountDto(accountDao.update(account, connection));
            }
        return null;
    }

    public void deleteAccount(AccountDto accountDto, Connection connection) {
        accountDao.delete(accountDto.getId(), connection);
    }
}
