package edu.java.library.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.java.library.config.MySQLConnInfo;
import edu.java.library.vo.OverdueJoinVO;
import edu.java.library.vo.RentalJoinVO;
import edu.java.library.vo.RentalVO;

public class RentalDAOImple implements RentalDAO {

	// 도서 대여
	@Override
	public int rentBook(RentalVO vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String memberSql =
					"SELECT * FROM EX_MEMBER "
					+ "WHERE MEMBER_ID = ?";

			pstmt = conn.prepareStatement(memberSql);

			pstmt.setInt(1, vo.getMemberId());

			rs = pstmt.executeQuery();

			if(!rs.next()) {

				System.out.println("존재하지 않는 회원입니다.");

				rs.close();
				pstmt.close();
				conn.close();

				return 0;
			}
			
			String checkSql =
					"SELECT BOOK_STATUS "
					+ "FROM EX_BOOK "
					+ "WHERE BOOK_ID = ?";

			pstmt = conn.prepareStatement(checkSql);

			pstmt.setInt(1, vo.getBookId());
			
			rs = pstmt.executeQuery();

			if(rs.next()) {

				String bookStatus = rs.getString("BOOK_STATUS");

				if(!bookStatus.equals("대여가능")) {
					System.out.println("이미 대여중인 도서입니다.");

					rs.close();
					pstmt.close();
					conn.close();

					return 0;
				}
			}
			
			Date rentalDate = new Date(System.currentTimeMillis());

			Date dueDate =
					new Date(
							System.currentTimeMillis()
							+ (14L * 24 * 60 * 60 * 1000));
//                 			- (1L * 24 * 60 * 60 * 1000));    // 연체 테스트용
			
			String rentalSql = "INSERT INTO EX_RENTAL "
					+ "(MEMBER_ID, BOOK_ID, RENTAL_DATE, DUE_DATE, RETURN_DATE, RENTAL_STATUS) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(rentalSql);

			pstmt.setInt(1, vo.getMemberId());
			pstmt.setInt(2, vo.getBookId());
			pstmt.setDate(3, rentalDate);
			pstmt.setDate(4, dueDate);
			pstmt.setDate(5, null);
			pstmt.setString(6, "대여중");

			result = pstmt.executeUpdate();
		
			String bookSql = "UPDATE EX_BOOK "
					+ "SET BOOK_STATUS = ? "
					+ "WHERE BOOK_ID = ?";

			pstmt = conn.prepareStatement(bookSql);

			pstmt.setString(1, "대여중");
			pstmt.setInt(2, vo.getBookId());

			pstmt.executeUpdate();
			
			rs.close();
			pstmt.close();
			conn.close();

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// return book
	@Override
	public int returnBook(int rentalId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String checkSql = "SELECT BOOK_ID FROM EX_RENTAL "
					+ "WHERE RENTAL_ID = ? AND RENTAL_STATUS = ?";

			pstmt = conn.prepareStatement(checkSql);

			pstmt.setInt(1, rentalId);
			pstmt.setString(2, "대여중");

			rs = pstmt.executeQuery();
			
			int bookId = 0;

			if(rs.next()) {
				bookId = rs.getInt("BOOK_ID");
			} else {
				System.out.println("반납할 대여 정보가 없습니다.");

				rs.close();
				pstmt.close();
				conn.close();

				return 0;
			}
			
			Date returnDate =
					new Date(System.currentTimeMillis());

			String rentalSql = "UPDATE EX_RENTAL "
					+ "SET RETURN_DATE = ?, RENTAL_STATUS = ? "
					+ "WHERE RENTAL_ID = ?";

			pstmt = conn.prepareStatement(rentalSql);

			pstmt.setDate(1, returnDate);
			pstmt.setString(2, "반납완료");
			pstmt.setInt(3, rentalId);

			result = pstmt.executeUpdate();
			
			String bookSql = "UPDATE EX_BOOK "
					+ "SET BOOK_STATUS = ? "
					+ "WHERE BOOK_ID = ?";

			pstmt = conn.prepareStatement(bookSql);

			pstmt.setString(1, "대여가능");
			pstmt.setInt(2, bookId);

			pstmt.executeUpdate();
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result;
		
	}

	// 전체 대여 목록 조회
	@Override
	public ArrayList<RentalVO> selectAll() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<RentalVO> list =
				new ArrayList<RentalVO>();

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql = "SELECT * FROM EX_RENTAL";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while(rs.next()) {

				int rentalId = rs.getInt("RENTAL_ID");
				int memberId = rs.getInt("MEMBER_ID");
				int bookId = rs.getInt("BOOK_ID");
				Date rentalDate = rs.getDate("RENTAL_DATE");
				Date dueDate = rs.getDate("DUE_DATE");
				Date returnDate = rs.getDate("RETURN_DATE");
				String rentalStatus = rs.getString("RENTAL_STATUS");

				RentalVO vo =
						new RentalVO(
								rentalId,
								memberId,
								bookId,
								rentalDate,
								dueDate,
								returnDate,
								rentalStatus);

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

	// 회원별 대여 목록 조회
	@Override
	public void selectByMemberId(int memberId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			boolean found = false;

			String sql = "SELECT * FROM EX_RENTAL "
					+ "WHERE MEMBER_ID = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, memberId);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {

				found = true;

				int rentalId = rs.getInt("RENTAL_ID");
				int bookId = rs.getInt("BOOK_ID");
				Date rentalDate = rs.getDate("RENTAL_DATE");
				Date dueDate = rs.getDate("DUE_DATE");
				Date returnDate = rs.getDate("RETURN_DATE");
				String rentalStatus = rs.getString("RENTAL_STATUS");

				System.out.println(
						rentalId + " / "
						+ memberId + " / "
						+ bookId + " / "
						+ rentalDate + " / "
						+ dueDate + " / "
						+ returnDate + " / "
						+ rentalStatus);
			}
			
			if(!found) {
				System.out.println("해당 회원의 대여 정보가 없습니다.");
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 내 대여 정보 조회
	@Override
	public ArrayList<RentalJoinVO> selectMyRental(int memberId) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<RentalJoinVO> list =
				new ArrayList<RentalJoinVO>();

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql =
					"SELECT "
					+ "R.RENTAL_ID, "
					+ "R.BOOK_ID, "
					+ "B.TITLE, "
					+ "R.RENTAL_DATE, "
					+ "R.DUE_DATE, "
					+ "R.RETURN_DATE, "
					+ "R.DISPLAY_STATUS AS RENTAL_STATUS "
                    + "FROM V_RENTAL_STATUS R "
					+ "JOIN EX_BOOK B "
					+ "ON R.BOOK_ID = B.BOOK_ID "
					+ "WHERE R.MEMBER_ID = ? "
					+ "ORDER BY "
					+ "CASE "
					+ "WHEN R.DISPLAY_STATUS = '연체중' THEN 0 "
					+ "WHEN R.DISPLAY_STATUS = '대여중' THEN 1 "
					+ "WHEN R.DISPLAY_STATUS = '반납완료' THEN 2 "
					+ "ELSE 3 END, "
					+ "R.RENTAL_ID";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, memberId);

			rs = pstmt.executeQuery();

			while(rs.next()) {

				int rentalId =
						rs.getInt("RENTAL_ID");

				int bookId =
						rs.getInt("BOOK_ID");

				String title =
						rs.getString("TITLE");

				Date rentalDate =
						rs.getDate("RENTAL_DATE");

				Date dueDate =
						rs.getDate("DUE_DATE");

				Date returnDate =
						rs.getDate("RETURN_DATE");

				String rentalStatus =
						rs.getString("RENTAL_STATUS");

				RentalJoinVO vo =
						new RentalJoinVO(
								rentalId,
								bookId,
								title,
								rentalDate,
								dueDate,
								returnDate,
								rentalStatus);

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


	// 연체 여부 조회
	@Override
	public void checkOverdue() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM V_RENTAL_STATUS "
			+ "WHERE DISPLAY_STATUS = '연체중'";
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			
			boolean found = false;

			while(rs.next()) {

				found = true;

				int rentalId = rs.getInt("RENTAL_ID");
				int memberId = rs.getInt("MEMBER_ID");
				int bookId = rs.getInt("BOOK_ID");
				Date rentalDate = rs.getDate("RENTAL_DATE");
				Date dueDate = rs.getDate("DUE_DATE");

				System.out.println(
						"[연체중] "
						+ rentalId + " / "
						+ memberId + " / "
						+ bookId + " / "
						+ rentalDate + " / "
						+ dueDate);
			}
			
			if(!found) {
				System.out.println("연체중인 도서가 없습니다.");
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	// 대여번호로 대여 정보 조회
	@Override
	public RentalVO selectByRentalId(
			int rentalId) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		RentalVO vo = null;

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql =
					"SELECT * FROM EX_RENTAL "
					+ "WHERE RENTAL_ID = ?";

			pstmt =
					conn.prepareStatement(sql);

			pstmt.setInt(1, rentalId);

			rs =
					pstmt.executeQuery();

			if(rs.next()) {

				vo =
						new RentalVO(
								rs.getInt("RENTAL_ID"),
								rs.getInt("MEMBER_ID"),
								rs.getInt("BOOK_ID"),
								rs.getDate("RENTAL_DATE"),
								rs.getDate("DUE_DATE"),
								rs.getDate("RETURN_DATE"),
								rs.getString("RENTAL_STATUS"));
			}

			rs.close();
			pstmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vo;
	}

	// 도서번호로 반납
	@Override
	public int returnBookByBookId(
			int memberId,
			int bookId) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int result = 0;

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql =
					"UPDATE EX_RENTAL "
					+ "SET RETURN_DATE = NOW(), "
					+ "RENTAL_STATUS = '반납완료' "
					+ "WHERE MEMBER_ID = ? "
					+ "AND BOOK_ID = ? "
					+ "AND RENTAL_STATUS = '대여중'";

			pstmt =
					conn.prepareStatement(sql);

			pstmt.setInt(1, memberId);
			pstmt.setInt(2, bookId);

			result =
					pstmt.executeUpdate();

			// 책 상태 변경
			if(result == 1) {

				String updateBookSql =
						"UPDATE EX_BOOK "
						+ "SET BOOK_STATUS = '대여가능' "
						+ "WHERE BOOK_ID = ?";

				pstmt =
						conn.prepareStatement(
								updateBookSql);

				pstmt.setInt(
						1,
						bookId);

				pstmt.executeUpdate();
			}

			pstmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	// 연체 회원 목록 조회
	@Override
	public ArrayList<OverdueJoinVO> selectOverdueList() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<OverdueJoinVO> list =
				new ArrayList<OverdueJoinVO>();

		String sql =
				"SELECT M.MEMBER_ID, "
				+ "M.NAME, "
				+ "M.PHONE, "
				+ "M.EMAIL, "
				+ "B.TITLE, "
				+ "R.RENTAL_DATE, "
				+ "R.DUE_DATE, "
				+ "R.DISPLAY_STATUS "
				+ "FROM V_RENTAL_STATUS R "
				+ "JOIN EX_MEMBER M "
				+ "ON R.MEMBER_ID = M.MEMBER_ID "
				+ "JOIN EX_BOOK B "
				+ "ON R.BOOK_ID = B.BOOK_ID "
				+ "WHERE R.DISPLAY_STATUS = '연체중' "
				+ "ORDER BY R.DUE_DATE";

		try {

			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			pstmt =
					conn.prepareStatement(sql);

			rs =
					pstmt.executeQuery();

			while(rs.next()) {

				OverdueJoinVO vo =
						new OverdueJoinVO();

				vo.setMemberId(
						rs.getInt("MEMBER_ID"));

				vo.setName(
						rs.getString("NAME"));

				vo.setPhone(
						rs.getString("PHONE"));

				vo.setEmail(
						rs.getString("EMAIL"));

				vo.setTitle(
						rs.getString("TITLE"));

				vo.setRentalDate(
						rs.getDate("RENTAL_DATE"));

				vo.setDueDate(
						rs.getDate("DUE_DATE"));

				vo.setStatus(
						rs.getString("DISPLAY_STATUS"));

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
	
		
	

}
