package com.sawiya.authservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Achintha Kalunayaka
 * @since 5/2/2025
 */

@Entity
@AllArgsConstructor
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @Column(nullable = false)
        private String firstName;

        @Column(nullable = false)
        private String lastName;


        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(updatable = false)
        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
                createdAt = LocalDateTime.now();
        }
}
