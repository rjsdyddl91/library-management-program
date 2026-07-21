package edu.java.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import edu.java.library.config.MySQLConnInfo;
import edu.java.library.vo.MemberVO;

public class MemberDAOImple implements MemberDAO {
	
	// 회원 등록 기능 구현
	@Override
	public int insert(MemberVO vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			String sql = "INSERT INTO EX_MEMBER "
					+ "(NAME, PHONE, EMAIL, PASSWORD) "
					+ "VALUES (?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPhone());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getPassword());
			result = pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("이미 사용 중인 이메일 또는 전화번호입니다.");
		}
		return result;
		
	}
	
	// 로그인
	@Override
	public MemberVO login(String email, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO vo = null;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "SELECT * FROM EX_MEMBER "
					+ "WHERE EMAIL = ? AND PASSWORD = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {

				int memberId = rs.getInt("MEMBER_ID");
				String name = rs.getString("NAME");
				String phone = rs.getString("PHONE");
				String userEmail = rs.getString("EMAIL");
				String userPassword = rs.getString("PASSWORD");

				vo = new MemberVO(
						memberId,
						name,
						phone,
						userEmail,
						userPassword);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return vo;
	}

    
	// 전체 회원 목록 조회 기능 구현
	@Override
	public ArrayList<MemberVO> selectAll() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<MemberVO> list =
				new ArrayList<MemberVO>();

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql =
					"SELECT * FROM EX_MEMBER";

			pstmt =
					conn.prepareStatement(sql);

			rs =
					pstmt.executeQuery();

			while(rs.next()) {

				int memberId =
						rs.getInt("MEMBER_ID");

				String name =
						rs.getString("NAME");

				String phone =
						rs.getString("PHONE");

				String email =
						rs.getString("EMAIL");

				String password =
						rs.getString("PASSWORD");

				MemberVO vo =
						new MemberVO(
								memberId,
								name,
								phone,
								email,
								password);

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

	// MEMBER_ID로 특정 회원 조회 기능 구현
	@Override
	public MemberVO selectByMemberId(int memberId) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		MemberVO vo = null;

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql =
					"SELECT * FROM EX_MEMBER "
					+ "WHERE MEMBER_ID = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, memberId);

			rs = pstmt.executeQuery();

			if(rs.next()) {

				int id = rs.getInt("MEMBER_ID");
				String name = rs.getString("NAME");
				String phone = rs.getString("PHONE");
				String email = rs.getString("EMAIL");
				String password = rs.getString("PASSWORD");

				vo = new MemberVO(
						id,
						name,
						phone,
						email,
						password);
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
	public ArrayList<MemberVO> selectByName(
			String name) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<MemberVO> list =
				new ArrayList<MemberVO>();

		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);

			String sql =
					"SELECT * "
					+ "FROM EX_MEMBER "
					+ "WHERE NAME LIKE ?";

			pstmt =
					conn.prepareStatement(sql);

			pstmt.setString(
					1,
					"%"
					+ name
					+ "%");

			rs =
					pstmt.executeQuery();

			while(rs.next()) {

				int memberId =
						rs.getInt(
								"MEMBER_ID");

				String memberName =
						rs.getString(
								"NAME");

				String phone =
						rs.getString(
								"PHONE");

				String email =
						rs.getString(
								"EMAIL");

				String password =
						rs.getString(
								"PASSWORD");

				MemberVO vo =
						new MemberVO(
								memberId,
								memberName,
								phone,
								email,
								password);

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

	// 회원 정보 수정 기능 구현
	@Override
	public int update(MemberVO vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "UPDATE EX_MEMBER "
					+ "SET NAME = ?, PHONE = ?, EMAIL = ?, PASSWORD = ? "
					+ "WHERE MEMBER_ID = ?";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPhone());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getPassword());
			pstmt.setInt(5, vo.getMemberId());
			
			result = pstmt.executeUpdate();

			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {

			if(e.getMessage()
					.contains("Duplicate entry")) {

				JOptionPane.showMessageDialog(
						null,
						"이미 사용 중인 회원 정보입니다.\n이메일 또는 전화번호를 확인해주세요.");

			} else {

				e.printStackTrace();
			}

			return 0;
		}
		
		return result;
	}

	// MEMBER_ID로 회원 삭제 기능 구현
	@Override
	public int delete(int memberId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "DELETE FROM EX_MEMBER "
					+ "WHERE MEMBER_ID = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, memberId);

			result = pstmt.executeUpdate();

			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {

			if(e.getMessage()
					.contains("foreign key constraint fails")) {

				// 대여 기록이 있는 회원 삭제 시도
				return 0;

			} else {

				e.printStackTrace();
			}
		}
		
		return result;
	}

	// 비밀번호 찾기
	@Override
	public String findPassword(String name, String phone, String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String password = null;
		
		try {
			conn = DriverManager.getConnection(
					MySQLConnInfo.URL,
					MySQLConnInfo.USER,
					MySQLConnInfo.PASSWORD);
			
			String sql = "SELECT PASSWORD FROM EX_MEMBER "
					+ "WHERE NAME = ? AND PHONE = ? AND EMAIL = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, email);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				password = rs.getString("PASSWORD");
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return password;
	}

	



}
