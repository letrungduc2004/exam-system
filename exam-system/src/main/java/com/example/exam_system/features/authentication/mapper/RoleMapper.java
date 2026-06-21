package com.example.exam_system.features.authentication.mapper;

import com.example.exam_system.features.authentication.dto.request.RoleRequest;
import com.example.exam_system.features.authentication.dto.response.RoleResponse;
import com.example.exam_system.features.authentication.entity.Permission;
import com.example.exam_system.features.authentication.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    // Bỏ qua Field permession vì request chỉ gửi về set<String> chứ không gửi cả Set<Permission>
    @Mapping(target = "permissions", ignore = true)
    Role toRole (RoleRequest request);

    @Mapping(target = "permissionName", source = "permissions", qualifiedByName =  "customPermissionName")
    RoleResponse toRoleResponse (Role request);

    List<RoleResponse> toRoleListResponse (List<Role> request);

    @Named("customPermissionName")
    default String custom(Permission permission){
        return permission.getName();
    }
}
