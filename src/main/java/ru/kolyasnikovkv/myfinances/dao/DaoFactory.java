package ru.kolyasnikovkv.myfinances.dao;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DaoFactory {
    private static DataSource dataSource;
    private static PersonDao personDao;
    private static CategoryDao categoryDao;
    private static CurrencyDao currencyDao;
    private static TransactionDao transactionDao;
    private static AccountDao accountDao;
    public static final int maxCountAccount = 5; // Максимальное число счетов у одной персоны

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(System.getProperty("jdbcUrl", "jdbc:postgresql://127.0.0.1:5432/my_finances"));
            ds.setUsername(System.getProperty("jdbcUsername", "postgres"));
            ds.setPassword(System.getProperty("jdbcPassword", "12345"));
            dataSource = ds;
            initDataBase();
        }
        return dataSource;
    }

    public static void initDataBase() {
        try {
            DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
            Liquibase liquibase = new Liquibase("liquibase.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DatabaseException e) {
            e.printStackTrace(); // Не создалась БД
        } catch (LiquibaseException e) {
            e.printStackTrace(); // changelog не прошел
        }
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static PersonDao getPersonDao() {
        if (personDao == null) {
            personDao = new PersonDao(getDataSource());
        }
        return personDao;
    }

    public static  CategoryDao getCategoryDao() {
        if (categoryDao == null) {
            categoryDao = new CategoryDao();
        }

        return categoryDao;
    }

    public static CurrencyDao getCurrencyDao() {
        if (currencyDao == null) {
            currencyDao = new CurrencyDao();
        }
        return currencyDao;
    }

    public static TransactionDao getTransactionDao() {
        if (transactionDao == null) {
            transactionDao = new TransactionDao();
        }
        return transactionDao;
    }

    public static AccountDao getAccountDao() {
        if (accountDao == null) {
            accountDao = new AccountDao();
        }

        return accountDao;
    }
}
