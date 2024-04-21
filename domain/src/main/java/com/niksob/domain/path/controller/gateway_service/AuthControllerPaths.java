package com.niksob.domain.path.controller.gateway_service;

public class AuthControllerPaths {
    public static final String BASE_URI = "/auth";
    public static final String SIGNUP = "/signup";
    public static final String ACTIVE_CODE = SIGNUP + "/activate";
    public static final String SIGNOUT = "/signout";
    public static final String SIGNOUT_ALL = SIGNOUT + "/all";
}
