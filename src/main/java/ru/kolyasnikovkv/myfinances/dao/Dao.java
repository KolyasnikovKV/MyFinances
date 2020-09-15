package ru.kolyasnikovkv.myfinances.dao;

import java.sql.Connection;
import java.util.List;

public interface Dao<Domain, Id> {
    Domain findById(Long id, Connection connection);
    List<Domain> findAll();
    Domain insert(Domain domain, Connection connection);
    Domain update(Domain domain, Connection connection);
    void delete(Long id, Connection connection);
}
