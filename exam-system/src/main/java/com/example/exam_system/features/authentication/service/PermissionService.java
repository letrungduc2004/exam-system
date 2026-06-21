package com.example.exam_system.features.authentication.service;

import com.example.exam_system.features.authentication.dto.request.PermissionRequest;
import com.example.exam_system.features.authentication.dto.response.PermissionResponse;
import com.example.exam_system.features.authentication.entity.Permission;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.authentication.mapper.PermessionMapper;
import com.example.exam_system.features.authentication.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermessionMapper mapper;

    public List<PermissionResponse> getAll() {
        List<Permission> getAll = permissionRepository.findAll();
        return mapper.toPermissionListResponse(getAll);
    }

    @Transactional(rollbackFor = Exception.class)
    public PermissionResponse create(PermissionRequest request) {
        boolean exsitingPermission = permissionRepository.existsById(request.getName());
        if (exsitingPermission) {
            throw new AppException(ErrorCode.PERMISSION_EXIST);
        }

        Permission permissionMapper = mapper.toPermission(request);
        permissionRepository.save(permissionMapper);
        return mapper.toPermissionResponse(permissionMapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        boolean exsitingPermission = permissionRepository.existsById(id);
        if (!exsitingPermission) {
            throw new AppException(ErrorCode.PERMISSION_NOT_EXIST);
        }
        permissionRepository.deleteById(id);
    }
}
