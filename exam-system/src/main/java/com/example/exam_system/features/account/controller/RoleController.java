package com.example.exam_system.features.account.controller;

import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.account.dto.request.RoleRequest;
import com.example.exam_system.features.account.dto.response.RoleResponse;
import com.example.exam_system.features.account.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/role")
    public APIResponse<List<RoleResponse>> getAllRole() {
        APIResponse<List<RoleResponse>> response = APIResponse.<List<RoleResponse>>builder()
                .code(1000)
                .data(roleService.getAllRole())
                .build();
        return response;
    }

    @PostMapping("/role")
    public APIResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        APIResponse<RoleResponse> response = APIResponse.<RoleResponse>builder()
                .code(1000)
                .message("Create Role Successfull ")
                .data(roleService.createRole(request))
                .build();
        return response;
    }

    @DeleteMapping("/role/{ids}")
    public APIResponse<RoleResponse> deleteRole(@PathVariable(name = "ids")  String idRole) {
        roleService.deleteRole(idRole);
        APIResponse<RoleResponse> response = APIResponse.<RoleResponse>builder()
                .code(1000)
                .message("Delete Role Successfull ")
                .build();
        return response;
    }
}
