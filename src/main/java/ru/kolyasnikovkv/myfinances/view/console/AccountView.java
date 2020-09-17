package ru.kolyasnikovkv.myfinances.view.console;

import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.service.AccountService;
import ru.kolyasnikovkv.myfinances.service.ServiceFactory;
import ru.kolyasnikovkv.myfinances.service.dto.AccountDto;
import ru.kolyasnikovkv.myfinances.service.dto.CurrencyDto;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountView {
    private final AccountService accountService = ServiceFactory.getAccountService();
    private Scanner scanner = new Scanner(System.in);
    private Connection connection = DaoFactory.getConnection();

    public AccountView() throws SQLException {
    }


    public AccountDto createNewAccount(AccountDto accountDto) {


        if (accountService.createNewAccount(accountDto, connection) != null) {
            System.out.println("New account number = " + accountDto.getNumberAccount() + " successfully created!");
            return accountDto;

        } else {
               System.out.println("Не удалось создать счёт! Проверьте данные! ");
               return null;
        }
    }


        public AccountDto createAccountDto(UserDto userDto) throws SQLException {

            if (userDto == null) {
                System.out.println("Невозможно создать счёт для неизвестного пользователя!");
                return null;
            }

            System.out.println("Please print your number account and press <Enter>");
            Long numberAccount = scanner.nextLong();
            AccountDto accountDto = new AccountDto();
            accountDto.setPersonId(userDto.getId());
            accountDto.setBalance(BigDecimal.valueOf(0));
            accountDto.setId(-11L);

            while (true) {
                if (validateNumberAccount(numberAccount)) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Please print your account description and press <Enter>");
                    String description = scanner.nextLine().trim();
                    accountDto.setNumberAccount(numberAccount);


                    while (true) {
                        if (validateDescription(description)) {
                            accountDto.setDescription(description);

                            //Вывести список имеющихся валют
                            // Либо создать свою валюту
                            List<CurrencyDto> list = new CurrencyView().findAllCurrencyDto();
                            int count = 0;
                            if (list == null) {
                                //Создаю валюту
                                CurrencyView currencyView = new CurrencyView();
                                CurrencyDto currencyDto = currencyView.createCurrencyDto();
                                CurrencyDto currency = currencyView.createNewCurrency(currencyDto);

                                if (currency != null) {
                                    accountDto.setCurrencyId(currency.getId());
                                    return accountDto;
                                }
                            }
                            for (CurrencyDto currency : list) {
                                count++;
                                System.out.println("please press " + count + " for choose " + currency.getNameCurrency());
                            }
                            System.out.println("please press 0 for create new currency ");
                            System.out.println("for exit press q or Q");
                            String value = scanner.nextLine().trim();

                            if (value.equalsIgnoreCase("q")) {
                                System.out.println("Вы выбрали завершение операции!");
                                return null;
                            }
                            else if (value.equals("0")) {
                                    //Создать новую Валюту!

                                    CurrencyView currencyView = new CurrencyView();
                                    CurrencyDto currencyDto = currencyView.createCurrencyDto();
                                    CurrencyDto currency = currencyView.createNewCurrency(currencyDto);

                                    if (currency != null) {
                                        accountDto.setCurrencyId(currency.getId());
                                        return accountDto;
                                    }
                                }
                            else {
                                int n = Integer.parseInt(value);
                                accountDto.setCurrencyId(list.get(n - 1).getId());
                                return accountDto;
                            }
                        }
                        else {
                            System.out.println("Our account description is not valid. Please try again");
                            System.out.println("for exit press q or Q");
                            description = scanner.nextLine().trim();

                            if (description.equalsIgnoreCase("q")) {
                                return null;
                            }
                        }
                    }

                } else {
                    System.out.println("Our number account is not valid. Please try again");
                    System.out.println("for exit press 0");
                    numberAccount = scanner.nextLong();

                    if (numberAccount == 0) {
                        return null;
                    }
                }
            }
        }

        private boolean validateNumberAccount(Long numberAccount) {
        if (String.valueOf(numberAccount).length() == 8) { //Номер счета 8 цифр всегда, можно заморочиться и проверить
            // префикс или сделать 12 и т д

            return true;
        }
        System.out.println("Number account must contain from 8 numbers!");
        return false;
    }


        private boolean validateDescription(String description) {

        Pattern p = Pattern.compile("[A-Za-zА-Яа-я]+");
        Matcher m = p.matcher(description);
        return m.matches();

    }

        public List<AccountDto> ShowAccount(UserDto userDto) {

         return accountService.findByPesonId(userDto, connection);
    }







}
