package com.example.exam_system.features.authentication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "invalidate_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidateToken {
    @Id
    private String id;
    private Instant expirationTime;
}
