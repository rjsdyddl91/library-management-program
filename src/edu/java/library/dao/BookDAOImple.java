package edu.java.library.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.java.library.config.MySQLConnInfo;
import edu.java.library.vo.BookVO;

public class BookDAOImple implements BookDAO {

	// 도서 등록 기능 구현
	@Override
	public int insert(BookVO vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "INSERT INTO EX_BOOK "
					+ "(TITLE, AUTHOR, PUBLISHER, BOOK_STATUS) "
					+ "VALUES (?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getAuthor());
			pstmt.setString(3, vo.getPublisher());
			pstmt.setString(4, vo.getBookStatus());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	} 
    
	// 도서 전체 조회
	@Override
	public ArrayList<BookVO> selectAll() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<BookVO> list =
				new ArrayList<BookVO>();
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "SELECT * FROM EX_BOOK";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int bookId = rs.getInt("BOOK_ID");
				String title = rs.getString("TITLE");
				String author = rs.getString("AUTHOR");
				String publisher = rs.getString("PUBLISHER");
				String bookStatus = rs.getString("BOOK_STATUS");

				BookVO vo =
						new BookVO(
								bookId,
								title,
								author,
								publisher,
								bookStatus);

				list.add(vo);
				
			}
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public BookVO selectByBookId(
			int bookId) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		BookVO vo = null;

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql =
					"SELECT * FROM EX_BOOK "
					+ "WHERE BOOK_ID = ?";

			pstmt =
					conn.prepareStatement(sql);

			pstmt.setInt(1, bookId);

			rs =
					pstmt.executeQuery();

			if(rs.next()) {

				vo =
						new BookVO(
								rs.getInt("BOOK_ID"),
								rs.getString("TITLE"),
								rs.getString("AUTHOR"),
								rs.getString("PUBLISHER"),
								rs.getString("BOOK_STATUS"));
			}

			rs.close();
			pstmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vo;
	}
    
    @Override
    public ArrayList<BookVO> selectByTitle(String title) {

    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;

    	ArrayList<BookVO> list =
    			new ArrayList<BookVO>();

    	try {
    		conn = DriverManager.getConnection(
    				MySQLConnInfo.URL,
    				MySQLConnInfo.USER,
    				MySQLConnInfo.PASSWORD);

    		String sql =
    				"SELECT * FROM EX_BOOK "
    				+ "WHERE TITLE LIKE ?";

    		pstmt =
    				conn.prepareStatement(sql);

    		pstmt.setString(
    				1,
    				"%" + title + "%");

    		rs =
    				pstmt.executeQuery();

    		while(rs.next()) {

    			int bookId =
    					rs.getInt("BOOK_ID");

    			String bookTitle =
    					rs.getString("TITLE");

    			String author =
    					rs.getString("AUTHOR");

    			String publisher =
    					rs.getString("PUBLISHER");

    			String bookStatus =
    					rs.getString("BOOK_STATUS");

    			BookVO vo =
    					new BookVO(
    							bookId,
    							bookTitle,
    							author,
    							publisher,
    							bookStatus);

    			list.add(vo);
    		}

    		rs.close();
    		pstmt.close();
    		conn.close();

    	} catch (SQLException e) {
    		e.printStackTrace();
    	}

    	return list;
    }

    // 도서 정보 수정 기능 구현
	@Override
	public int update(BookVO vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "UPDATE EX_BOOK "
					+ "SET TITLE = ?, AUTHOR = ?, PUBLISHER = ?, BOOK_STATUS = ? "
					+ "WHERE BOOK_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getAuthor());
			pstmt.setString(3, vo.getPublisher());
			pstmt.setString(4, vo.getBookStatus());
			pstmt.setInt(5, vo.getBookId());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// BOOK_ID로 도서 삭제 기능 구현
	@Override
	public int delete(int bookId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "DELETE FROM EX_BOOK "
					+ "WHERE BOOK_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookId);
			result = pstmt.executeUpdate();

			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	


	

} // class end