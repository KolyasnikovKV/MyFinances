package ru.kolyasnikovkv.myfinances.service;

import ru.kolyasnikovkv.myfinances.dao.AccountDao;
import ru.kolyasnikovkv.myfinances.dao.CategoryDao;
import ru.kolyasnikovkv.myfinances.dao.TransactionDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Account;
import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.dao.domain.Transaction;
import ru.kolyasnikovkv.myfinances.service.converters.TransactionConverter;
import ru.kolyasnikovkv.myfinances.service.dto.AccountDto;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;
import ru.kolyasnikovkv.myfinances.service.dto.TransactionDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionService {
    private final TransactionDao transactionDao;
    private final CategoryDao categoryDao;
    private final TransactionConverter transactionConverter;
    private final AccountDao accountDao;

    public TransactionService(TransactionDao transactionDao, CategoryDao categoryDao, TransactionConverter transactionConverter, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.categoryDao = categoryDao;
        this.transactionConverter = transactionConverter;
        this.accountDao = accountDao;
    }

    public boolean transfer(Account accountFrom, Account accountTo, BigDecimal sum, Category category, Connection connection) throws SQLException {
        try {
            connection.setAutoCommit(false);

            if (accountFrom.getBalance().compareTo(sum) >= 0) {

                //вычесть средства со счета
                accountFrom.setBalance(accountFrom.getBalance().add(sum.negate()));

                //положить
                accountTo.setBalance(accountTo.getBalance().add(sum));

                //внести изменения на счета
                accountDao.update(accountFrom, connection);
                accountDao.update(accountTo, connection);

                //Закончить транзакцию успешно
                Transaction transaction = new Transaction();
                transaction.setAccountTo(accountFrom.getId());
                transaction.setAccountFrom(accountTo.getId());
                transaction.setAmmount((sum).negate());
                transaction.setCategory(category.getId());
                transaction.setDate(now());
                transactionDao.insert(transaction, connection);

                Transaction transaction2 = transaction;
                transaction2.setAmmount(sum);
                transactionDao.insert(transaction2, connection);

                connection.commit();
                return true;
            }
            else {
                connection.rollback();
                return false;
            }
        }

        catch (Exception sql) {
            connection.rollback();
            return false;
        }

        finally {
            connection.setAutoCommit(true);

            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
            catch (Exception exp) {
                throw  new RuntimeException(exp);
            }
        }
    }

    public boolean deleteTransfer(Account account1, Account account2, BigDecimal sum, Category category, Connection connection) throws SQLException {

        try {
            connection.setAutoCommit(false);

            account1.setBalance(account1.getBalance().add(sum));
            account2.setBalance(account2.getBalance().add(sum.negate()));

            //внести изменения на счета
            accountDao.update(account1, connection);
            accountDao.update(account2, connection);

            //Закончить транзакцию успешно

            Transaction transaction = new Transaction();
            transaction.setAccountFrom(account1.getId());
            transaction.setAccountTo(account2.getId());
            transaction.setCategory(category.getId());
            transaction.setDate(now());
            transaction.setAmmount(sum);
            transactionDao.insert(transaction, connection);

            Transaction transaction2 = transaction;
            transaction2.setAmmount(sum.negate());
            transactionDao.insert(transaction2, connection);

            connection.commit();
            return true;
        }

        catch (SQLException sql) {
            connection.rollback();
        }

        finally {
            connection.setAutoCommit(true);
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
            catch (Exception exp) {
                throw  new RuntimeException(exp);
            }
        }
        return false;
    }

    public Transaction addSum(Account account, BigDecimal sum,  Category category, Connection connection) throws SQLException { //Положить на счет


        try {
            connection.setAutoCommit(false);

            //положить
            account.setBalance(account.getBalance().add(sum));

            //внести изменения на счета
            accountDao.update(account, connection);

            //Закончить транзакцию успешно
            Transaction transaction = new Transaction();
            transaction.setAmmount(sum);
            transaction.setAccountFrom(account.getId());
            transaction.setCategory(category.getId());
            transaction.setDate(now());
            transactionDao.insert(transaction, connection);

            connection.commit();
            return transaction;
        }
        catch (Exception exp){
            connection.rollback();
            throw new RuntimeException(exp);

        }

        finally {

            connection.setAutoCommit(true);
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
            catch (Exception exp) {
                throw  new RuntimeException(exp);
            }
        }
    }

    public Transaction takeSum(Account account, BigDecimal sum,  Category category, Connection connection) throws SQLException {

        try {
            connection.setAutoCommit(false);

            //положить
            account.setBalance(account.getBalance().add(sum.negate()));

            //внести изменения на счета
            accountDao.update(account, connection);


            //Закончить транзакцию успешно
            Transaction transaction = new Transaction();
            transaction.setAmmount(sum.negate());
            transaction.setAccountFrom(account.getId());
            transaction.setCategory(category.getId());
            transaction.setDate(now());
            transactionDao.insert(transaction, connection);

            connection.commit();
            return transaction;
        }
        catch (Exception exp){
            connection.rollback();
            throw new RuntimeException(exp);

        }

        finally {

            connection.setAutoCommit(true);
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
            catch (Exception exp) {
                throw  new RuntimeException(exp);
            }
        }

    }


    private boolean checkAccountId(AccountDto accountDto, Connection connection) {
        AccountDao accountDao = null;

        if (accountDao.findById(accountDto.getId(), connection) != null) {
            return true;
        }
        return false;
    }

    private boolean checkCategoryId(CategoryDto categoryDto, Connection connection) {

        if (categoryDao.findById(categoryDto.getId(), connection) != null) { //Существует
            return true;
        }
        return false; //Не существует
    }

    public TransactionDto createNewTransaction(TransactionDto transactionDto, Connection connection) {

        if (transactionDto == null) {
            return null;
        }

        if (accountDao.findById(transactionDto.getAccountIdFrom(), connection) != null && categoryDao.findById(transactionDto.getCategoryId(), connection) != null) {
            Transaction transaction = transactionConverter.transactionDtoToTransaction(transactionDto);
            transactionDto = transactionConverter.transactionToTransactionDto(transactionDao.insert(transaction, connection));
            return transactionDto;
        }
        return null;
    }

    public TransactionDto updateTransaction(TransactionDto transactionDto, Connection connection) {

        Transaction transaction = transactionDao.findById(transactionDto.getId(), connection);

        if (transaction != null) {
            transactionDto = transactionConverter.transactionToTransactionDto(transactionDao.update(transaction, connection));
            return transactionDto;
        }
        return null;
    }

    public void deleteTransaction(TransactionDto transactionDto, Connection connection) {
        transactionDao.delete(transactionDto.getId(), connection);
    }

    public List<TransactionDto> findTransactionByAccountId(AccountDto accountDto, Connection connection) {
        return  transactionConverter.listTransactionToListTransactionDto(transactionDao.findByAccountId(accountDto.getId(), connection));
    }

    public List<TransactionDto> getAllTransaction(AccountDto accountDto, Connection connection) {
        return  transactionConverter.listTransactionToListTransactionDto(transactionDao.findAll());
    }

    private String now() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        return dateFormat.format(new java.util.Date());
    }


}


