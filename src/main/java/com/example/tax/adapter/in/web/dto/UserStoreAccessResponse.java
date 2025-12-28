package com.example.tax.adapter.in.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class UserStoreAccessResponse {

    private final List<String> accessibleStoreIds;

}
