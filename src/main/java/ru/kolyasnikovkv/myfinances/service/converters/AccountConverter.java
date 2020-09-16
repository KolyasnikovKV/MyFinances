package ru.kolyasnikovkv.myfinances.service.converters;

import ru.kolyasnikovkv.myfinances.dao.domain.Account;
import ru.kolyasnikovkv.myfinances.service.dto.AccountDto;

import java.util.ArrayList;
import java.util.List;

public class AccountConverter {

    public Account accountDtoToAccount(AccountDto accountDto) {

        if (accountDto != null) {

            Account account = new Account();
            Long id = accountDto.getId();

            account.setBalance(accountDto.getBalance());
            account.setId(id);
            account.setNumberAccount(accountDto.getNumberAccount());
            account.setPerson(accountDto.getPersonId());
            account.setCurrency(accountDto.getCurrencyId());
            account.setDescription(accountDto.getDescription());

            return account;
        }

        return null;
    }

    public AccountDto accountToAccountDto(Account account) {

        if (account != null) {
            AccountDto accountDto = new AccountDto();

            accountDto.setId(account.getId());
            accountDto.setNumberAccount(account.getNumberAccount());
            accountDto.setPersonId(account.getPerson());
            accountDto.setBalance(account.getBalance());
            accountDto.setCurrencyId(account.getCurrency());
            accountDto.setDescription(account.getDescription());

            return accountDto;
        }
        return null;
    }

    public List<AccountDto> listAccountToListAccountDto(List<Account> account) {

        if (account == null) {
            return null;
        }

        List<AccountDto> listAccountDto = new ArrayList<>();
        AccountConverter converter = new AccountConverter();

        for (Account a : account) {

            listAccountDto.add(converter.accountToAccountDto(a));
        }

        return listAccountDto;
    }

}
