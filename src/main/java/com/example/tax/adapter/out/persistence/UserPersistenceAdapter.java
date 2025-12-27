package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.repository.UserRepository;
import com.example.tax.application.port.out.UserPort;
import com.example.tax.domain.exception.InvalidStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {
    private final UserRepository userRepository;

    @Override
    public void checkExistUser(Long userSrl) {
        userRepository.findById(userSrl)
                .orElseThrow(() -> new InvalidStateException("사용자 정보가 없습니다. userSrl: " + userSrl));
    }
}