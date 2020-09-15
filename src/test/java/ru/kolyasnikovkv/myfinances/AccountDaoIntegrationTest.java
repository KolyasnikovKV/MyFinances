package ru.kolyasnikovkv.myfinances;

import org.junit.Before;
import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Currency;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountDaoIntegrationTest {
    //AccountDao accountDao = DaoFactory.getAccountDao();
    //AccountService subj;
    Person person = null;
    Currency currency = null;

    @Before
    public void setUp() throws SQLException{
        System.setProperty("jdbcUrl", "jdbc:h2:mem:testDatabase");//H2 DB работает только в памяти!
        System.setProperty("jdbcUsername", "sa");
        System.setProperty("jdbcPassword", "");

        person = new Person();
        person.setEmail("kk@mail.ru");
        person.setPassword("pwd");
        Connection connection = DaoFactory.getConnection();

        person = DaoFactory.getPersonDao().insert(person, connection);

        /*currency = new Currency();
        currency.setNameOfCurrency("Валюта");

        currency = DaoFactory.getCurrencyDao().insert(currency, connection);
        connection.close();

        CurrencyDao currencyDao = DaoFactory.getCurrencyDao();
        AccountConverter accountConverter = ServiceFactory.getAccountConverter();
        PersonDao personDao = DaoFactory.getPersonDao();

        accountDao = DaoFactory.getAccountDao();
        subj = new AccountService(accountDao, accountConverter, currencyDao, personDao);*/


    }
}
