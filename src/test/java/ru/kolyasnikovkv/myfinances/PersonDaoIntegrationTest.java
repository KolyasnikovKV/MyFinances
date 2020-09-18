package ru.kolyasnikovkv.myfinances;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.kolyasnikovkv.myfinances.dao.Dao;
import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.dao.PersonDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.exception.DaoException;
import ru.kolyasnikovkv.myfinances.service.PersonService;
import ru.kolyasnikovkv.myfinances.service.converters.UserConverter;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static ru.kolyasnikovkv.myfinances.dao.DaoFactory.getDataSource;

public class PersonDaoIntegrationTest {
    DataSource dataSource;
    PersonService subj;
    PersonDao personDao;
    UserConverter userConverter = new UserConverter();

    @Before
    public void setUp()  {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:testDatabase");//H2 DB работает только в памяти!
        System.setProperty("jdbcUsername", "sa");
        System.setProperty("jdbcPassword", "");
        dataSource = getDataSource();
        personDao = new PersonDao(dataSource);
        subj = new PersonService(personDao, userConverter);


    }


    @Test
    public void personInsert_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);

        assertNotEquals(-1L, (long) person.getId()); //ключ сгенерирован!

        assertNotNull(personFromDao);
        assertEquals(person.getId(), personFromDao.getId()); // Проверил генерацию ключа
        assertEquals(person, personFromDao);
        connection.close();
    }

    @Test
    public void personInsert_mailWrong() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogorao.ru");
        person.setPassword("pass");
        UserDto personFromDao = subj.createNewPerson(userConverter.personToUserDto(person), connection);


        assertNull(personFromDao);
        connection.close();
    }

    @Test
    public void personUpdate_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);

        assertEquals(person.getEmail(), personFromDao.getEmail());

        personFromDao.setEmail("eee@yu.ru");
        person = personDao.update(personFromDao, connection);

        assertEquals("eee@yu.ru", person.getEmail());



        assertNotEquals(-1L, (long)person.getId()); //ключ сгенерирован!
        assertNotNull(personFromDao);
        connection.close();
    }

    @Test
    public void personUpdate_idWrong() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);

        assertEquals(person.getEmail(), personFromDao.getEmail());

        personFromDao.setEmail("eee@yu.ru");
        personFromDao.setId(personFromDao.getId());
        person = personDao.update(personFromDao, connection);

        //assertNull(person);
        connection.close();
    }

    @Test
    public void personFindById_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);
        System.out.println(personFromDao.getEmail());

        person.setEmail("igogogogogo@aou.ru");
        person = personDao.findById(personFromDao.getId(), connection);

        assertEquals(person.getId(), personFromDao.getId());
        assertEquals("iogor@ao.ru", person.getEmail());
        connection.close();
    }

    @Test
    public void personFindById_idWrong() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);

        person.setEmail("igogogogogo@aou.ru");
        person = personDao.findById(200L, connection);


        assertNull(person);
        connection.close();
    }


    @Test
    public void personFindAll_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        personDao.insert(person, connection);

        Person person2 = new Person();
        person.setEmail("iogorW@ao.ru");
        person.setPassword("pasds");
        personDao.insert(person, connection);




        List<Person> list = personDao.findAll();

        assertNotNull(list);
        assertEquals(2, list.size());
        connection.close();
    }

    /*@Test
    public void personDelete_ok() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        Person personFromDao = personDao.insert(person, connection);



        assertNotNull(personFromDao);

        assertTrue(personDao.delete(personFromDao.getId(), connection));
        connection.close();
    }

    @Test
    public void personDelete_idWrong() throws SQLException {

        Connection connection = DaoFactory.getConnection();

        Person person = new Person();
        person.setEmail("iogor@ao.ru");
        person.setPassword("pass");
        person.setId(10);
        UserDto user = new UserConverter().personToUserDtoConvert(person);

        Person personFromDao = personDao.insert(person, connection);



        assertNotNull(personFromDao);

        assertFalse(subj.deletePerson(user, connection));
        connection.close();
    }*/

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
