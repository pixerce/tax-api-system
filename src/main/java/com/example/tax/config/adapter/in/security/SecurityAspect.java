package com.example.tax.config.adapter.in.security;

import com.example.tax.application.port.in.security.exception.AccessDeniedException;
import com.example.tax.application.port.out.UserStorePort;
import com.example.tax.domain.valueobject.StoreId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityAspect {

    private final HttpServletRequest request;
    private final UserStorePort userStorePort;

    @Before("@annotation(com.example.tax.application.port.in.security.AdminOnly)")
    public void validateAdmin() {
        String role = request.getHeader("X-Admin-Role");
        if (!"ADMIN".equals(role)) {
            throw new AccessDeniedException("ADMIN 권한이 필요한 서비스입니다.");
        }
    }

    @Before("@annotation(com.example.tax.application.port.in.security.CheckStoreAccess)")
    public void validateStoreAccess(JoinPoint joinPoint) {
        String role = request.getHeader("X-Admin-Role");

        if ("ADMIN".equals(role)) return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        String storeId = null;
        Long userSrl = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if ("storeId".equals(parameterNames[i])) {
                storeId = (String) args[i];
                continue;
            }

            if ("userSrl".equals(parameterNames[i])) {
                userSrl = (Long) args[i];
                continue;
            }
        }

        if (storeId == null || userSrl == null)
            throw new IllegalArgumentException("권한 확인을 위한 사업장 식별자(storeId/userSrl)를 찾을 수 없습니다.");

        boolean hasAccess = userStorePort.existsByUserSrlAndStoreId(userSrl, StoreId.of(storeId));
        if (!hasAccess)
            throw new AccessDeniedException("해당 사업장에 대한 관리 권한이 없습니다.");
    }
}