package com.ordersmanagement.DataAccessLayer;

import com.ordersmanagement.Connection.ConnectionFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Constructor that determines the class type of the generic parameter T.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        type = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * Converts a camelCase string to snake_case.
     *
     * @param camelCase input camelCase string
     * @return snake_case formatted string
     */
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    /**
     * Creates a SELECT query string to find a record by a given field.
     *
     * @param field the field name to filter by
     * @return the SQL SELECT query string
     */
    private String createSelectQuery(String field) {
        return "SELECT * FROM " + getTableName() + " WHERE " + toSnakeCase(field) + " = ?";
    }

    /**
     * Retrieves all records of type T from the database.
     *
     * @return a list of all T objects from the database
     */
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + getTableName();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();

                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = resultSet.getObject(toSnakeCase(field.getName()));
                    field.set(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.warning("findAll failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return list;
    }

    /**
     * Creates a list of objects from a ResultSet by mapping each row to an instance of T.
     *
     * @param resultSet the ResultSet to convert
     * @return a list of T objects
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        Constructor<?>[] ctors = type.getDeclaredConstructors();
        Constructor<?> ctor = null;
        for (Constructor<?> c : ctors) {
            if (c.getGenericParameterTypes().length == 0) {
                ctor = c;
                break;
            }
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(toSnakeCase(fieldName));
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inserts a new record of type T into the database.
     * Sets the generated ID back into the object if insertion is successful.
     *
     * @param t the object to insert
     * @return the inserted object with updated ID, or null if insertion failed
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Field[] tfields = type.getDeclaredFields();

        try {
            for (Field field : tfields) {
                if (!field.getName().equalsIgnoreCase("id")) {
                    field.setAccessible(true);
                    fields.append(toSnakeCase(field.getName())).append(",");
                    values.append("?,");
                }
            }

            fields.setLength(fields.length() - 1);
            values.setLength(values.length() - 1);

            String query = "INSERT INTO " + getTableName() + " (" + fields + ") VALUES (" + values + ")";

            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int index = 1;
            for (Field field : tfields) {
                if (!field.getName().equalsIgnoreCase("id")) {
                    field.setAccessible(true);
                    statement.setObject(index++, field.get(t));
                }
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.warning("Insert failed: no rows affected.");
                return null;
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                Field idField = type.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, generatedId);
            } else {
                LOGGER.warning("Insert failed: no ID obtained.");
                return null;
            }

        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.warning("Insert failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            ConnectionFactory.close(generatedKeys);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

    /**
     * Deletes the record of type T from the database based on its ID.
     *
     * @param t the object to delete
     * @return the deleted object, or null if deletion failed
     */
    public T delete(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();

            String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
            Object idValue = null;

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    idValue = field.get(t);
                    break;
                }
            }

            if (idValue == null) {
                LOGGER.warning("Delete failed: No 'id' field found or value is null.");
                return null;
            }

            statement = connection.prepareStatement(query);
            statement.setObject(1, idValue);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Delete successful.");
            } else {
                LOGGER.warning("Delete failed. No records deleted.");
            }

        } catch (SQLException | IllegalAccessException e) {
            LOGGER.warning("Delete failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

    /**
     * Updates the record of type T in the database based on its ID.
     *
     * @param t the object with updated data
     * @return the updated object
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();

            StringBuilder setClause = new StringBuilder();
            Field[] tfields = t.getClass().getDeclaredFields();
            String idFieldName = null;
            Object idValue = null;

            for (Field field : tfields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(t);

                if (fieldName.equals("id")) {
                    idFieldName = fieldName;
                    idValue = fieldValue;
                    continue;
                }

                setClause.append(toSnakeCase(fieldName)).append(" = ?, ");
            }

            setClause.setLength(setClause.length() - 2);

            String query = "UPDATE " + getTableName() + " SET " + setClause + " WHERE " + toSnakeCase(idFieldName) + " = ?";

            statement = connection.prepareStatement(query);

            int index = 1;
            for (Field field : tfields) {
                field.setAccessible(true);
                if (!field.getName().equals(idFieldName)) {
                    statement.setObject(index++, field.get(t));
                }
            }

            statement.setObject(index, idValue);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Update successful.");
            } else {
                LOGGER.warning("Update failed. No records updated.");
            }

        } catch (SQLException | IllegalAccessException e) {
            LOGGER.warning("Update failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

    /**
     * Gets the table name in the database that corresponds to the class type T.
     * Handles special cases for reserved words or compound table names.
     *
     * @return the database table name as a string
     */
    protected String getTableName() {
        if (type.getSimpleName().equals("Order")) {
            return "orders"; // reserved word in SQL
        } else if (type.getSimpleName().equals("OrderItem")) {
            return "order_item"; // special case mapping
        }
        return type.getSimpleName().toLowerCase();
    }

}
