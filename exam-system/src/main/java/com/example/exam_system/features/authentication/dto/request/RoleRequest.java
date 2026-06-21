package com.example.exam_system.features.authentication.dto.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequest {
    private String name;
    private String description;

    // Tạo role kèm Permession
    Set<String> permissionName;
}
