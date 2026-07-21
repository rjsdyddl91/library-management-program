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

		String sql = "INSERT INTO EX_MEMBER "
				+ "(NAME, PHONE, EMAIL, PASSWORD) "
				+ "VALUES (?, ?, ?, ?)";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);
		) {

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPhone());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getPassword());

			return pstmt.executeUpdate();

		} catch (SQLException e) {

			// 이메일 또는 전화번호 중복
			if (e.getErrorCode() == 1062) {
				System.out.println(
						"すでに使用されているメールアドレスまたは電話番号です。");
			} else {
				e.printStackTrace();
			}

			return 0;
		}
	}

	// 로그인
	@Override
	public MemberVO login(String email, String password) {

		MemberVO vo = null;

		String sql = "SELECT * FROM EX_MEMBER "
				+ "WHERE EMAIL = ? AND PASSWORD = ?";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);
		) {

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

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
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vo;
	}

	// 전체 회원 목록 조회 기능 구현
	@Override
	public ArrayList<MemberVO> selectAll() {

		ArrayList<MemberVO> list = new ArrayList<>();

		String sql = "SELECT * FROM EX_MEMBER";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);

				ResultSet rs = pstmt.executeQuery();
		) {

			while (rs.next()) {

				int memberId = rs.getInt("MEMBER_ID");
				String name = rs.getString("NAME");
				String phone = rs.getString("PHONE");
				String email = rs.getString("EMAIL");
				String password = rs.getString("PASSWORD");

				MemberVO vo = new MemberVO(
						memberId,
						name,
						phone,
						email,
						password);

				list.add(vo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// MEMBER_ID로 특정 회원 조회 기능 구현
	@Override
	public MemberVO selectByMemberId(int memberId) {

		MemberVO vo = null;

		String sql = "SELECT * FROM EX_MEMBER "
				+ "WHERE MEMBER_ID = ?";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);
		) {

			pstmt.setInt(1, memberId);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

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
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vo;
	}

	// 회원 이름으로 조회
	@Override
	public ArrayList<MemberVO> selectByName(String name) {

		ArrayList<MemberVO> list = new ArrayList<>();

		String sql = "SELECT * FROM EX_MEMBER "
				+ "WHERE NAME LIKE ?";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);
		) {

			pstmt.setString(1, "%" + name + "%");

			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {

					int memberId = rs.getInt("MEMBER_ID");
					String memberName = rs.getString("NAME");
					String phone = rs.getString("PHONE");
					String email = rs.getString("EMAIL");
					String password = rs.getString("PASSWORD");

					MemberVO vo = new MemberVO(
							memberId,
							memberName,
							phone,
							email,
							password);

					list.add(vo);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 회원 정보 수정 기능 구현
	@Override
	public int update(MemberVO vo) {

		String sql = "UPDATE EX_MEMBER "
				+ "SET NAME = ?, PHONE = ?, EMAIL = ?, PASSWORD = ? "
				+ "WHERE MEMBER_ID = ?";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);
		) {

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPhone());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getPassword());
			pstmt.setInt(5, vo.getMemberId());

			return pstmt.executeUpdate();

		} catch (SQLException e) {

			// 이메일 또는 전화번호 중복
			if (e.getErrorCode() == 1062) {

				JOptionPane.showMessageDialog(
						null,
						"すでに使用されている会員情報です。\n"
								+ "メールアドレスまたは電話番号を確認してください。");

			} else {
				e.printStackTrace();
			}

			return 0;
		}
	}

	// MEMBER_ID로 회원 삭제 기능 구현
	@Override
	public int delete(int memberId) {

		String sql = "DELETE FROM EX_MEMBER "
				+ "WHERE MEMBER_ID = ?";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);
		) {

			pstmt.setInt(1, memberId);

			return pstmt.executeUpdate();

		} catch (SQLException e) {

			// 대여 기록이 존재하여 회원을 삭제할 수 없는 경우
			if (e.getErrorCode() == 1451) {
				return 0;
			}

			e.printStackTrace();
			return 0;
		}
	}

	// 비밀번호 찾기
	@Override
	public String findPassword(
			String name,
			String phone,
			String email) {

		String password = null;

		String sql = "SELECT PASSWORD FROM EX_MEMBER "
				+ "WHERE NAME = ? AND PHONE = ? AND EMAIL = ?";

		try (
				Connection conn = DriverManager.getConnection(
						MySQLConnInfo.URL,
						MySQLConnInfo.USER,
						MySQLConnInfo.PASSWORD);

				PreparedStatement pstmt = conn.prepareStatement(sql);
		) {

			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, email);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {
					password = rs.getString("PASSWORD");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return password;
	}

}