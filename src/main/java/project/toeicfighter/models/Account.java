package project.toeicfighter.models;

import java.time.LocalDateTime;

public class Account {
    private String accountID;
    private String username;
    private String password;
    private String fullname;
    private String role;
    private LocalDateTime createDate;
    private LocalDateTime lastLogin;

    public Account(String username, String password, String fullname, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    public Account(String accountID, String username, String password, String fullname, String role, LocalDateTime createDate, LocalDateTime lastLogin) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
        this.createDate = createDate;
        this.lastLogin = lastLogin;
    }

    public Account(String accountID, String username, String password, String fullname, String role) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
