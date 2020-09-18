package ru.kolyasnikovkv.myfinances.dao;

import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.exception.DaoException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDao extends AbstractDao<Person, Integer> {

    protected final RowMapper<Person> rowMapper;
    private final static String TABLE_NAME = "person";

    public PersonDao(DataSource dataSource){
        super(new PersonRowMapper(), dataSource, TABLE_NAME);
        rowMapper = new PersonRowMapper();
        //this.dataSource = dataSource;
   }

    @Override
    String getSQL(OperationSql type){
        switch (type) {
            case SQL_SELECT_ID: {
                return "Select * From person WHERE (person.id =  ?)";
            }
            case SQL_SELECT_ALL: {
                return "SELECT * FROM person";
            }
            case SQL_INSERT: {
                return "INSERT INTO person(email, password, nick, fullname) VALUES(?, ?, ?, ?)";
            }
            case SQL_UPDATE: {
                return "UPDATE person SET email = ?, password = ?, nick = ?, fullname = ? WHERE id = ?";
            }
            case SQL_DELETE: {
                return "DELETE FROM person WHERE (person.id = ?)";
            }
            default: return "";
        }
    }

    private static class PersonRowMapper implements RowMapper<Person> {
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getLong("id"));
            person.setEmail(rs.getString("email"));
            person.setPassword(rs.getString("password"));
            person.setNick(rs.getString("nick"));
            person.setFullName(rs.getString("fullName"));
            return person;
        }

        public void preparedStatement(Person person, PreparedStatement preparedStatement) throws SQLException{
            preparedStatement.setString(1, person.getEmail());
            preparedStatement.setString(2, person.getPassword());
            preparedStatement.setString(3, person.getNick());
            preparedStatement.setString(4, person.getFullName());
        }
    }

    public Person findByNickAndPassword(String nick, String password, Connection connection) {

        Person person = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From person " +
                "WHERE (UPPER(person.nick_name) = UPPER(?) and person.password = ?)")) {

            preparedStatement.setString(1, nick);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                person = rowMapper.mapRow(rs, 0);
            }

            if (person == null) {
                throw new SQLException("Not found.");
            }
        }
        catch (SQLException exept) {
            throw new DaoException(exept);
        }
        return null;
    }

    public Person findByMail(String mail, Connection connection) {
        Person person = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From person " +
                "WHERE UPPER(person.e_mail) = UPPER(?)"))
        {

            preparedStatement.setString(1, mail);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                person = rowMapper.mapRow(rs, 0);
            }

        }
        catch (SQLException except) {
            throw new DaoException(except);
        }
        return person;
    }


    /*public Person insert(Person person, Connection connection){
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

    public Person findById(Long id, Connection connection){
        Person person = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From person "  +
                "WHERE (person.id =  ?)")) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                person = rowMapper.mapRow(rs, 0);
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }
        return person;
    }*/

    /*public List<Person> findAll(){

        List<Person> list = new ArrayList<Person>();
        try(Connection connection = super.dataSource.getConnection();
        Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT * FROM person");
            while (rs.next()){
                Person person = new Person();
                list.add(rowMapper.mapRow(rs, 0));
            }
        }
        catch (SQLException except){
            throw new DaoException(except);
        }
        return list;
    }*/

    /*public Person update(Person person, Connection connection){

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
        catch (SQLException except) {
            throw new DaoException(except);
        }
    }
*/
}
