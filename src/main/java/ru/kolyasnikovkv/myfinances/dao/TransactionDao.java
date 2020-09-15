package ru.kolyasnikovkv.myfinances.dao;

import ru.titov.s05.dao.domain.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.titov.s05.dao.DaoFactory.getConnection;

public class TransactionDao implements Dao<Transaction, Integer> {

    private Transaction getTransaction(ResultSet rs, Transaction transaction) throws SQLException {

        transaction.setId(rs.getInt(1));
        transaction.setAccountID(rs.getInt(2));
        transaction.setSum(rs.getBigDecimal(3));
        transaction.setDate(rs.getString(4));
        transaction.setCategorieID(rs.getInt(5));

        return transaction;
    }

    private void setTransaction(PreparedStatement preparedStatement, Transaction transaction) throws SQLException {
        preparedStatement.setInt(1, transaction.getAccountID());
        preparedStatement.setBigDecimal(2, transaction.getSum());
        preparedStatement.setString(3,  transaction.getDate());
        preparedStatement.setInt(4, transaction.getCategorieID());
    }

    @Override
    public Transaction findById(Integer id, Connection connection) {
        Transaction transaction = new Transaction();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction" +
                     "WHERE (transaction.id = ?)")) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return getTransaction(rs, transaction);
            }
        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return null;
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
            throw new RuntimeException(exept);
        }

        return list;
    }

    @Override
    public Transaction insert(Transaction transaction, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transaction(" +
                     "account_id, sum, date, categorie_id) VALUES( ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS);) {

            setTransaction(preparedStatement, transaction);
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                int id = rs.getInt(1); //вставленный ключ
                transaction.setId(id);
            }


            return transaction;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction update(Transaction transaction, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE transaction SET " +
                     "account_id = ?, sum = ?, date = ?, categorie_id = ? " +
                     "WHERE transaction.id = ?");) {
            preparedStatement.setInt(5, transaction.getId());
            setTransaction(preparedStatement, transaction);


            if (preparedStatement.executeUpdate() > 0) {
                return transaction;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean delete(Integer id, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM  transaction WHERE (transaction.id = ?)")) {


            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return false;
    }


    public List<Transaction> findByAccountId(int accountId, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction " +
                     "WHERE transaction.account_id = ?")) {

            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
            return list;

        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }
    }

    public List<Transaction> findByAccountIdAndData(int accountId, Date date, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction " +
                     "WHERE transaction.account_id = ? and transaction.date = ?")) {

            preparedStatement.setInt(1, accountId);
            preparedStatement.setDate(2, date);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return list;
    }

    public List<Transaction> findByAccountIdCategorieId(int accountId, int categorieId, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction " +
                     "WHERE transaction.account_id = ? and transaction.categorie_id = ?")) {

            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, categorieId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return list;
    }

    public List<Transaction> findByAccountIdCategorieIdDate(int accountId, int categorieId, Date date, Connection connection) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From transaction WHERE transaction.account_id =? and transaction.categorie_id = ? and transaction.date = ?")) {

            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, categorieId);
            preparedStatement.setDate(3, date);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                list.add(getTransaction(rs, transaction));
            }
        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return list;
    }

}