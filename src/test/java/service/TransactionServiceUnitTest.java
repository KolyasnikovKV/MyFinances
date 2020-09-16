package service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kolyasnikovkv.myfinances.dao.*;
import ru.kolyasnikovkv.myfinances.dao.domain.Account;
import ru.kolyasnikovkv.myfinances.dao.domain.Category;
import ru.kolyasnikovkv.myfinances.dao.domain.Transaction;
import ru.kolyasnikovkv.myfinances.service.TransactionService;
import ru.kolyasnikovkv.myfinances.service.converters.TransactionConverter;
import ru.kolyasnikovkv.myfinances.service.dto.TransactionDto;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceUnitTest {


    @InjectMocks
    TransactionService subj;
    AccountDao accountDao;
    PersonDao personDao;
    CurrencyDao currencyDao;
    CategoryDao categoryDao;
    @Mock
    TransactionConverter transactionConverter;
    @Mock
    TransactionDao transactionDao;
    @Mock AccountDao accountDaoMock;
    @Mock CategoryDao categoryDaoMock;
    @Mock TransactionDao transactionDaoMock;
    @Mock Connection connectionMock;



    @Test
    public void createNewTransaction() {

        TransactionDto transactionDto = new TransactionDto();
        Transaction transaction = new Transaction();
        transactionDto.setCategoryId(22L);
        transactionDto.setAccountIdFrom(2L);
        Account account = new Account();
        Category category = new Category();

            when(transactionConverter.transactionDtoToTransaction(transactionDto)).thenReturn(transaction);
            when(accountDaoMock.findById(2L, connectionMock)).thenReturn(account);
            when(categoryDaoMock.findById(22L, connectionMock)).thenReturn(category);
            when(transactionDaoMock.insert(transaction, connectionMock)).thenReturn(transaction);
            when(transactionConverter.transactionToTransactionDto(transaction)).thenReturn(transactionDto);


        TransactionDto transactionDtoFromService = subj.createNewTransaction(transactionDto, connectionMock);
        assertEquals(transactionDto, transactionDtoFromService);

    }

    @Test
    public void updateTransaction_transactionDtoIdWrong() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(22L);
        Transaction transaction = new Transaction();

        when(transactionDaoMock.findById(23L, connectionMock)).thenReturn(transaction);
        when(transactionDaoMock.update(transaction, connectionMock)).thenReturn(transaction);
        when(transactionConverter.transactionToTransactionDto(transaction)).thenReturn(transactionDto);
        TransactionDto transactionDtoFromService = subj.updateTransaction(transactionDto, connectionMock);

        assertNotEquals(transactionDto, transactionDtoFromService);
    }

    @Test
    public void updateTransaction_ok() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(22L);
        Transaction transaction = new Transaction();

        when(transactionConverter.transactionToTransactionDto(transaction)).thenReturn(transactionDto);
        when(transactionDaoMock.findById(22L, connectionMock)).thenReturn(transaction);
        when(transactionDaoMock.update(transaction, connectionMock)).thenReturn(transaction);
        TransactionDto transactionDtoFromService = subj.updateTransaction(transactionDto, connectionMock);

        assertEquals(transactionDto, transactionDtoFromService);
    }

/*    @Test
    public void deleteTransaction_ok() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(22L);

        when(transactionDaoMock.delete(22L, connectionMock)).thenReturn(true);
        Boolean deleteTrue = subj.deleteTransaction(transactionDto, connectionMock);

        assertTrue(deleteTrue);
    }

    @Test
    public void deleteTransaction_transactionIdWrong() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(22);

        when(transactionDaoMock.delete(23, connectionMock)).thenReturn(true);
        Boolean deleteCategoryTrue = subj.deleteTransaction(transactionDto, connectionMock);

        assertFalse(deleteCategoryTrue);
    }

*/
    }