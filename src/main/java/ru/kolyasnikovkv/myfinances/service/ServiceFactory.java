package ru.kolyasnikovkv.myfinances.service;

import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.service.converters.*;

public class ServiceFactory {
    private static UserConverter userConverter;
    private static TransactionConverter transactionConverter;
    private static CurrencyConverter currencyConverter;
    private static CategoryConverter categoryConverter;
    private static AccountConverter accountConverter;
    private static DigestService digestService;
    private static SecurityService securityService;
    private static CurrencyService currencyService;
    private static CategoryService categoryService;
    private static TransactionService transactionService;
    private static  AccountService accountService;
    private static PersonService personService;

    public static UserConverter getUserConverter() {
        if (userConverter == null) {
            userConverter = new UserConverter();
        }

        return userConverter;
    }

    public static TransactionConverter getTransactionConverter() {
        if (transactionConverter == null) {
            transactionConverter = new TransactionConverter();
        }

        return transactionConverter;
    }

    public static CurrencyConverter getCurrencyConverter() {
        if (currencyConverter == null) {
            currencyConverter = new CurrencyConverter();
        }

        return currencyConverter;
    }

    public static CategoryConverter getCategoryConverter() {
        if (categoryConverter == null) {
            categoryConverter = new CategoryConverter();
        }

        return categoryConverter;
    }

    public static AccountConverter getAccountConverter() {
        if (accountConverter == null) {
            accountConverter = new AccountConverter();
        }

        return accountConverter;
    }

    public static DigestService getDigestService() {
        if (digestService == null) {
            digestService = new DigestService();
        }

        return digestService;
    }

    public static SecurityService getSecurityService() {
        if (securityService == null) {
            securityService = new SecurityService(DaoFactory.getPersonDao(), getDigestService(), getUserConverter());
        }

        return securityService;
    }

    public static CurrencyService getCurrencyService() {
        if (currencyService == null) {
            currencyService = new CurrencyService(DaoFactory.getCurrencyDao(), currencyConverter);
        }
        return currencyService;
    }

    public static CategoryService getCategoryService() {
        if (categoryService == null) {
            categoryService = new CategoryService(DaoFactory.getCategoryDao(), categoryConverter);
        }

        return categoryService;
    }

    public static TransactionService getTransactionService() {
        if (transactionService == null) {
            transactionService = new TransactionService(DaoFactory.getTransactionDao(), DaoFactory.getCategoryDao(), transactionConverter, DaoFactory.getAccountDao());
        }
        return transactionService;
    }

    public static AccountService getAccountService() {
        if (accountService == null) {
            accountService = new AccountService(DaoFactory.getAccountDao(), accountConverter, DaoFactory.getCurrencyDao(), DaoFactory.getPersonDao());
        }
        return accountService;
    }

    public static PersonService getPersonService() {
        if (personService == null) {
            personService = new PersonService(DaoFactory.getPersonDao(), userConverter);
        }
        return personService;
    }
}
