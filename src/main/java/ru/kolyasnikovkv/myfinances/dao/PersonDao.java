package ru.kolyasnikovkv.myfinances.dao;


import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.exception.DaoException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDao implements Dao<Person, Integer> {
    private final DataSource dataSource;

    public PersonDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    private Person getPersonаFromResultSet(ResultSet rs, Person person) throws SQLException {

        person.setId(rs.getLong("id"));
        person.setEmail(rs.getString("email"));
        person.setPassword(rs.getString("password"));
        person.setNick(rs.getString("nick"));
        person.setFullName(rs.getString("fullName"));

        return person;
    }

    public Person findById(Long id, Connection connection){
        Person person = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From person "  +
                "WHERE (person.id =  ?)")) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                person = new Person();
                return getPersonаFromResultSet(rs, person);
            }
        }
        catch (SQLException exept) {
            throw new RuntimeException(exept);
        }
        return person;
    }

    public Person findByMail(String mail, Connection connection) {
        Person person = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From person " +
                "WHERE UPPER(person.e_mail) = UPPER(?)"))
        {

            preparedStatement.setString(1, mail);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                person = new Person();
                return getPersonаFromResultSet(rs, person);
            }

        }
        catch (SQLException exept) {
            throw new RuntimeException(exept);
        }
        return person;
    }

    public List<Person> findAll(){

        List<Person> list = new ArrayList<Person>();
        try(Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT * FROM person");
            while (rs.next()){
                Person person = new Person();
                list.add(getPersonаFromResultSet(rs, person));
            }
        }
        catch (SQLException except){
            throw new RuntimeException(except);
        }
        return list;
    }

    public Person insert(Person person, Connection connection){
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person(" +
                        "email, password, nick, fullname) VALUES(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, person.getEmail());
            preparedStatement.setString(2, person.getPassword());
            preparedStatement.setString(3, person.getNick());
            preparedStatement.setString(4, person.getFullName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    person.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained");
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);

        }
        return person;
    }


    public Person update(Person person, Connection connection){

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE person SET " +
                "e_mail = ?, password = ?, nick_name = ?, full_name = ? " +
                "WHERE id = ?");)
        {

            preparedStatement.setString(1, person.getEmail());
            preparedStatement.setString(2, person.getPassword());
            preparedStatement.setString(3, person.getNick());
            preparedStatement.setString(4, person.getFullName());
            preparedStatement.setLong(5, person.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            return person;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void delete(Long id, Connection connection){

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person WHERE (person.id = ?)"))
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
}
