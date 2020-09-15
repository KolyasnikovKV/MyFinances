package ru.kolyasnikovkv.myfinances.dao;

import ru.kolyasnikovkv.myfinances.dao.Dao;
import ru.kolyasnikovkv.myfinances.dao.domain.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.kolyasnikovkv.myfinances.dao.DaoFactory.getConnection;


public class CategorieDao implements Dao<Categorie, Integer> {


    @Override
    public Categorie findById(Integer id, Connection connection) {
        Categorie categorie = new Categorie();

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From categorie " +
                     "WHERE (categorie.id = ?)")) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return getCategorie(rs, categorie);
            }
        }
        catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return null;
    }

    @Override
    public List<Categorie> findAll() {
        List<Categorie> list = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("Select * From categorie ");

            while (rs.next()) {

                Categorie categorie = new Categorie();
                list.add(getCategorie(rs, categorie));

            }
        }
        catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return list;
    }

    private Categorie getCategorie(ResultSet rs, Categorie categorie) throws SQLException {
        categorie.setId(rs.getInt(1));
        categorie.setDescription(rs.getString(2));

        return categorie;
    }

    @Override
    public Categorie insert(Categorie categorie, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO categorie(" +
                     "description) VALUES( ? )", Statement.RETURN_GENERATED_KEYS);)
        {

            preparedStatement.setString(1, categorie.getDescription());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                int id = rs.getInt(1); //вставленный ключ
                categorie.setId(id);
            }

            return categorie;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Categorie update(Categorie categorie, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE categorie SET " +
                     "description = ?" +
                     "WHERE categorie.id = ?");)
        {
            preparedStatement.setInt(2, categorie.getId());
            preparedStatement.setString(1, categorie.getDescription());

            if (preparedStatement.executeUpdate() > 0) {

                return categorie;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean delete(Integer id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM categorie WHERE (categorie.id = ?)" )) {

            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() > 0) {
                return true;
            }

        }
        catch (SQLException exept) {
            throw new RuntimeException(exept);
        }

        return false;
    }
}
