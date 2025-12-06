package example.model;

import java.sql.Timestamp;

public class User {
	private int userId;
    private String email;
    private String password;
    private String fullName;
    private String gender; // map toi enum nam nu khac
    private String phone;
    private Timestamp createdAt;
    private String role; // 'admin' hoac 'user'

    public User() {}

    public User(int userId, String email, String password, String fullName, String gender, String phone, Timestamp createdAt, String role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.createdAt = createdAt;
        this.role = role;
    }
    
    public User(String email, String password, String fullName, String gender, String phone, Timestamp createdAt, String role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.createdAt = createdAt;
        this.role = role;
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    
}
