package model;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static User createRandomUser(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@ya.ru".toLowerCase();
        final String password = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        return new User(email, password, name);
    }

    public static String createRandomEmail(){
        return RandomStringUtils.randomAlphabetic(10)+"@ya.ru".toLowerCase();
    }
    public static String createRandomUserData(){
        return RandomStringUtils.randomAlphabetic(10).toLowerCase();
    }
}
