package edu.java.library.test;

import java.util.ArrayList;

import edu.java.library.dao.BookDAOImple;
import edu.java.library.vo.BookVO;

public class BookTest {

	public static void main(String[] args) {
		BookVO vo = new BookVO(
				0,
				"자바의 정석",
				"남궁성",
				"도우출판",
				"대여가능");
		
		BookDAOImple dao = new BookDAOImple();
		int result = dao.insert(vo);
		if(result == 1) {
			System.out.println("도서 등록 성공!");
		} else {
			System.out.println("도서 등록 실패!");
		}
		ArrayList<BookVO> list = dao.selectAll();

		for(BookVO book : list) {
			System.out.println(book);
		}
		
		System.out.println("===== 도서 인덱스 검색 =====");
		dao.selectByBookId(99);
		
		System.out.println("===== 도서 수정 =====");

		BookVO updateVo = new BookVO(
				1,
				"자바의 정석 개정판",
				"남궁성",
				"도우출판",
				"대여가능");

		int updateResult = dao.update(updateVo);

		if(updateResult == 1) {
			System.out.println("도서 수정 성공!");
		} else {
			System.out.println("도서 수정 실패!");
		}

		ArrayList<BookVO> bookList =
				dao.selectAll();

		for(BookVO book : bookList) {
			System.out.println(book);
		}
		
		System.out.println("===== 도서 삭제 =====");

		int deleteResult = dao.delete(1);

		if(deleteResult == 1) {
			System.out.println("도서 삭제 성공!");
		} else {
			System.out.println("도서 삭제 실패!");
		}

		ArrayList<BookVO> Allbook =
				dao.selectAll();

		for(BookVO book : Allbook) {
			System.out.println(book);
		}


	} // main end

} // class end
