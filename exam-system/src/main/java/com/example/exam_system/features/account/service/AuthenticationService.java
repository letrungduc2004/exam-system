package com.example.exam_system.features.account.service;

import com.example.exam_system.common.enums.ROLE;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.dto.request.AuthenticationRequest;
import com.example.exam_system.features.account.dto.response.AuthenticationResponse;
import com.example.exam_system.features.account.entity.Role;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.account.mapper.AuthenticationMapping;
import com.example.exam_system.features.account.repository.RoleRepository;
import com.example.exam_system.features.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationMapping authenticationMapping;


    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse register(AuthenticationRequest request) {
        boolean existingUser = userRepository.existsByUserName(request.getUserName());
        if (existingUser) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = authenticationMapping.toUser(request);
        user.setCreatedAt(new Date());
        // Set Role
        List<Role> role = roleRepository.findAllById(List.of(ROLE.STUDENT.name()));
        user.setRoles(new HashSet<>(role));

        userRepository.save(user);
        return authenticationMapping.toAuthenticationResponse(user);
    }
}
