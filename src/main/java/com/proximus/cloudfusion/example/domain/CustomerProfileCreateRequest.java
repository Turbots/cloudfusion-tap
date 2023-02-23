package com.proximus.cloudfusion.example.domain;


import jakarta.validation.constraints.NotBlank;

public record CustomerProfileCreateRequest(String firstName, String lastName, @NotBlank String email) {

    public CustomerProfileCreateRequest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String email() {
        return email;
    }
}
