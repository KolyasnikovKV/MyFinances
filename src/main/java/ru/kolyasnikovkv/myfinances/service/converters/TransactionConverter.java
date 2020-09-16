package ru.kolyasnikovkv.myfinances.service.converters;

import ru.kolyasnikovkv.myfinances.dao.domain.Transaction;
import ru.kolyasnikovkv.myfinances.service.dto.TransactionDto;

import java.util.ArrayList;
import java.util.List;

public class TransactionConverter {

    public Transaction transactionDtoToTransaction(TransactionDto transactionDto) {

        if (transactionDto != null) {
            Transaction transaction = new Transaction();
            transaction.setId(transactionDto.getId());
            transaction.setAccountFrom(transactionDto.getAccountIdFrom());
            transaction.setAccountTo(transactionDto.getAccountIdTo());
            transaction.setAmmount(transactionDto.getAmmount());
            transaction.setDate(transactionDto.getDate());
            transaction.setCategory(transactionDto.getCategoryId());
            return transaction;
        }
        return null;
    }

    public TransactionDto transactionToTransactionDto(Transaction transaction) {

        if (transaction != null) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setAccountIdFrom(transaction.getAccountFrom());
            transactionDto.setAccountIdTo(transaction.getAccountTo());
            transactionDto.setAmmount(transaction.getAmmount());
            transactionDto.setDate(transaction.getDate());
            transactionDto.setCategoryId(transaction.getCategory());
            return transactionDto;
        }
        return null;
    }

    public List<TransactionDto> listTransactionToListTransactionDto(List<Transaction> transactions) {

        if (transactions == null) {
            return null;
        }

        List<TransactionDto> listTransactionDto = new ArrayList<>();
        TransactionConverter converter = new TransactionConverter();

        for (Transaction tran : transactions) {
            listTransactionDto.add(converter.transactionToTransactionDto(tran));
        }
        return listTransactionDto;
    }



}
