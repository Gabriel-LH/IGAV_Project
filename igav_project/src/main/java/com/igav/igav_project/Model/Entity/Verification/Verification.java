package com.igav.igav_project.Model.Entity.Verification;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "verification")
public class Verification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Embedded
    @Column(name = "email_identifier", nullable = false)
    public EmailIdentifier emailIdentifier;

    @Embedded
    @Column(name = "verification_code", nullable = false)
    public VerificationCode verificationCode;

    public LocalDateTime createdAt;
    
    public LocalDateTime expiresAt;
    
    public LocalDateTime updatedAt;

    protected Verification(){

    }

    private Verification(
        EmailIdentifier emailIdentifier, 
        VerificationCode verificationCode,
        LocalDateTime createdAt,
        LocalDateTime expiredAt,
        LocalDateTime updatedAt
        ) 
        {
        this.emailIdentifier = emailIdentifier;
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
        this.expiresAt = expiredAt;
        this.updatedAt = expiredAt;
    }

    

    public static Verification create(
        EmailIdentifier emailIdentifier,
        VerificationCode verificationCode,
        LocalDateTime createdAt,
        LocalDateTime expiredAt,
        LocalDateTime updatedAt
    ){
        return new Verification(
            emailIdentifier,
            verificationCode,
            createdAt,
            expiredAt,
            updatedAt
        );
    }

    public Long getId() {return id;}
    public EmailIdentifier getEmailIdentifier() {return emailIdentifier;}
    public VerificationCode getVerificationCode() {return verificationCode;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getExpiresAt() {return expiresAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}

}
