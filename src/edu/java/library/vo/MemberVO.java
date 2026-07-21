package edu.java.library.vo;

public class MemberVO {
	private int memberId;
	private String name;
	private String phone;
	private String email;
	private String password;
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "MemberVO [memberId=" + memberId + ", name=" + name + ", phone=" + phone + ", email=" + email
				+ ", password=" + password + "]";
	}
	

	public MemberVO(int memberId, String name, String phone, String email, String password) {
		super();
		this.memberId = memberId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}

	public MemberVO() {}

}
