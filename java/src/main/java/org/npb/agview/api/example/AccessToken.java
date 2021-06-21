package org.npb.agview.api.example;

import lombok.Data;

@Data
public class AccessToken {
    private String access;
    private long exp;
}
