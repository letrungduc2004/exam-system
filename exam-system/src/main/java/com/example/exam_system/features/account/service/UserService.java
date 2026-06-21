package com.example.exam_system.features.account.service;

import com.example.exam_system.features.account.dto.UserRequest;
import com.example.exam_system.features.account.dto.UserResponse;
import com.example.exam_system.features.authentication.entity.Role;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.mapper.UserMapping;
import com.example.exam_system.features.authentication.repository.RoleRepository;
import com.example.exam_system.features.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapping mapping;
    private final PasswordEncoder encoder ;

    public UserResponse updateUser(UserRequest request) {
        User existingUser = userRepository.findByUserName(request.getUserName()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));
        mapping.mapToUpdate(existingUser, request);
        List<Role> role = roleRepository.findAllById(request.getRoles());
        existingUser.setRoles(new HashSet<>(role));
        existingUser.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(existingUser);
        return mapping.mappingUserResponse(existingUser);
    }
}
