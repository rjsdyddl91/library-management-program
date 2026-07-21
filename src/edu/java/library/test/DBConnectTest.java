package edu.java.library.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.java.library.config.MySQLConnInfo;

public class DBConnectTest {

	public static void main(String[] args) {

		Connection conn = null;

		try {

			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			System.out.println("DB 연결 성공!");
			conn.close();

		} catch (SQLException e) {

			System.out.println("DB 연결 실패!");
			e.printStackTrace();

		}
		
	} // main end

} // class end