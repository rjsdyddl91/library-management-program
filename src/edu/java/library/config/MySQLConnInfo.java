package edu.java.library.config;

public interface MySQLConnInfo {

    String URL = System.getenv().getOrDefault(
            "LIBRARY_DB_URL",
            "jdbc:mysql://localhost:3306/library_db_jp"
    );

    String USER = System.getenv().getOrDefault(
            "LIBRARY_DB_USER",
            "root"
    );

    String PASSWORD = System.getenv("LIBRARY_DB_PASSWORD");
}