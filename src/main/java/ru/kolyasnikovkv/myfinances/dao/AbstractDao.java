package ru.kolyasnikovkv.myfinances.dao;

import ru.kolyasnikovkv.myfinances.dao.domain.AbstractEntity;
import ru.kolyasnikovkv.myfinances.dao.domain.Person;
import ru.kolyasnikovkv.myfinances.exception.DaoException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.kolyasnikovkv.myfinances.dao.OperationSql.*;

abstract class AbstractDao<Domain extends AbstractEntity, Id> implements Dao<Domain, Id> {

    protected final DataSource dataSource;
    protected final RowMapper<Domain> rowMapper;
    abstract String getSQL(OperationSql type);

    protected AbstractDao(RowMapper<Domain> rowMapper, DataSource dataSource, String tableName) {
        this.rowMapper = rowMapper;
        this.dataSource = dataSource;
    }

    @Override
    public List<Domain> findAll(){

        List<Domain> list = new ArrayList<Domain>();
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(getSQL(SQL_SELECT_ALL));
            while (rs.next()){
                list.add(rowMapper.mapRow(rs, 0));
            }
        }
        catch (SQLException except){
            throw new DaoException(except);
        }
        return list;
    }
    @Override
    public Domain findById(Long id, Connection connection){
        Domain domain = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSQL(SQL_SELECT_ID))) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                domain = rowMapper.mapRow(rs, 0);
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }
        return domain;
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

    public Domain update(Domain domain, Connection connection){

        try (PreparedStatement preparedStatement = connection.prepareStatement(getSQL(SQL_UPDATE)))
        {
            rowMapper.preparedStatement(domain, preparedStatement);
            preparedStatement.setLong(5, domain.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            return domain;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void delete(Long id, Connection connection){

        try (PreparedStatement preparedStatement = connection.prepareStatement(getSQL(SQL_UPDATE)))
        {
            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }
        }
        catch (SQLException except) {
            throw new DaoException(except);
        }
    }

}
