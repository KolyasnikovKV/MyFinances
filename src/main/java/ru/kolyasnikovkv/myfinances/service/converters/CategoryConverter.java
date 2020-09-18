package ru.kolyasnikovkv.myfinances.service.converters;

import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryConverter {

    public Category categoryDtoToCategory(CategoryDto categoryDto) {

        if (categoryDto != null) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setDescription(categoryDto.getDescription());
            return category;
        }
        return null;
    }


    public CategoryDto categoryToCategoryDto(Category category) {

        if (category != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setDescription(category.getDescription());
            return categoryDto;
        }
        return null;
    }

    public List<CategoryDto> listCategoryToListCategoryDto(List<Category> category) {

        if (category == null) {
            return null;
        }

        List<CategoryDto> listCategoryDto = new ArrayList<>();
        CategoryConverter converter = new CategoryConverter();

        for (Category cat : category) {
            listCategoryDto.add(converter.categoryToCategoryDto(cat));
        }

        return listCategoryDto;
    }



}
