package ru.kolyasnikovkv.myfinances.service.converters;

import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryConverter {

    public Category categorieDtoToCategory(CategoryDto categorieDto) {

        if (categorieDto != null) {
            Category categorie = new Category();
            categorie.setId(categorieDto.getId());
            categorie.setDescription(categorieDto.getDescription());
            return categorie;
        }
        return null;
    }


    public CategoryDto categorieToCategoryDto(Category categorie) {

        if (categorie != null) {
            CategoryDto categorieDto = new CategoryDto();
            categorieDto.setId(categorie.getId());
            categorieDto.setDescription(categorie.getDescription());
            return categorieDto;
        }
        return null;
    }

    public List<CategoryDto> listCategoryToListCategoryDto(List<Category> categorie) {

        if (categorie == null) {
            return null;
        }

        List<CategoryDto> listCategoryDto = new ArrayList<>();
        CategoryConverter converter = new CategoryConverter();

        for (Category cat : categorie) {
            listCategoryDto.add(converter.categorieToCategoryDto(cat));
        }

        return listCategoryDto;
    }



}
