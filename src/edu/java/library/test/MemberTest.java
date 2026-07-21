package edu.java.library.test;

import java.util.ArrayList;

import edu.java.library.dao.MemberDAOImple;
import edu.java.library.vo.MemberVO;

public class MemberTest {

	public static void main(String[] args) {
		MemberVO vo = new MemberVO(
				0,
				"고건영",
				"010-2439-3808",
				"rjsdyddl91@naver.com",
				"38083808");
		
		MemberDAOImple dao = new MemberDAOImple();

		int result = dao.insert(vo);

		if(result == 1) {
			System.out.println("회원 등록 성공!");
		} else {
			System.out.println("회원 등록 실패!");
		}
		
		ArrayList<MemberVO> list =
				dao.selectAll();

		for(MemberVO member : list) {
			System.out.println(member);
		}
		
		System.out.println("===== 로그인 테스트 =====");

		MemberVO loginUser =
				dao.login(
						"tesd@tesdt.com",
						"1234");

		if(loginUser != null) {
			System.out.println("로그인 성공!");
			System.out.println(loginUser);
		} else {
			System.out.println("이메일 또는 비밀번호가 틀렸습니다.");
		}
		
		System.out.println("===== 회원 인덱스 검색 =====");
		dao.selectByMemberId(1);
	
		System.out.println("===== 회원 수정 =====");

		MemberVO updateVo = new MemberVO(
				1,
				"김철수",
				"010-9999-9999",
				"kim@test.com",
				"9999");
		
		int updateResult = dao.update(updateVo);

		if(updateResult == 1) {
			System.out.println("회원 수정 성공!");
		} else {
			System.out.println("회원 수정 실패!");
		}

		ArrayList<MemberVO> listAll =
				dao.selectAll();

		for(MemberVO member : listAll) {
			System.out.println(member);
		}
		
		System.out.println("===== 회원 삭제 =====");

		int deleteResult = dao.delete(1);

		if(deleteResult == 1) {
			System.out.println("회원 삭제 성공!");
		} else {
			System.out.println("회원 삭제 실패!");
		}

		ArrayList<MemberVO> Memberlist =
				dao.selectAll();

		for(MemberVO member : Memberlist) {
			System.out.println(member);
		}
		
		System.out.println("===== 비밀번호 찾기 =====");

		String findPassword =
				dao.findPassword(
						"홍길동",
						"010-1234-5678",
						"test@test.com");

		if(findPassword != null) {
			System.out.println(
					"비밀번호 : " + findPassword);
		} else {
			System.out.println(
					"일치하는 회원 정보가 없습니다.");
		}
		
	} // main end

} // class end
