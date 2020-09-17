package ru.kolyasnikovkv.myfinances.view.console;

import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.service.ServiceFactory;
import ru.kolyasnikovkv.myfinances.service.TransactionService;
import ru.kolyasnikovkv.myfinances.service.dto.AccountDto;
import ru.kolyasnikovkv.myfinances.service.dto.CategoryDto;
import ru.kolyasnikovkv.myfinances.service.dto.TransactionDto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TransactionView {
    private Scanner scanner = new Scanner(System.in);
    private final TransactionService transactionService = ServiceFactory.getTransactionService();

    private String now() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        return dateFormat.format(new Date());
    }



    public TransactionDto createNewTransaction(TransactionDto transactionDto) throws SQLException {

        if (transactionService.createNewTransaction(transactionDto, DaoFactory.getConnection()) != null) {
            System.out.println("New account number =" + transactionDto.getId() + " successfully created!");
            return transactionDto;

        } else {
            System.out.println("Ошибка записи транзакции в БД!");
            return null;
        }
    }

    public TransactionDto createTransactionDto(AccountDto accountDto) throws SQLException {

        System.out.println("Please print your sum and press <Enter>");
        BigDecimal sum = scanner.nextBigDecimal();

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountIdFrom(accountDto.getId());
        transactionDto.setDate(now());
        accountDto.setId(-11L);

        while (true) {
            //Категория транзакции
                System.out.println("Please print your account description and press <Enter>");
             //Вывести список имеющихся категорий транзакци
            // Либо создать НОВУЮ
                        List<CategoryDto> list = new CategoryView().findAllCategoryDto();
                        int count = 1;
                        for (CategoryDto category : list) {
                            System.out.println("please press " + count + " for choose " + category.getDescription());
                        }
                        System.out.println("please press 0 for create new category description ");
                        System.out.println("for exit press q or Q");
                        String value = scanner.nextLine().trim();

                        if (value.equalsIgnoreCase("q")) {
                            System.out.println("Вы выбрали завершение операции!");
                            return null;
                        }
                        else {
                            int number = Integer.parseInt(value);
                            if (number == 0) {
                                //Создать Описание!

                                CategoryView categoryView = new CategoryView();
                                CategoryDto categoryDto = new CategoryView().createCategoryDto();
                                categoryDto = categoryView.createNewCategory(categoryDto);

                                if (categoryDto != null) {
                                    transactionDto.setCategoryId(categoryDto.getId());
                                    return transactionDto;
                                }
                            }
                            else {
                                transactionDto.setCategoryId(list.get( number - 1).getId());
                                return transactionDto;
                            }
                        }
                }

       }

    public List<TransactionDto> ShowAccount(AccountDto accountDto) throws SQLException {

        return transactionService.findTransactionByAccountId(accountDto, DaoFactory.getConnection());
    }

}

