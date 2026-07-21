package edu.java.library.vo;

import java.sql.Date;

public class RentalJoinVO {

	private int rentalId;
	private int bookId;
	private String title;
	private Date rentalDate;
	private Date dueDate;
	private Date returnDate;
	private String rentalStatus;

	public RentalJoinVO() {}

	public RentalJoinVO(int rentalId, int bookId, String title,
			Date rentalDate, Date dueDate, Date returnDate,
			String rentalStatus) {
		this.rentalId = rentalId;
		this.bookId = bookId;
		this.title = title;
		this.rentalDate = rentalDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.rentalStatus = rentalStatus;
	}

	public int getRentalId() {
		return rentalId;
	}

	public void setRentalId(int rentalId) {
		this.rentalId = rentalId;
	}

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

	public Date getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(Date rentalDate) {
		this.rentalDate = rentalDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getRentalStatus() {
		return rentalStatus;
	}

	public void setRentalStatus(String rentalStatus) {
		this.rentalStatus = rentalStatus;
	}

	@Override
	public String toString() {
		return rentalId + " / " + bookId + " / " + title + " / "
				+ rentalDate + " / " + dueDate + " / "
				+ returnDate + " / " + rentalStatus;
	}
}