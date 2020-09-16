package ru.kolyasnikovkv.myfinances.dao;


import ru.kolyasnikovkv.myfinances.dao.domain.Account;
import ru.kolyasnikovkv.myfinances.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.kolyasnikovkv.myfinances.dao.DaoFactory.getConnection;

public class AccountDao implements Dao<Account, Integer> {

    @Override
    public Account findById(Long id, Connection connection) {
        Account account = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From account " +
                     "WHERE (account.id = ?)")) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return getAccount(rs, account);
            }
        }
        catch (SQLException exept) {
            throw new DaoException(exept);
        }
        return account;
    }

    private  Account getAccount(ResultSet rs, Account account) throws SQLException {

        account.setId(rs.getLong(1));
        account.setNumberAccount(rs.getInt(2));
        account.setPerson(rs.getInt(3));
        account.setBalance(rs.getBigDecimal(4));
        account.setCurrency(rs.getInt(5));
        account.setDescription(rs.getString(6));

        return account;
    }

    private void setPreparedStatement(PreparedStatement preparedStatement, Account account) throws SQLException {

        preparedStatement.setLong(1, account.getNumberAccount());
        preparedStatement.setLong(2, account.getPerson());
        preparedStatement.setBigDecimal(3, account.getBalance());
        preparedStatement.setLong(4, account.getCurrency());
        preparedStatement.setString(5, account.getDescription());

    }

    @Override
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("Select * From account ");

            while (rs.next()) {
                Account account = new Account();
                list.add(getAccount(rs, account));
            }
        }
        catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return list;
    }

    @Override
    public Account insert(Account account, Connection connection){
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account(" +
                        "number_account, person_id, balance, currency_id, description) VALUES( ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setInt(1, account.getNumberAccount());
            preparedStatement.setInt(2, account.getPerson());
            preparedStatement.setBigDecimal(3, account.getBalance());
            preparedStatement.setInt(4, account.getCurrency());
            preparedStatement.setString(5, account.getDescription());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);

        }
        return account;
    }

    @Override
    public Account update(Account account, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET " +
                     "number_account = ?, person_id = ?, balance = ?, currency_id = ?, description = ? " +
                     "WHERE account.id = ?"))
        {

            preparedStatement.setInt(1, account.getNumberAccount());
            preparedStatement.setInt(2, account.getPerson());
            preparedStatement.setBigDecimal(3, account.getBalance());
            preparedStatement.setInt(4, account.getCurrency());
            preparedStatement.setString(5, account.getDescription());
            preparedStatement.setLong(6, account.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            return account;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM account WHERE (account.id = ?)"))
            {
                preparedStatement.setLong(1, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0){
                    throw new SQLException("Creating transaction failed, no rows affected");
                }
            }
        catch (SQLException exept) {
                throw new DaoException(exept);
            }
    }

    public List<Account> findByNumberAccount(Long numberAccount, Connection connection) {
        Account account = new Account();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From account " +
                     "WHERE (account.number_account = ?)")) {

            preparedStatement.setLong(1, numberAccount);
            ResultSet rs = preparedStatement.executeQuery();
            List<Account> list = new ArrayList();

            while (rs.next()) {
                list.add(getAccount(rs, account));
            }
            return list;
        }
        catch (SQLException exept) {
            throw new DaoException(exept);
        }
    }

    public List<Account> findByPersonId(Long personId, Connection connection) {
        List<Account> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From account " +
                     "WHERE (account.person_id = ?)")) {


            preparedStatement.setLong(1, personId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account account = new Account();
                list.add(getAccount(rs, account));
            }
            return list;
        }
        catch (SQLException exept) {
            throw new RuntimeException(exept);
        }
    }

    public int countAccountPerson(Long personId, Connection connection) {

        int count = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From account " +
                     "WHERE (account.person_id = ?)")) {


            preparedStatement.setLong(1, personId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                count++;
            }
            return count;
        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

    }


    }
