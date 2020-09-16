package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.CategoryDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.service.CategoryService;
import ru.kolyasnikovkv.myfinances.service.converters.CategoryConverter;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;


import java.sql.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService subj;
    @Mock
    CategoryDao categoryDao;
    @Mock
    CategoryConverter categoryConverter;
    @Mock Connection connectionMock;

    @Test
    public void createNewCategory_categoryIsNull() {

        CategoryDto categoryDto = null;

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connectionMock);

        assertNull(categoryDtoFromService);

}

    @Test
    public void createNewCategory_ok() {

        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("dddddd");

        when(categoryConverter.categoryDtoToCategory(categoryDto)).thenReturn(category);
        when(categoryDao.findByDescription(categoryDto.getDescription(), connectionMock)).thenReturn(null);
        when(categoryDao.insert(category, connectionMock)).thenReturn(category);
        when(categoryConverter.categoryToCategoryDto(category)).thenReturn(categoryDto);

        CategoryDto categoryDtoFromService = subj.createNewCategory(categoryDto, connectionMock);
        assertEquals(categoryDto, categoryDtoFromService);
    }

    @Test
    public void updateCategory_ok() {
        Category category = new Category();

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("ddddddddddddd");
        categoryDto.setId(10L);
        category.setId(10L);


        when(categoryConverter.categoryDtoToCategory(categoryDto)).thenReturn(category);
        when(categoryConverter.categoryToCategoryDto(category)).thenReturn(categoryDto);
        when(categoryDao.findById(10L, connectionMock)).thenReturn(category);
        when(categoryDao.update(category, connectionMock)).thenReturn(category);


        CategoryDto categoryDtoFromService = subj.updateCategory(categoryDto, connectionMock);
        assertEquals(categoryDto, categoryDtoFromService);
    }

    @Test
    public void updateCategory_idIsWrong() {
        Category category = new Category();

        CategoryDto categoryDto = new CategoryDto();
        category.setId(10L);

        when(categoryConverter.categoryDtoToCategory(categoryDto)).thenReturn(category);
        when(categoryConverter.categoryToCategoryDto(category)).thenReturn(categoryDto);
        when(categoryDao.findById(11L, connectionMock)).thenReturn(category);



        CategoryDto categoryDtoFromService = subj.updateCategory(categoryDto, connectionMock);
        assertNull(categoryDtoFromService);
    }

    @Test
    public void checkDescription_ok() {
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("bla-bk");

        when(categoryConverter.categoryDtoToCategory(categoryDto)).thenReturn(category);
        when(categoryDao.findByDescription(categoryDto.getDescription(), connectionMock)).thenReturn(null);
        Boolean checkDescriptionTrue = subj.checkDescription(categoryDto, connectionMock);

        assertTrue(checkDescriptionTrue);
    }

   /* @Test
    public void deleteCategory_ok() {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(100L);

        when(categoryDao.delete(100, connectionMock)).thenReturn(true);
        Boolean deleteCategoryTrue = subj.deleteCategory(categoryDto, connectionMock);

                 assertTrue(deleteCategoryTrue);
            }

    @Test
    public void deleteCategory_idIsWrong() {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(100);

        when(categoryDao.delete(22, connectionMock)).thenReturn(true);
        Boolean deleteCategory = subj.deleteCategory(categoryDto, connectionMock);

        assertFalse(deleteCategory);
    }*/
}
