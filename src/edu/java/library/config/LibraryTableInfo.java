package edu.java.library.config;

public interface LibraryTableInfo {
	// EX_BOOK 테이블 정보
	String TABLE_BOOK = "EX_BOOK";

	String COL_BOOK_ID = "BOOK_ID";
	String COL_TITLE = "TITLE";
	String COL_AUTHOR = "AUTHOR";
	String COL_PUBLISHER = "PUBLISHER";
	String COL_BOOK_STATUS = "BOOK_STATUS";
	
	// EX_MEMBER 테이블 정보
	String TBL_EX_MEMBER = "EX_MEMBER";

	String COL_MEMBER_ID = "MEMBER_ID";
	String COL_NAME = "NAME";
	String COL_PHONE = "PHONE";
	String COL_EMAIL = "EMAIL";
	String COL_PASSWORD = "PASSWORD";
	
	// EX_RENTAL 테이블 정보
	String TABLE_RENTAL = "EX_RENTAL";

	String COL_RENTAL_ID = "RENTAL_ID";
	String COL_RENTAL_MEMBER_ID = "MEMBER_ID";
	String COL_RENTAL_BOOK_ID = "BOOK_ID";
	String COL_RENTAL_DATE = "RENTAL_DATE";
	String COL_DUE_DATE = "DUE_DATE";
	String COL_RETURN_DATE = "RETURN_DATE";
	String COL_RENTAL_STATUS = "RENTAL_STATUS";

}
