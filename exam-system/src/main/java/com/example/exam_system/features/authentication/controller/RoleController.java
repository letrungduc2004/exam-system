package com.example.exam_system.features.authentication.controller;

import com.example.exam_system.features.authentication.dto.request.RoleRequest;
import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.authentication.dto.response.RoleResponse;
import com.example.exam_system.features.authentication.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/role")
    public APIResponse<List<RoleResponse>> getAll(){
        APIResponse<List<RoleResponse>> apiResponse = APIResponse.<List<RoleResponse>>builder()
                .code(200)
                .data(roleService.getAllRole())
                .build();
        return apiResponse;
    }

    @PostMapping ("/role")
    public APIResponse<RoleResponse> create(@RequestBody RoleRequest request){
        APIResponse<RoleResponse> apiResponse = APIResponse.<RoleResponse>builder()
                .code(200)
                .message("Create Success")
                .data(roleService.createRole(request))
                .build();
        return apiResponse;
    }

    @DeleteMapping("/role/{ids}")
    public APIResponse<Void> delete(@PathVariable String ids){
        roleService.deleteRole(ids);
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .code(200)
                .message("Delete Success")
                .build();
        return apiResponse;
    }
}
