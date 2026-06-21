package com.example.exam_system.features.account.mapper;

import com.example.exam_system.features.account.dto.UserRequest;
import com.example.exam_system.features.account.dto.UserResponse;
import com.example.exam_system.features.account.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapping {

    // Mapping UserRequest => User
    @Mapping(target = "roles", source = "roles", ignore = true)
    User mappingUser (UserRequest userRequest);

    @Mapping(target = "roles", source = "roles", ignore = true)
    @Mapping(target = "password", source = "password", ignore = true)
    void mapToUpdate(@MappingTarget User user, UserRequest request);


    UserResponse mappingUserResponse (User user);
}
