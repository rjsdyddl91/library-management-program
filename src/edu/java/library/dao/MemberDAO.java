package edu.java.library.dao;

import java.util.ArrayList;

import edu.java.library.vo.MemberVO;

public interface MemberDAO {
	
	// 새로운 회원 등록
	int insert(MemberVO vo);
		
	// 로그인
	MemberVO login(String email, String password);
	
	// 전체 회원 목록 조회
	ArrayList<MemberVO> selectAll();
		
	// MEMBER_ID로 특정 회원 조회
	MemberVO selectByMemberId(int memberId);
	
	// 회원명 검색
	ArrayList<MemberVO> selectByName(String name);
		
	// 기존 회원 정보 수정
	int update(MemberVO vo);
		
	// MEMBER_ID로 회원 삭제
	int delete(int memberId);
	
	// 비밀번호 찾기
	String findPassword(String name, String phone, String email);

}
