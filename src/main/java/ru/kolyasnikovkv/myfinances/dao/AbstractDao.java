package ru.kolyasnikovkv.myfinances.dao;

import ru.kolyasnikovkv.myfinances.dao.domain.AbstractEntity;
import ru.kolyasnikovkv.myfinances.exception.DaoException;

import java.sql.*;

import static ru.kolyasnikovkv.myfinances.dao.OperationSql.*;

abstract class AbstractDao<Domain extends AbstractEntity, Id> implements Dao<Domain, Id> {

    protected final RowMapper<Domain> rowMapper;
    abstract String getSQL(OperationSql type);

    protected AbstractDao(RowMapper<Domain> rowMapper, String tableName) {
        this.rowMapper = rowMapper;
    }

    @Override
    public Domain findById(Long id, Connection connection){
        Domain person = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSQL(SQL_SELECT_ID))) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                person = rowMapper.mapRow(rs, 0);
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }
        return person;
    }


    @Override
    public Domain insert(Domain domain, Connection connection) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSQL(SQL_INSERT),
                Statement.RETURN_GENERATED_KEYS)) {

            rowMapper.preparedStatement(domain, preparedStatement);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    domain.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained");
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);

        }
        return domain;
    }

}
