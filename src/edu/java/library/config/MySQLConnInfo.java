package edu.java.library.config;

public interface MySQLConnInfo {
	String URL = "jdbc:mysql://localhost:3306/library_db_jp";
	String USER = "root";
	String PASSWORD = System.getenv("LIBRARY_DB_PASSWORD");

}
