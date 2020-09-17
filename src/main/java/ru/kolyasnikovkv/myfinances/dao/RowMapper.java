package ru.kolyasnikovkv.myfinances.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<Domain> {
    Domain mapRow(ResultSet rs, int rowNum) throws SQLException;
    void preparedStatement(Domain domain, PreparedStatement preparedStatement) throws SQLException;
}
