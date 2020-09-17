package ru.kolyasnikovkv.myfinances.view.console;

import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.service.CategoryService;
import ru.kolyasnikovkv.myfinances.service.ServiceFactory;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CategoryView {
    private final CategoryService categoryService = ServiceFactory.getCategoryService();

    public CategoryDto createNewCategory(CategoryDto categoryDto) throws SQLException {

        if (categoryService.checkDescription(categoryDto, DaoFactory.getConnection())) {
            System.out.println("The same DESCRIPTION already exist....");
            return null;
        }
        else {
            categoryDto = categoryService.createNewCategory(categoryDto, DaoFactory.getConnection());

            if (categoryDto != null) {

                System.out.println("New description " + categoryDto.getDescription() + " successfully created!");
                return categoryDto;
            }
        }

            System.out.println("Что-то пошло не так...");
            return null;
    }

    public CategoryDto createCategoryDto() {

        System.out.println("Please print your descripton and press <Enter>");
        Scanner scanner = new Scanner(System.in);
        String description = scanner.nextLine().trim();

        if (validateDescription(description)) {

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setDescription(description);
            categoryDto.setId(-11L);

            return categoryDto;

        }

        return null;
    }

    public boolean validateDescription(String description) {
        if (description != null && description.length() < 25 && description.length() > 4) {
            System.out.println("Description must contain from 4 to 25 letters!");
            return false;
        }
        return true;
    }

    public List<CategoryDto> findAllCategoryDto() {

        List<CategoryDto> list = categoryService.getAllCategory();

        if (list != null && list.size() >0 ) {
            return list;
        }
        return null;
    }


}
