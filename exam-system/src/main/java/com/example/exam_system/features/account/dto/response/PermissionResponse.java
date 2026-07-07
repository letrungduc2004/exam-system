package com.example.exam_system.features.account.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionRequest {
    private String permissionName;
    private String description;
}
