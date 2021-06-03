package com.agview.api.example;

import java.math.BigInteger;

public class OAuthAccessToken {
    private String access;
    private long exp;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
}
