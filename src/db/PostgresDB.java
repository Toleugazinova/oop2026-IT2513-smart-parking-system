package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresDB implements IDatabase {

    protected String url;
    protected String user;
    protected String password;

    public PostgresDB(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException("DB connection failed", e);
        }
    }
}