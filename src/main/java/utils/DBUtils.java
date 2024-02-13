package utils;

import configurations.Config;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {

    private static final String SQL_GET_USER = "SELECT * from trello.user_data WHERE name = '%s'";


    private static Connection getConnectToDB() throws SQLException {
        return DriverManager.getConnection(
                Config.baseConfigurations().getDbUrl(),
                Config.baseConfigurations().getDbLogin(),
                Config.baseConfigurations().getDbPassword());
    }

    private static List<Map<String, Object>> getTable(String sql) {

        try (Connection connection = getConnectToDB(); Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Map<String, Object>> list = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    map.put(columnName, resultSet.getObject(i));
                }
                list.add(map);
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getUserData(String userName) {
        List<Map<String, Object>> temp = DBUtils.getTable(String.format(SQL_GET_USER, userName));
        Map<String, String> map = new HashMap<>();
        map.put("key", EncryptorUtils.encrypt(temp.get(0).get("key").toString()));
        map.put("token", EncryptorUtils.encrypt(temp.get(0).get("token").toString()));
        return map;
    }

}
