package com.farming.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * User Entity - Represents farmers, equipment owners, and admins
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("phone_number")
    private String phoneNumber;

    @Field("full_name")
    private String fullName;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Indexed
    private UserRole role; // FARMER, OWNER, ADMIN

    @Field("address")
    private String address;

    @Field("city")
    private String city;

    @Field("state")
    private String state;

    @Field("pincode")
    private String pincode;

    @Field("profile_image")
    private String profileImage;

    @Field("is_active")
    private Boolean isActive = true;

    @Field("is_blocked")
    private Boolean isBlocked = false;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    // Enum for User Roles
    public enum UserRole {
        FARMER,
        OWNER,
        ADMIN
    }
}
