package ru.kolyasnikovkv.myfinances;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.*;
import ru.kolyasnikovkv.myfinances.dao.domain.*;
import ru.kolyasnikovkv.myfinances.service.ServiceFactory;
import ru.kolyasnikovkv.myfinances.service.TransactionService;
import ru.kolyasnikovkv.myfinances.service.converters.TransactionConverter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceIntagrationTest {
    AccountDao accountDao;
    PersonDao personDao;
    CurrencyDao currencyDao;
    CategoryDao categoryDao;
    TransactionDao transactionDao;
    TransactionService subj;
    @Mock TransactionDao transactionDaoMock;
    @Mock TransactionConverter transactionConverterMock;


    @Before
    public void SetUp() throws SQLException {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:testDatabase");//H2 DB работает только в памяти!
        System.setProperty("jdbcUsername", "sa");
        System.setProperty("jdbcPassword", "");

        accountDao = DaoFactory.getAccountDao();
        personDao = DaoFactory.getPersonDao();
        currencyDao = DaoFactory.getCurrencyDao();
        transactionDao = DaoFactory.getTransactionDao();
        categoryDao = DaoFactory.getCategoryDao();
        subj = ServiceFactory.getTransactionService();
    }

    @Test
    public void transfer_testRollback() throws SQLException {
        Connection connection = DaoFactory.getConnection();
        Connection connectionTwo = DaoFactory.getConnection();


        doThrow(new RuntimeException()).when(transactionDaoMock).insert(any(Transaction.class), any(Connection.class));
        subj = new TransactionService(transactionDaoMock, categoryDao, transactionConverterMock, accountDao);


        Currency currency = new Currency();
        currency.setName("Гривна");
        currencyDao.insert(currency, connectionTwo);


        Category category = new Category();
        category.setDescription("гривна");
        category = categoryDao.insert(category, connectionTwo);

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        Person person2 = new Person();
        person2.setEmail("Viвo@ao.ru");
        person2.setPassword("onаe");
        person = personDao.insert(person, connectionTwo);
        person2 = personDao.insert(person2, connectionTwo);

        Account accountTo = new Account();
        accountTo.setPerson(person2.getId());
        accountTo.setNumberAccount(85522677L);
        accountTo.setCurrency(currency.getId());
        accountTo.setDescription("Тест2");
        accountTo.setBalance(BigDecimal.valueOf(0));
        accountTo = accountDao.insert(accountTo, connectionTwo);


        Account accountFrom = new Account();
        accountFrom.setPerson(person.getId());
        accountFrom.setNumberAccount(12333677L);
        accountFrom.setCurrency(currency.getId());
        accountFrom.setDescription("Тест");
        accountFrom.setBalance(BigDecimal.valueOf(1000));
        accountFrom = accountDao.insert(accountFrom, connectionTwo);


        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom.getId());
        transaction.setAccountTo(accountTo.getId());
        transaction.setAmmount(BigDecimal.valueOf(100));
        transaction.setDate("22-11-78");
        transaction.setCategory(category.getId());
        transaction = transactionDao.insert(transaction, connectionTwo);

        BigDecimal sum = BigDecimal.valueOf(99);
        category.setDescription("перевод2");

        try {
            subj.transfer(accountFrom, accountTo, sum, category, connection);
        } catch (RuntimeException s) {
            //Не обрабатываю!
        }
        accountFrom = accountDao.findById(accountFrom.getId(), connectionTwo);
        accountTo = accountDao.findById(accountTo.getId(), connectionTwo);
        System.out.println("Balance from: " + accountFrom.getBalance() + "     Balance to: " + accountTo.getBalance());

        assertEquals(BigDecimal.valueOf(1000.0), accountFrom.getBalance());
        assertEquals(BigDecimal.valueOf(0.0), accountTo.getBalance());
        connectionTwo.close();

    }

    @Test
    public void transfer_ok() throws SQLException {
        Connection connection = DaoFactory.getConnection();
        Connection connectionTwo = DaoFactory.getConnection();


        Currency currency = new Currency();
        currency.setName("USD");
        currencyDao.insert(currency, connectionTwo);
        Currency currency2 = new Currency();
        currency.setName("рубль");
        currency = currencyDao.insert(currency, connectionTwo);

        Category category = new Category();
        category.setDescription("наличка");
        category = categoryDao.insert(category, connectionTwo);

        Person person = new Person();
        person.setEmail("io@ao.ru");
        person.setPassword("ffffffffff");
        Person person2 = new Person();
        person2.setEmail("ioc@ao.ru");
        person2.setPassword("fffffcfffff");
        person = personDao.insert(person, connectionTwo);
        person2 = personDao.insert(person2, connectionTwo);

        Account accountTo = new Account();
        accountTo.setPerson(person2.getId());
        accountTo.setNumberAccount(88045677L);
        accountTo.setCurrency(currency.getId());
        accountTo.setDescription("Тест");
        accountTo.setBalance(BigDecimal.valueOf(0));
        accountTo = accountDao.insert(accountTo, connectionTwo);


        Account accountFrom = new Account();
        accountFrom.setPerson(person.getId());
        accountFrom.setNumberAccount(12045677L);
        accountFrom.setCurrency(currency.getId());
        accountFrom.setDescription("Тест");
        accountFrom.setBalance(BigDecimal.valueOf(1000));
        accountFrom = accountDao.insert(accountFrom, connectionTwo);


        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom.getId());
        transaction.setAccountTo(accountFrom.getId());
        transaction.setAmmount(BigDecimal.valueOf(100));
        transaction.setDate("22-11-78");
        transaction.setCategory(category.getId());
        transaction = transactionDao.insert(transaction, connectionTwo);


        BigDecimal sum = BigDecimal.valueOf(99);
        category.setDescription("перевод");


        try {
            subj.transfer(accountFrom, accountTo, sum, category, connection);
        } catch (RuntimeException s) {
            //Не обрабатываю!
        }
        accountFrom = accountDao.findById(accountFrom.getId(), connectionTwo);
        accountTo = accountDao.findById(accountTo.getId(), connectionTwo);
        System.out.println("Balance from: " + accountFrom.getBalance() + "     Balance to: " + accountTo.getBalance());

        assertEquals(BigDecimal.valueOf(901.0), accountFrom.getBalance());
        assertEquals(BigDecimal.valueOf(99.0), accountTo.getBalance());

        connectionTwo.close();
        connection.close();

    }

    @Test
    public void addSum_ok() throws  SQLException {
        Connection connection = DaoFactory.getConnection();
        Connection connectionTwo = DaoFactory.getConnection();
        Currency currency = new Currency();
        currency.setName("Евро_");
        currencyDao.insert(currency, connectionTwo);
        Category category = new Category();
        category.setDescription("карта_");
        category = categoryDao.insert(category, connectionTwo);
        Person person = new Person();
        person.setEmail("iввввввor@ao.ru");
        person.setPassword("ffffrfdqdffff");
        person = personDao.insert(person, connectionTwo);
        Account account = new Account();
        account.setPerson(person.getId());
        account.setNumberAccount(88045697L);
        account.setCurrency(currency.getId());
        account.setDescription("ЕщёТест_");
        account.setBalance(BigDecimal.valueOf(0));
        account = accountDao.insert(account, connectionTwo);

        BigDecimal sum = BigDecimal.valueOf(9201.0);


        try {
            subj.addSum(account, sum, category, connection);
        } catch (RuntimeException s) {
            //Не обрабатываю!
        }
        account = accountDao.findById(account.getId(), connectionTwo);

        System.out.println("Balance: " + account.getBalance());

        assertEquals(BigDecimal.valueOf(9201.0), account.getBalance());
        connectionTwo.close();
        connection.close();

    }

    @Test
    public void takeSum_ok() throws  SQLException {
        Connection connection = DaoFactory.getConnection();
        Connection connectionTwo = DaoFactory.getConnection();

        Currency currency = new Currency();
        currency.setName("Евро-2");
        currencyDao.insert(currency, connectionTwo);

        Category category = new Category();
        category.setDescription("карта-4");
        category = categoryDao.insert(category, connectionTwo);

        Person person = new Person();
        person.setEmail("ioКr@ao.ru");
        person.setPassword("ffffrffцfff");
        person = personDao.insert(person, connectionTwo);

        Account account = new Account();
        account.setPerson(person.getId());
        account.setNumberAccount(88045697L);
        account.setCurrency(currency.getId());
        account.setDescription("ЕщёОдинТест");
        account.setBalance(BigDecimal.valueOf(901));
        account = accountDao.insert(account, connectionTwo);

        BigDecimal sum = BigDecimal.valueOf(2201.0);


        try {
            subj.takeSum(account, sum, category, connection);
        } catch (RuntimeException s) {
            //Не обрабатываю!
        }
        account = accountDao.findById(account.getId(), connectionTwo);

        System.out.println("Balance: " + account.getBalance());

        assertEquals(BigDecimal.valueOf(-1300.0), account.getBalance());
        connectionTwo.close();
        connection.close();

    }


    @Test
    public void takeSum_Exception() throws  SQLException {
        Connection connection = DaoFactory.getConnection();
        Connection connectionTwo = DaoFactory.getConnection();

        doThrow(new RuntimeException()).when(transactionDaoMock).insert(any(Transaction.class), any(Connection.class));
        subj = new TransactionService(transactionDaoMock, categoryDao, transactionConverterMock, accountDao);

        Currency currency = new Currency();
        currency.setName("Тугрик");
        currencyDao.insert(currency, connectionTwo);

        Category category = new Category();
        category.setDescription("Безналичный");
        category = categoryDao.insert(category, connectionTwo);

        Person person = new Person();
        person.setEmail("iцor@ao.ru");
        person.setPassword("fffуfrfffff");
        person = personDao.insert(person, connectionTwo);

        Account account = new Account();
        account.setPerson(person.getId());
        account.setNumberAccount(88045697L);
        account.setCurrency(currency.getId());
        account.setDescription("ЕщёТест");
        account.setBalance(BigDecimal.valueOf(901));
        account = accountDao.insert(account, connectionTwo);

        BigDecimal sum = BigDecimal.valueOf(2201.0);

        try {
            subj.takeSum(account, sum, category, connection);
        } catch (RuntimeException s) {
            //Не обрабатываю!
        }
        account = accountDao.findById(account.getId(), connectionTwo);

        System.out.println("Balance: " + account.getBalance());

        assertEquals(BigDecimal.valueOf(901.0), account.getBalance());
        connectionTwo.close();
        connection.close();

    }

    @Test
    public void addSum_Exception() throws  SQLException {
        Connection connection = DaoFactory.getConnection();
        Connection connectionTwo = DaoFactory.getConnection();

        doThrow(new RuntimeException()).when(transactionDaoMock).insert(any(Transaction.class), any(Connection.class));
        subj = new TransactionService(transactionDaoMock, categoryDao, transactionConverterMock, accountDao);

        Currency currency = new Currency();
        currency.setName("Евро");
        currencyDao.insert(currency, connectionTwo);

        Category category = new Category();
        category.setDescription("карта");
        category = categoryDao.insert(category, connectionTwo);

        Person person = new Person();
        person.setEmail("ior@ao.ru");
        person.setPassword("ffffrfffff");
        person = personDao.insert(person, connectionTwo);

        Account account = new Account();
        account.setPerson(person.getId());
        account.setNumberAccount(88045697L);
        account.setCurrency(currency.getId());
        account.setDescription("ЕщёТест");
        account.setBalance(BigDecimal.valueOf(100));
        account = accountDao.insert(account, connectionTwo);

        BigDecimal sum = BigDecimal.valueOf(9201.0);


        try {
            subj.addSum(account, sum, category, connection);
        } catch (RuntimeException s) {
            //Не обрабатываю!
        }
        account = accountDao.findById(account.getId(), connectionTwo);

        System.out.println("Balance: " + account.getBalance());

        assertEquals(BigDecimal.valueOf(100.0), account.getBalance());
        connectionTwo.close();
        connection.close();

    }



}



