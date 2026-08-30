package com.example.exam_system.features.account.service;

import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.dto.request.PermissionRequest;
import com.example.exam_system.features.account.dto.response.PermissionResponse;
import com.example.exam_system.features.account.entity.Permission;
import com.example.exam_system.features.account.mapper.PermissionMapping;
import com.example.exam_system.features.account.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapping mapping;

    public List<PermissionResponse> getAllPermission() {
        List<Permission> getAll = permissionRepository.findAll();
        List<PermissionResponse> convertResponse = mapping.toPermissionListResponse(getAll);
        return convertResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public PermissionResponse createPermission(PermissionRequest request) {
        boolean existingPermission = permissionRepository.existsById(request.getPermissionName());
        if(existingPermission){
            throw new AppException(ErrorCode.PERMISSION_EXIST);
        }
        Permission permission = mapping.toPermision(request);
        return mapping.toPermissionResponse(permissionRepository.save(permission));
    }

    @Transactional(rollbackFor = Exception.class)
    public PermissionResponse deletePermission(String request) {
        Permission existingPermission = permissionRepository.findById(request).orElseThrow(()
                -> new AppException(ErrorCode.PERMISSION_NOT_EXIST));
        permissionRepository.deleteById(request);
        return mapping.toPermissionResponse(existingPermission);
    }

}
