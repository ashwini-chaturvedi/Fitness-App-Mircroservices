package com.fitnessApp.userService.repository;

import com.fitnessApp.userService.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String>, JpaRepository<User, String> {

    boolean existsByEmailId(@NotBlank(message = "Email Id is Required") @Email(message = "Invalid Email Format") String emailId);


    User findByEmailId(@NotBlank(message = "Email Id is Required") @Email(message = "Invalid Email Format") String emailId);

    boolean existsByKeycloakId(String keycloakId);

    User findByKeycloakId(String keycloakId);
}
