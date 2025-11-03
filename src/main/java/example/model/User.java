package example.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Corresponds to table users.
 * NOTE: store hashed password (bcrypt) in passwordHash.
 */
public class User {
    private int id;
    private String username;
    private LocalDate birthDate;
    private Boolean gender;         // nullable
    private String phoneNumber;
    private String passwordHash;    // store hashed password
    private String email;
    private Role role;

    public User() {}

    public User(int id, String username, LocalDate birthDate, Boolean gender,
                String phoneNumber, String passwordHash, String email, Role role) {
        this.id = id;
        this.username = username;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    // --- getters / setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Boolean getGender() { return gender; }
    public void setGender(Boolean gender) { this.gender = gender; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", email=" + email + ", role=" + role + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
