package edu.java.library.test;

import java.util.ArrayList;

import edu.java.library.dao.RentalDAOImple;
import edu.java.library.vo.RentalVO;

public class RentalTest {

	public static void main(String[] args) {
		RentalVO vo = new RentalVO();

		vo.setMemberId(2);
		vo.setBookId(1);

		RentalDAOImple dao = new RentalDAOImple();

		int result = dao.rentBook(vo);

		if(result == 1) {
			System.out.println("도서 대여 성공!");
		} else {
			System.out.println("도서 대여 실패!");
		}
		
		System.out.println("===== 대여 전체 조회 =====");

		ArrayList<RentalVO> list =
				dao.selectAll();

		for(RentalVO rental : list) {
			System.out.println(rental);
		}
		
		System.out.println("===== 회원별 대여 조회 =====");
		dao.selectByMemberId(2);
		
		System.out.println("===== 도서 반납 =====");

		int returnResult = dao.returnBook(1);

		
		if(returnResult == 1) {
			System.out.println("도서 반납 성공!");
		} else {
			System.out.println("도서 반납 실패!");
		}
		
		System.out.println("===== 연체 여부 확인 =====");
		dao.checkOverdue();
		
		

	} // main end

} // class end
