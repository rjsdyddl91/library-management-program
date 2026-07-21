package edu.java.library.dao;

import java.util.ArrayList;

import edu.java.library.vo.BookVO;

public interface BookDAO {
	
	// 도서 등록
	int insert(BookVO vo);
		
	// 도서 번호(인덱스) 조회	
	BookVO selectByBookId(int bookId);
	
	// 도서 전체 목록 조회
	ArrayList<BookVO> selectAll();
	
	// 도서명 검색
	ArrayList<BookVO> selectByTitle(String title);
		
	// 도서 정보 수정
	int update(BookVO vo);
		
	// 도서 삭제
	int delete(int bookId);
}
