package com.example.exam_system.features.authentication.service;

import com.example.exam_system.features.authentication.dto.request.RoleRequest;
import com.example.exam_system.features.authentication.dto.response.RoleResponse;
import com.example.exam_system.features.authentication.entity.Permission;
import com.example.exam_system.features.authentication.entity.Role;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.authentication.mapper.RoleMapper;
import com.example.exam_system.features.authentication.repository.PermissionRepository;
import com.example.exam_system.features.authentication.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper mapper;

    public List<RoleResponse> getAllRole(){
        List<Role> role = roleRepository.findAll();
        return mapper.toRoleListResponse(role);
    }

    @Transactional(rollbackOn = Exception.class)
    public RoleResponse createRole(RoleRequest request){
        boolean existingRole = roleRepository.existsById(request.getName());
        if(existingRole){
            throw new AppException(ErrorCode.ROLE_EXIST);
        }

        Role roleMapper = mapper.toRole(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissionName());
        roleMapper.setPermissions(new HashSet<>(permissions));
        roleRepository.save(roleMapper);
        return mapper.toRoleResponse(roleMapper);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteRole(String ids){
        boolean existingRole = roleRepository.existsById(ids);
        if(!existingRole){
            throw new AppException(ErrorCode.ROLE_NOT_EXIST);
        }
        roleRepository.deleteById(ids);
    }


}
