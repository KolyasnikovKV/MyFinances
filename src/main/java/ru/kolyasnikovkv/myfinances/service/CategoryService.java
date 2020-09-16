package ru.kolyasnikovkv.myfinances.service;


import ru.kolyasnikovkv.myfinances.dao.CategoryDao;
import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.service.converters.CategoryConverter;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;

import java.sql.Connection;
import java.util.List;

public class CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryConverter categoryConverter;

    public CategoryService(CategoryDao categoryDao, CategoryConverter categoryConverter) {
        this.categoryDao = categoryDao;
        this.categoryConverter = categoryConverter;
    }

    public CategoryDto createNewCategory(CategoryDto categoryDto, Connection connection) {
        if (checkDescription(categoryDto, connection)) {
            Category category = categoryConverter.categoryDtoToCategory(categoryDto);
            category = categoryDao.insert(category, connection);
            categoryDto = categoryConverter.categoryToCategoryDto(category);
            return categoryDto;
        }
        return null;
    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Connection connection) {
        Category category;

        category = categoryDao.findById(categoryDto.getId(), connection);
        if (category != null && checkDescription(categoryDto, connection)) {
                category = categoryConverter.categoryDtoToCategory(categoryDto);
                category = categoryDao.update(category, connection);
                return categoryConverter.categoryToCategoryDto(category);
        }
        return null;
    }

    public List<CategoryDto> getAllCategory() {
        return  categoryConverter.listCategoryToListCategoryDto(categoryDao.findAll());
    }

    public boolean checkDescription(CategoryDto categoryDto, Connection connection) {

        if (categoryDto != null && categoryDto.getDescription().length() >= 2 ) {
            //Category category = categoryConverter.categoryDtoToCategory(categoryDto);
            Category category = categoryDao.findByDescription(categoryDto.getDescription(), connection);

            if (category != null) {
                return false;
            }
            return true;
        }
        return false;
    }

    public void deleteCategory(CategoryDto categoryDto, Connection connection) {
        if (categoryDto != null) {
           categoryDao.delete(categoryDto.getId(), connection);
        }

    }
}
