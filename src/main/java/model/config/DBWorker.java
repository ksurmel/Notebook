package model.config;


import java.sql.*;


public class DBWorker {

    private static final String URL = "jdbc:mysql://localhost:3306/notebook?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "qwerty";

    private Connection connection;
    private static DBWorker dbWorker;

    private DBWorker() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }

    }

    public static DBWorker getInstance(){
        return dbWorker = new DBWorker();
    }

    public Connection getConnection() {
        return connection;
    }
}