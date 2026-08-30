package com.example.exam_system.features.account.mapper;

import com.example.exam_system.features.account.dto.request.AuthenticationRequest;
import com.example.exam_system.features.account.dto.request.UserRequest;
import com.example.exam_system.features.account.dto.response.AuthenticationResponse;
import com.example.exam_system.features.account.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthenticationMapping {

    @Mapping(target = "roles", ignore = true)
    User toUser(AuthenticationRequest request);

    AuthenticationResponse toAuthenticationResponse(User user);

}
