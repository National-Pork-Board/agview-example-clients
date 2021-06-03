package com.agview.api.example;

import lombok.Data;

@Data
public class OAuthAccessToken {
    private String access;
    private long exp;
}
