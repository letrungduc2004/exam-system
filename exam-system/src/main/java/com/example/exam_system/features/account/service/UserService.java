package com.example.exam_system.features.account.service;

import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.dto.request.UserRequest;
import com.example.exam_system.features.account.dto.response.UserResponse;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.account.mapper.UserMapping;
import com.example.exam_system.features.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse updateUser(UUID userId, UserRequest request) {
        User user = existingUser(userId);
        user.setFullName(request.getFullName());
        user.setPassword(request.getPassword());
        return userMapping.toUserResponse(user);
    }

    public User existingUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
}
