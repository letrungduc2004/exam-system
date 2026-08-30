package com.example.exam_system.features.account.controller;

import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.account.dto.request.PermissionRequest;
import com.example.exam_system.features.account.dto.response.PermissionResponse;
import com.example.exam_system.features.account.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping("/permission")
    public APIResponse<List<PermissionResponse>> getAllPermission() {
        APIResponse<List<PermissionResponse>> response = APIResponse.<List<PermissionResponse>>builder()
                .code(1000)
                .data(permissionService.getAllPermission())
                .build();
        return response;
    }

    @PostMapping("/permission")
    public APIResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        APIResponse<PermissionResponse> response = APIResponse.<PermissionResponse>builder()
                .code(1000)
                .message("Create Permission Successfull ")
                .data(permissionService.createPermission(request))
                .build();
        return response;
    }

    @DeleteMapping("/permission/{ids}")
    public APIResponse<PermissionResponse> deletePermission(@PathVariable(name = "ids")  String idPermission) {
        APIResponse<PermissionResponse> response = APIResponse.<PermissionResponse>builder()
                .code(1000)
                .message("Delete Permission Successfull ")
                .data(permissionService.deletePermission(idPermission))
                .build();
        return response;
    }

}
