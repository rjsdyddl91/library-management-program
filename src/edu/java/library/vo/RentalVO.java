package edu.java.library.vo;

import java.sql.Date;

public class RentalVO {
	private int rentalId;
	private int memberId;
	private int bookId;
	private Date rentalDate;
	private Date dueDate;
	private Date returnDate;
	private String rentalStatus;
	public int getRentalId() {
		return rentalId;
	}
	public void setRentalId(int rentalId) {
		this.rentalId = rentalId;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
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
		return "RentalVO [rentalId=" + rentalId + ", memberId=" + memberId + ", bookId=" + bookId + ", rentalDate="
				+ rentalDate + ", dueDate=" + dueDate + ", returnDate=" + returnDate + ", rentalStatus=" + rentalStatus
				+ "]";
	}
	public RentalVO(int rentalId, int memberId, int bookId, Date rentalDate, Date dueDate, Date returnDate,
			String rentalStatus) {
		super();
		this.rentalId = rentalId;
		this.memberId = memberId;
		this.bookId = bookId;
		this.rentalDate = rentalDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.rentalStatus = rentalStatus;
	}
	public RentalVO() {}
	

}
