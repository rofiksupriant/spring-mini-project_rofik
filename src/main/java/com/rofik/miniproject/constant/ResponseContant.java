package com.rofik.miniproject.constant;

import org.springframework.stereotype.Component;

@Component
public class ResponseContant {
    private ResponseContant() {
    }

    public static final String USER_CREATED = "user created successfully";
    public static final String USER_NOT_FOUND = "user not found";
    public static final String USER_EMAIL_EXIST = "email already exist";
    public static final String USER_USERNAME_EXIST = "username already exist";
    public static final String USER_GET_ALL = "list user";
    public static final String USER_GET_BY_ID = "user by id";
    public static final String USER_UPDATED = "user updated successfully";
    public static final String USER_DELETED = "user deleted successfully";

}
