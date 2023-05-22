package top.scaleda.kernel.database.dao;

import java.util.Date;

public class Token {
    public Token() {
    }

    public Token(String token, String username, Date exp) {
        this.token = token;
        this.username = username;
        this.exp = exp;
    }

    private String token;
    private String username;
    private Date exp;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", exp=" + exp +
                '}';
    }
}
