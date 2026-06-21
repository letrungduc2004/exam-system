package com.example.exam_system.features.account.repository;

import com.example.exam_system.features.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    Optional<User> findByUserName(String username);

//    @Query("select u.userName as UserName, u.password as Password," +
//            "r.name as RoleName, p.name as PermissionName from User u " +
//            "join u.roles r " +
//            "join r.permissions p " +
//            "where u.userName =:name")
//    UserProjection getUserAndRolePermission(@Param("name") String userName);

    @Query("select u from User u " +
            "join fetch u.roles r " +
            "join fetch r.permissions p " +
            "where u.userName =:name")
    Optional<User> getUserAndRolePermission(@Param("name") String userName);
}
