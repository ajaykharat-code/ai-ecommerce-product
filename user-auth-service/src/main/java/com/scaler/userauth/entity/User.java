package com.scaler.userauth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    // Can be null if OAuth2 login
    private String password;
    
    private String provider; // "LOCAL", "GOOGLE"
    
    private String role; // "ROLE_USER"
}
