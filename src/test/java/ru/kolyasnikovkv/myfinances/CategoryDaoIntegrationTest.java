package ru.kolyasnikovkv.myfinances;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.kolyasnikovkv.myfinances.dao.CategoryDao;
import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.service.CategoryService;
import ru.kolyasnikovkv.myfinances.service.converters.CategoryConverter;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class CategoryDaoIntegrationTest {
    CategoryService subj;
    CategoryDao categoryDao = new CategoryDao();
    CategoryConverter categoryConverter = new CategoryConverter();

    @Before
    public void setUp()  {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:testDatabase");//H2 DB работает только в памяти!
        System.setProperty("jdbcUsername", "sa");
        System.setProperty("jdbcPassword", "");

        subj = new CategoryService(categoryDao, categoryConverter);
    }

    @Test
    public void categoryInsert_ok() throws SQLException {

        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();

        categoryDto.setId(-1L);
        categoryDto.setDescription("Наличка");
        assertEquals(-1L, (long) categoryDto.getId()); //ключ ещё не сгенерирован!

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);
        assertNotNull(categoryDto);
        assertNotEquals(-1L, (long)categoryDtoFromService.getId()); // Проверил генерацию ключа
        assertNotEquals(categoryDto.getId(), categoryDtoFromService.getId());
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());
        connection.close();
    }

    @Test
    public void categoryInsert_NameCurrencyWrong() throws SQLException {

        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();


        categoryDto.setId(-1L);
        categoryDto.setDescription("");

        assertEquals(-1L, (long) categoryDto.getId());

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);

        assertNotNull(categoryDto);

        assertNull(categoryDtoFromService);
        connection.close();
    }

    @Test
    public void categoryUpdate_ok() throws SQLException {
        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();


        categoryDto.setId(-1L);
        categoryDto.setDescription("Грвн");

        assertEquals(-1L, (long) categoryDto.getId());

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);

        assertNotNull(categoryDtoFromService);
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());

        categoryDto.setDescription("Рубль");
        categoryDto.setId(categoryDtoFromService.getId());
        categoryDtoFromService = subj.updateCategory(categoryDto, connection);

        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());
        assertEquals("Рубль", categoryDtoFromService.getDescription());
        connection.close();
    }

    @Test
    public void categoryUpdate_idWrong() throws SQLException {
        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();

        categoryDto.setId(-1L);
        categoryDto.setDescription("USD");

        assertEquals(-1L, (long)categoryDto.getId());
        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);

        assertNotNull(categoryDtoFromService);
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());


        categoryDto.setDescription("Euro");
        categoryDto.setId(100L);
        categoryDtoFromService = subj.updateCategory(categoryDto, connection);

        assertNull(categoryDtoFromService);

        connection.close();
    }

    @Test
    public void categoryFindById_ok() throws SQLException {

        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();

        categoryDto.setId(-1L);
        categoryDto.setDescription("USD");

        assertEquals(-1L, (long) categoryDto.getId());

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);

        assertNotNull(categoryDtoFromService);
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());

        categoryDto = categoryConverter.categoryToCategoryDto(
                categoryDao.findById(categoryDtoFromService.getId(), connection));

        assertEquals(categoryDto.getId(), categoryDtoFromService.getId());
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());
    }

    @Test
    public void categoryFindById_idWrong() throws SQLException {

        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();


        categoryDto.setId(-1L);
        categoryDto.setDescription("USD");

        assertEquals(-1L, (long)categoryDto.getId()); //ключ ещё не сгенерирован!

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);

        assertNotNull(categoryDtoFromService);
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());

        categoryDto = categoryConverter.categoryToCategoryDto(
                categoryDao.findById(categoryDto.getId(), connection));

        assertNotEquals(categoryDto, categoryDtoFromService);
    }

    @Test
    public void categoryDelete_ok() throws SQLException {
        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();

        categoryDto.setId(-1L);
        categoryDto.setDescription("USD");

        assertEquals(-1L, (long)categoryDto.getId()); //ключ ещё не сгенерирован!

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);

        assertNotNull(categoryDtoFromService);
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());

        //assertTrue(subj.deleteCategory(categoryDtoFromService, connection));

        connection.close();
    }

    @Test
    public void categoryDelete_idWrong() throws SQLException {
        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();

        categoryDto.setId(-1L);
        categoryDto.setDescription("USD");

        assertEquals(-1L, (long)categoryDto.getId()); //ключ ещё не сгенерирован!

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connection);

        assertNotNull(categoryDtoFromService);
        assertEquals(categoryDto.getDescription(), categoryDtoFromService.getDescription());

        //assertFalse(subj.deleteCategory(categoryDto, connection));

        connection.close();
    }


    @Test
    public void categoryFindAll_ok() throws SQLException {

        CategoryDto categoryDto = new CategoryDto();
        Connection connection = DaoFactory.getConnection();

        categoryDto.setId(-1L);
        categoryDto.setDescription("USD");

        assertEquals(-1L, (long)categoryDto.getId()); //ключ ещё не сгенерирован!

        subj.createNewCategory(categoryDto, connection);
        categoryDto.setDescription("FRT");
        subj.createNewCategory(categoryDto, connection);

        List<Category> list = categoryDao.findAll();

        assertNotNull(list);
        assertEquals(2, list.size());

        connection.close();
    }

    @After
    public void setDown() throws SQLException {

        Connection connection = DaoFactory.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM category;")) {

            preparedStatement.executeUpdate();
            connection.close();


        } catch (SQLException exept) {
            throw new RuntimeException(exept);
        }
    }
}
