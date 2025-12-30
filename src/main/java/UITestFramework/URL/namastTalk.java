package UITestFramework.URL;

import UITestFramework.Controllers.controllers;

public enum namastTalk implements controllers {

    SIGNUP("auth/signup"),
    LOGIN("auth/login"),
    ME("auth/me"),
    CHECK_AUTH("auth/check"),
    ON_BOARDING("auth/onBoarding"),
    LOGOUT("auth/logout"),
    DELETE("auth/deleteUser");

    private final String path;
    namastTalk(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getModulePath() {
        return "api";
    }

    @Override
    public String version() {
        return "";
    }
}
