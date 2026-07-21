package edu.java.library.vo;

import java.sql.Date;

public class OverdueJoinVO {
	
	private int memberId;
	private String name;
	private String phone;
	private String email;
	private String title;
	private Date rentalDate;
	private Date dueDate;
	private String status;
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "OverdueJoinVO [memberId=" + memberId + ", name=" + name + ", phone=" + phone + ", email=" + email
				+ ", title=" + title + ", rentalDate=" + rentalDate + ", dueDate=" + dueDate + ", status=" + status
				+ "]";
	}
	public OverdueJoinVO(int memberId, String name, String phone, String email, String title, Date rentalDate,
			Date dueDate, String status) {
		super();
		this.memberId = memberId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.title = title;
		this.rentalDate = rentalDate;
		this.dueDate = dueDate;
		this.status = status;
	}
	public OverdueJoinVO() {}
	
	

}
