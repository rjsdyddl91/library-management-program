package edu.java.library.dao;

import java.util.ArrayList;

import edu.java.library.vo.OverdueJoinVO;
import edu.java.library.vo.RentalJoinVO;
import edu.java.library.vo.RentalVO;

public interface RentalDAO {
	
	// 도서 대여
	int rentBook(RentalVO vo);

	// 도서 반납
	int returnBook(int rentalId);

	// 전체 대여 목록 조회
	ArrayList<RentalVO> selectAll();

	// 회원별 대여 정보 조회
	void selectByMemberId(int memberId);
	
	// 내 대여 정보 조회
	ArrayList<RentalJoinVO> selectMyRental(int memberId);
	
	// 연체 여부 확인
	void checkOverdue();
	
	// 대여번호로 대여 정보 조회
	RentalVO selectByRentalId(int rentalId);
	
	// 도서번호로 반납
	int returnBookByBookId(int memberId, int bookId);
	
	// 연체 회원 목록 조회
	ArrayList<OverdueJoinVO> selectOverdueList();
	

}
