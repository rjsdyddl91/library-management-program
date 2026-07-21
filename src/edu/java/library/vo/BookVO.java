	package edu.java.library.vo;

	public class BookVO {
		private int bookId;
		private String title;
		private String author;
		private String publisher;
		private String bookStatus;
		public int getBookId() {
			return bookId;
		}
		public void setBookId(int bookId) {
			this.bookId = bookId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getPublisher() {
			return publisher;
		}
		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}
		public String getBookStatus() {
			return bookStatus;
		}
		public void setBookStatus(String bookStatus) {
			this.bookStatus = bookStatus;
		}
		@Override
		public String toString() {
			return "BookVO [bookId=" + bookId + ", title=" + title + ", author=" + author + ", publisher=" + publisher
					+ ", bookStatus=" + bookStatus + "]";
		}
		public BookVO(int bookId, String title, String author, String publisher, String bookStatus) {
			super();
			this.bookId = bookId;
			this.title = title;
			this.author = author;
			this.publisher = publisher;
			this.bookStatus = bookStatus;
		}
		public BookVO() {}
		

		
		
		
		
	}
