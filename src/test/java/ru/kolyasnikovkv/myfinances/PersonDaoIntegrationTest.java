package ru.kolyasnikovkv.myfinances;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.kolyasnikovkv.myfinances.dao.Dao;
import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class PersonDaoIntegrationTest {
    DataSource dataSource;
    PersonDao personDao;

    @Before
    public void setUp()  {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:testDatabase");//H2 DB работает только в памяти!
        System.setProperty("jdbcUsername", "sa");
        System.setProperty("jdbcPassword", "");
        dataSource = DaoFactory.getDataSource();
        personDao = new PersonDao(dataSource);
    }

    @Test
    public void personInsert_ok() throws SQLException{

        Connection connection = dataSource.getConnection();
        Person person = new Person();
        person.setEmail("kk@mail.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);

        assertNotEquals(-1L, (long) person.getId()); //ключ сгенерирован!
        assertNotNull(personFromDao);
        assertEquals(person.getId(), personFromDao.getId()); // Проверил генерацию ключа
        assertEquals(person, personFromDao);
        connection.close();
    }

    @Test
    public void personFindById_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("kk@mail.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);
        person = personDao.findById(personFromDao.getId(), connection);
        assertEquals("kk@mail.ru", person.getEmail());
        connection.close();
    }

    @Test
    public void personFindAll_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();
        Person person = new Person();
        person.setEmail("kk@mail.ru");
        person.setPassword("pass");
        personDao.insert(person, connection);

        Person person2 = new Person();
        person.setEmail("kk2@mail.ru");
        person.setPassword("pasds");
        personDao.insert(person, connection);

        List<Person> list = personDao.findAll();

        assertNotNull(list);
        assertEquals(2, list.size());
        connection.close();
    }

    @Test
    public void personDelete_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("kk@mail.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);
        assertNotNull(personFromDao);
        personDao.delete(personFromDao.getId(), connection);
        person = personDao.findById(personFromDao.getId(), connection);
        assertNull(person);
        connection.close();
    }

    @Test
    public void personDelete_idWrong() throws SQLException {

    }

    @After
    public void setDown() throws SQLException {

        Connection connection = DaoFactory.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person;")) {

            preparedStatement.executeUpdate();
            connection.close();

        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }
    }
}
