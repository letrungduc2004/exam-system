package com.example.exam_system.features.authentication.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    private String name;
    private String description;

    // Tạo role kèm Permession
    Set<String> permissionName;
}
