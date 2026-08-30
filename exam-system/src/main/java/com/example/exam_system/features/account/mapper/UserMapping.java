package com.example.exam_system.features.account.mapper;

import com.example.exam_system.features.account.dto.request.UserRequest;
import com.example.exam_system.features.account.dto.response.UserResponse;
import com.example.exam_system.features.account.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapping {

    // Mapping UserRequest => User
    @Mapping(target = "roles", ignore = true)
    User toUser (UserRequest userRequest);

    UserResponse toUserResponse(User user);

    // NullValuePropertyMappingStrategy bỏ qua các field null (field request không gửi)
    void toMappingUpdate(@MappingTarget User user, UserRequest request);
}
