package com.example.exam_system.features.account.entity;

import com.example.exam_system.features.authentication.entity.Role;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    UUID id;

    @Column(name = "username", unique = true, nullable = false)
    String userName;

    @Column(name = "full_name")
    String fullName;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;


    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    Date createdAt ;

    @OneToMany(mappedBy = "user")
    List<ExamAttempt> examAttempts = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    Set<Role> roles = new HashSet<>();
}
