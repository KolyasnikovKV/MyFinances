package ru.kolyasnikovkv.myfinances.dao;

public class CategoryDao{
//public class CategoryDao implements Dao<Category, Integer> {


  /*  @Override
    public Category findById(Long id, Connection connection) {
        Category category = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("Select * From category " +
                     "WHERE (category.id = ?)")) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return getCategory(rs, category);
            }
        }
        catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return category;
    }

    @Override
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("Select * From category ");

            while (rs.next()) {
                Category category = new Category();
                list.add(getCategory(rs, category));
            }
        }
        catch (SQLException exept) {
            throw new DaoException(exept);
        }

        return list;
    }

    private Category getCategory(ResultSet rs, Category category) throws SQLException {
        category.setId(rs.getLong(1));
        category.setDescription(rs.getString(2));

        return category;
    }

    @Override
    public Category insert(Category category, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO category(" +
                     "description) VALUES( ? )", Statement.RETURN_GENERATED_KEYS);)
        {

            preparedStatement.setString(1, category.getDescription());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                Long id = rs.getLong(1); //вставленный ключ
                category.setId(id);
            }

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained");
                }
            }

            return category;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Category update(Category category, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE category SET " +
                     "description = ?" +
                     "WHERE category.id = ?");)
        {
            preparedStatement.setLong(2, category.getId());
            preparedStatement.setString(1, category.getDescription());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }


        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }

    @Override
    public void delete(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM category WHERE (category.id = ?)" )) {

            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("Creating transaction failed, no rows affected");
            }

        }
        catch (SQLException exept) {
            throw new DaoException(exept);
        }

    }*/
}
