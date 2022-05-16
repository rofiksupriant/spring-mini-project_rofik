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
    public static final String CATEGORY_EXIST = "category name already exist";
    public static final String CATEGORY_NOT_FOUND = "category not found";
    public static final String CATEGORY_CREATED = "category created successfully";
    public static final String CATEGORY_UPDATED = "category updated successfully";
    public static final String CATEGORY_DELETED = "category deleted successfully";
    public static final String CATEGORY_GET_ALL = "list category";
    public static final String CATEGORY_GET_BY_ID = "category by id";
    public static final String PAYMENT_EXIST = "payment name already exist";
    public static final String PAYMENT_NOT_FOUND = "payment not found";
    public static final String PAYMENT_CREATED = "payment created successfully";
    public static final String PAYMENT_UPDATED = "payment updated successfully";
    public static final String PAYMENT_DELETED = "payment deleted successfully";
    public static final String PAYMENT_GET_ALL = "list payment";
    public static final String PAYMENT_GET_BY_ID = "payment by id";
    public static final String PRODUCT_NOT_FOUND = "product not found";
    public static final String PRODUCT_CREATED = "product created successfully";
    public static final String PRODUCT_UPDATED = "product updated successfully";
    public static final String PRODUCT_DELETED = "product deleted successfully";
    public static final String PRODUCT_GET_ALL = "list product";
    public static final String PRODUCT_GET_ALL_DELETED = "list product deleted";
    public static final String PRODUCT_GET_BY_ID = "product by id";
    public static final String PRODUCT_CATEGORY_NOT_FOUND = "product category not found";
    public static final String PRODUCT_CATEGORY_EXIST = "product category already exist";
    public static final String PRODUCT_CATEGORY_CREATED = "product category created successfully";
    public static final String PRODUCT_CATEGORY_UPDATED = "product category updated successfully";
    public static final String PRODUCT_CATEGORY_DELETED = "product category deleted successfully";
    public static final String PRODUCT_CATEGORY_GET_ALL = "list product category";
    public static final String PRODUCT_CATEGORY_GET_BY_ID = "product category by id";
    public static final String ORDER_CREATED = "order created successfully";

}
