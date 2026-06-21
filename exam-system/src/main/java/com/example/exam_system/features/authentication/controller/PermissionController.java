package com.example.exam_system.features.authentication.controller;


import com.example.exam_system.features.authentication.dto.request.PermissionRequest;
import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.authentication.dto.response.PermissionResponse;
import com.example.exam_system.features.authentication.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class PermissionController {
   private final PermissionService permissionService;

   @GetMapping("/permission")
    public APIResponse<List<PermissionResponse>> getAllPermession(){
       APIResponse<List<PermissionResponse>> response = APIResponse.<List<PermissionResponse>>builder()
               .code(200)
               .data(permissionService.getAll())
               .build();
       return response;
   }

    @PostMapping("/permission")
    public APIResponse<PermissionResponse> creatPermission(@RequestBody PermissionRequest request){
        APIResponse<PermissionResponse> response = APIResponse.<PermissionResponse>builder()
                .code(200)
                .message("Create Success")
                .data(permissionService.create(request))
                .build();
        return response;
    }

    @DeleteMapping("/permission/{ids}")
    public APIResponse<PermissionResponse> creatPermission(@PathVariable String ids){
        permissionService.delete(ids);
        APIResponse<PermissionResponse> response = APIResponse.<PermissionResponse>builder()
                .code(200)
                .message("Delete Success")
                .build();
        return response;
    }

}
