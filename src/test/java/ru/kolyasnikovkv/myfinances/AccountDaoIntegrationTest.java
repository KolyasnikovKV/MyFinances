package ru.kolyasnikovkv.myfinances;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.kolyasnikovkv.myfinances.dao.AccountDao;
import ru.kolyasnikovkv.myfinances.dao.CurrencyDao;
import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Account;
import ru.kolyasnikovkv.myfinances.dao.domain.Currency;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.service.AccountService;
import ru.kolyasnikovkv.myfinances.service.ServiceFactory;
import ru.kolyasnikovkv.myfinances.service.converters.AccountConverter;
import ru.kolyasnikovkv.myfinances.service.dto.AccountDto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;


public class AccountDaoIntegrationTest {

    AccountDao accountDao = DaoFactory.getAccountDao();
    AccountService subj;
    Person person = null;
    Currency currency = null;

    @Before
    public void setUp() throws SQLException {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:testDatabase");//H2 DB работает только в памяти!
        System.setProperty("jdbcUsername", "sa");
        System.setProperty("jdbcPassword", "");


        person = new Person();
        person.setEmail("fot@ru.ru");
        person.setPassword("dddde");
        Connection connection = DaoFactory.getConnection();

        person = DaoFactory.getPersonDao().insert(person, connection);


        currency = new Currency();
        currency.setName("Валюта");


        currency = DaoFactory.getCurrencyDao().insert(currency, connection);

        connection.close();

        CurrencyDao currencyDao = DaoFactory.getCurrencyDao();
        AccountConverter accountConverter = ServiceFactory.getAccountConverter();
        PersonDao personDao = DaoFactory.getPersonDao();

        accountDao = DaoFactory.getAccountDao();
        subj = new AccountService(accountDao, accountConverter, currencyDao, personDao);


    }


    @Test
    public void accountInsert_ok() throws SQLException {

        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();


        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(12045677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        assertEquals(-1L, (long)account.getId()); //ключ ещё не сгенерирован!


        account = subj.createNewAccount(account, connection);


        assertNotNull(account);
        assertNotEquals(-1L, (long) account.getId()); // Проверил генерацию ключа по умолчанию -11
        connection.close();
    }

    @Test
    public void accountInsert_personIdWrong() throws SQLException {

        Connection connection = DaoFactory.getConnection();
        AccountDto account = new AccountDto();


        account.setPersonId(10000L); //Нет реального человека - нет счёта!
        account.setNumberAccount(12545677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        assertEquals(-1L,(long) account.getId());


        account = subj.createNewAccount(account, connection);


        assertNull(account);
        connection.close();
    }

    @Test
    public void accountInsert_CurrencyIdWrong() throws SQLException {

        Connection connection = DaoFactory.getConnection();
        AccountDto account = new AccountDto();

        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(14045677L);
        account.setCurrencyId(1200L); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        assertEquals(-1L,(long) account.getId());


        account = subj.createNewAccount(account, connection);


        assertNull(account);
        connection.close();
    }

    @Test
    public void accountUpdate_ok() throws SQLException {
        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();


        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(11045677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        account = subj.createNewAccount(account, connection);
        account.setBalance(BigDecimal.valueOf(560));
        account = subj.updateAccount(account, connection);


        assertEquals(BigDecimal.valueOf(560), account.getBalance());

        connection.close();
    }

    @Test
    public void accountUpdate_idWrong() throws SQLException {
        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();


        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(11045677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        account = subj.createNewAccount(account, connection);
        AccountDto accountTwo = account;
        accountTwo.setId(100L);
        accountTwo.setBalance(BigDecimal.valueOf(560));

        account = subj.updateAccount(accountTwo, connection);


        assertNotEquals(account, accountTwo);

        connection.close();
    }


    @Test
    public void accountFindId_ok() throws SQLException {

        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();


        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(11045677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(12.0));

        account = subj.createNewAccount(account, connection);

        Account accountFromService = accountDao.findById(account.getId(), connection);
        AccountDto fromService = new AccountConverter().accountToAccountDto(accountFromService);


        assertEquals(account.getCurrencyId(), fromService.getCurrencyId());
        assertEquals(account.getPersonId(), fromService.getPersonId());
        assertEquals(account.getId(), fromService.getId());
        assertEquals(account.getNumberAccount(), fromService.getNumberAccount());
        assertEquals(account.getBalance(), fromService.getBalance());

        connection.close();
    }

    @Test
    public void accountFindById_idWrong() throws SQLException {

        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();


        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(11045677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(12.0));

        account = subj.createNewAccount(account, connection);

        Account accountFromService = accountDao.findById(-1L, connection);
        AccountDto fromService = new AccountConverter().accountToAccountDto(accountFromService);


        assertNull(accountFromService);

        connection.close();
    }

    @Test
    public void accountFindAll_ok() throws SQLException {
        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();


        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(11045677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        account = subj.createNewAccount(account, connection);

        List<Account> list = accountDao.findAll();
        Account account1 = list.get(0);


        assertEquals(account.getId(), account1.getId());
        assertEquals(account.getPersonId(), account1.getPerson());
        assertEquals(account.getCurrencyId(), account1.getCurrency());


        connection.close();
    }


    /*@Test
    public void accountDelete_ok() throws SQLException {

        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();

        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(11045677L);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        account = subj.createNewAccount(account, connection);

        assertTrue(subj.deleteAccount(account, connection));


        connection.close();
    }

    @Test
    public void accountDelete_idWrong() throws SQLException {

        AccountDto account = new AccountDto();
        Connection connection = DaoFactory.getConnection();

        account.setPersonId(person.getId()); //Нет реального человека - нет счёта!
        account.setNumberAccount(11045677);
        account.setCurrencyId(currency.getId()); //Нет такой валюты - нет счёта!
        account.setDescription("Тест");
        account.setBalance(BigDecimal.valueOf(100));

        account = subj.createNewAccount(account, connection);

        account.setId(-1);

        assertFalse(subj.deleteAccount(account, connection));


        connection.close();
    }*/

    @After
    public void setDown() throws SQLException {
        //Код в нем будет "TRUNCATE TABLE USER
        Connection connection = DaoFactory.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM account;")) {

            preparedStatement.executeUpdate();


        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person;")) {

            preparedStatement.executeUpdate();

        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM currency;")) {

            preparedStatement.executeUpdate();

        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        connection.close();

    }

}
