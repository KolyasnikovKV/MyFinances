package ru.kolyasnikovkv.myfinances.dao;

import ru.kolyasnikovkv.myfinances.dao.domain.Currency;
import ru.kolyasnikovkv.myfinances.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.kolyasnikovkv.myfinances.dao.DaoFactory.getConnection;

public class CurrencyDao implements Dao<Currency, Integer> {

    private Currency getCurrency(ResultSet rs, Currency currency) throws SQLException {
        currency.setId(rs.getLong(1));
        currency.setName(rs.getString(2));

        return currency;
    }

    @Override
    public Currency findById(Long id, Connection connection) {
        Currency currency = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From currency " +
                     "WHERE (currency.id = ?)")) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();


            if (rs.next()) {
                currency = new Currency();
                return getCurrency(rs, currency);
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }

        return currency;
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> list = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("Select * From currency");

            while (rs.next()) {
                Currency currency = new Currency();
                list.add(getCurrency(rs, currency));
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }

        return list;
    }

    @Override
    public Currency insert(Currency currency, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO currency(" +
                     "name) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {


            preparedStatement.setString(1, currency.getName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    currency.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained");
                }
            }

            return currency;

        } catch (SQLException e) {
            throw new DaoException(e); // кидать свое исключение и ловить в тестах
        }

    }


    @Override
    public Currency update(Currency currency, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE currency SET " +
                     "name = ? " +
                     "WHERE currency.id = ?");)
        {
            preparedStatement.setLong(2, currency.getId());
            preparedStatement.setString(1, currency.getName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            return currency;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM currency WHERE (currency.id = ?)")) {

            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }
    }

    public Currency findByName(String name, Connection connection) {

        Currency currency = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From currency " +
                     "WHERE (UPPER(currency.name) = UPPER(?))")) {

            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return getCurrency(rs, currency);
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }

         return currency;

    }
}
