package com.example.exam_system.features.authentication.mapper;

import com.example.exam_system.features.authentication.dto.request.PermissionRequest;
import com.example.exam_system.features.authentication.dto.response.PermissionResponse;
import com.example.exam_system.features.authentication.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermessionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission request);

    List<PermissionResponse> toPermissionListResponse(List<Permission> request);

}
