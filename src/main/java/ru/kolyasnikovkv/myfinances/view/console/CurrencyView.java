package ru.kolyasnikovkv.myfinances.view.console;

import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.service.CurrencyService;
import ru.kolyasnikovkv.myfinances.service.ServiceFactory;
import ru.kolyasnikovkv.myfinances.service.dto.CurrencyDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyView {
    private final CurrencyService currencyService = ServiceFactory.getCurrencyService();



    public CurrencyDto createNewCurrency(CurrencyDto currencyDto) throws SQLException {

            if (currencyService.checkNameOfCurrency(currencyDto, DaoFactory.getConnection())) {

                currencyDto = currencyService.createNewCurrency(currencyDto, DaoFactory.getConnection());


                if (currencyDto != null) {
                    System.out.println("New currency " + currencyDto.getNameCurrency() + " successfully created!");
                    return currencyDto;
                }

            }

            else {

                System.out.println("Возможно данная валюта " + currencyDto.getNameCurrency() + " уже зарегистрирована!" +
                        "\n Повторная регистрация запрещена!");
                return null;

            }

            System.out.println("Что то пошло не так!");
            return null;
    }

    public CurrencyDto createCurrencyDto() {

        System.out.println("Please print your name of currency and press <Enter>");

        Scanner scanner = new Scanner(System.in);
        String nameCurrency = scanner.nextLine().trim();

        if (validateCurrency(nameCurrency)) {

            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setNameCurrency(nameCurrency);

            return currencyDto;
        }

        return null;
    }

    public boolean validateCurrency(String nameCurrency) {
        if (nameCurrency == null || nameCurrency.length() > 15 || nameCurrency.length()  < 1) {
            System.out.println("name of currency must contain from 1 to 15 letters!");
            return false;
        }

        Pattern p = Pattern.compile("[$]?[a-zA-Zа-яА-Я-_]*");
        Matcher m = p.matcher(nameCurrency);
        return m.matches();
    }

    public List<CurrencyDto> findAllCurrencyDto() {

        List<CurrencyDto> list = currencyService.getAllCurrency();

        if (list != null && list.size() > 0 ) {
            return list;
        }
        return null;
    }


}
