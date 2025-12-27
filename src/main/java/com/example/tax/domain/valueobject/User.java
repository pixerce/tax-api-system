package com.example.tax.domain.valueobject;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private Long srl;
    private String username;

    public User(Long srl, String username) {
        this.srl = srl;
        this.username = username;
    }
}