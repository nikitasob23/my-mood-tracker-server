package com.niksob.domain.path.controller.authorization_service;

public class AuthControllerPaths {
    public static final String SIGNUP = "/signup";
    public static final String ACTIVE_CODE = SIGNUP + "/activate";
    public static final String SIGNOUT = "/signout";
    public static final String SIGNOUT_ALL = SIGNOUT + "/all";
    public static final String PASSWORD_RESETTING = "/reset/password";
    public static final String EMAIL_RESETTING = "/reset/email";
    public static final String EMAIL_RESETTING_ACTIVATION = EMAIL_RESETTING + "/activation";
}
