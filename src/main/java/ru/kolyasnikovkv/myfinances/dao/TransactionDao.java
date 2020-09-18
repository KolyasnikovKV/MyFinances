package ru.kolyasnikovkv.myfinances.dao;


import ru.kolyasnikovkv.myfinances.dao.domain.Transaction;
import ru.kolyasnikovkv.myfinances.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.kolyasnikovkv.myfinances.dao.DaoFactory.getConnection;


public class TransactionDao implements Dao<Transaction, Long> {

    private Transaction getTransaction(ResultSet rs, Transaction transaction) throws SQLException {

        transaction.setId(rs.getLong(1));
        transaction.setAccountFrom(rs.getLong(2));
        transaction.setAccountTo(rs.getLong(3));
        transaction.setAmmount(rs.getBigDecimal(4));
        transaction.setDate(rs.getString(5));
        transaction.setCategory(rs.getLong(6));

        return transaction;
    }

    private void setTransaction(PreparedStatement preparedStatement, Transaction transaction) throws SQLException {
        preparedStatement.setLong(1, transaction.getAccountFrom());
        preparedStatement.setLong(1, transaction.getAccountTo());
        preparedStatement.setBigDecimal(2, transaction.getAmmount());
        preparedStatement.setString(3,  transaction.getDate());
        preparedStatement.setLong(4, transaction.getCategory());
    }

    @Override
    public Transaction findById(Long id, Connection connection) {
        Transaction transaction = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction" +
                     "WHERE (transaction.id = ?)")) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                transaction = new Transaction();
                return getTransaction(rs, transaction);
            }
        } catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("Select * From transaction");

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
        } catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return list;
    }

    @Override
    public Transaction insert(Transaction transaction, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transaction(" +
                     "account_from, account_to, ammount, date, category) VALUES( ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS);) {

            setTransaction(preparedStatement, transaction);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained");
                }
            }

           return transaction;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Transaction update(Transaction transaction, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE transaction SET " +
                     "account_from = ?, account_to = ?, ammount = ?, date = ?, category_id = ? " +
                     "WHERE transaction.id = ?");) {
            preparedStatement.setLong(5, transaction.getId());
            setTransaction(preparedStatement, transaction);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            return transaction;

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public void delete(Long id, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM  transaction WHERE (transaction.id = ?)")) {

            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }
        } catch (SQLException exept) {
            throw new DaoException(exept);
        }
    }


    public List<Transaction> findByAccountId(Long accountId, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction " +
                     "WHERE transaction.account_from = ?")) {

            preparedStatement.setLong(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
            return list;

        } catch (SQLException exept) {
            throw new DaoException(exept);
        }
    }

    public List<Transaction> findByAccountIdAndData(Long accountId, Date date, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction " +
                     "WHERE transaction.account_from = ? and transaction.date = ?")) {

            preparedStatement.setLong(1, accountId);
            preparedStatement.setDate(2, date);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
        } catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return list;
    }

    public List<Transaction> findByAccountIdCategoryId(Long accountId, Long categoryId, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction " +
                     "WHERE transaction.account_from = ? and transaction.category_id = ?")) {

            preparedStatement.setLong(1, accountId);
            preparedStatement.setLong(2, categoryId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
        } catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return list;
    }

    public List<Transaction> findByAccountIdCategoryIdDate(Long accountId, Long categoryId, Date date, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction WHERE transaction.account_from =? and transaction.category_id = ? and transaction.date = ?")) {

            preparedStatement.setLong(1, accountId);
            preparedStatement.setLong(2, categoryId);
            preparedStatement.setDate(3, date);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
        } catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return list;
    }

}