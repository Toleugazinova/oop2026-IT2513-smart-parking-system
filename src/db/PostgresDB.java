package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresDB implements IDatabase {
    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require",
                    "postgres.ycdxqgmrsyzfgojsbucb",
                    "Ne0DvSdLpK4URYNQ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}