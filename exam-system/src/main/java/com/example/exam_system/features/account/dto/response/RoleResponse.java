package com.example.exam_system.features.account.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    @JsonProperty("name")
    private String roleName;
    private String description;
    private Set<String> permissionName;
}
