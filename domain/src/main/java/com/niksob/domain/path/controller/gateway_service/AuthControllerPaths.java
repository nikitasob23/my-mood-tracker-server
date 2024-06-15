package com.niksob.domain.path.controller.gateway_service;

public class AuthControllerPaths {
    public static final String BASE_URI = "/auth";
    public static final String SIGNUP = "/signup";
    public static final String ACTIVE_CODE = SIGNUP + "/activate";
    public static final String SIGNOUT = "/signout";
    public static final String SIGNOUT_ALL = SIGNOUT + "/all";
    public static final String PASSWORD_RESETTING =  "/reset/password";
    public static final String EMAIL_RESETTING = "/reset/email";
    public static final String EMAIL_RESETTING_ACTIVATION = EMAIL_RESETTING + "/activate";
}
