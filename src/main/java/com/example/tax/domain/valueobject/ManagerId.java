package com.example.tax.domain.valueobject;

import org.springframework.util.StringUtils;

public record ManagerId (Long id) {

    public static ManagerId of(String id) {
        if (!StringUtils.hasText(id))
            throw new IllegalArgumentException("잘못된 관리자 ID 입니다.");

        try {
            return new ManagerId(Long.parseLong(id));
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("잘못된 관리자 ID 입니다.");
        }
    }
}
