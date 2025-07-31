package com.fitnessApp.userService.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")//postgres has internal table as user so we will name ours users
@Data //it will help to generate all the getters,setters,constructors etc
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String keycloakId;

    @Column(unique = true, nullable = false)
    private String emailId;

    private String userName;
    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    private String phoneNo;

    private LocalDateTime DOB;

    @CreationTimestamp//when an entity is inserted into the DB this field will get automatically inserted
    private LocalDateTime createdAt;

    @UpdateTimestamp//similar to creation if updated it will be inserted
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;


}
