package com.example.exam_system.features.account.service;

import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.dto.request.RoleRequest;
import com.example.exam_system.features.account.dto.response.RoleResponse;
import com.example.exam_system.features.account.entity.Permission;
import com.example.exam_system.features.account.entity.Role;
import com.example.exam_system.features.account.mapper.RoleMapping;
import com.example.exam_system.features.account.repository.PermissionRepository;
import com.example.exam_system.features.account.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapping mapping;

    @Transactional(rollbackFor = Exception.class)
    public List<RoleResponse> getAllRole() {
        List<Role> getAll = roleRepository.findAll();
        List<RoleResponse> convertResponse = mapping.toRoleListResponse(getAll);
        return convertResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public RoleResponse createRole(RoleRequest request) {
        boolean existingRole = roleRepository.existsById(request.getRoleName());
        if(existingRole){
            throw new AppException(ErrorCode.ROLE_EXIST);
        }
        List<Permission> getPermissionName = permissionRepository.findAllById(request.getPermissionName());
        Role mappingRole = mapping.toRole(request);
        mappingRole.setPermissions(new HashSet<>(getPermissionName));
        return mapping.toRoleResponse(roleRepository.save(mappingRole));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String request) {
        Role existingRole = roleRepository.findById(request).orElseThrow(()
                -> new AppException(ErrorCode.ROLE_NOT_EXIST));
        roleRepository.deleteById(request);
    }
}
