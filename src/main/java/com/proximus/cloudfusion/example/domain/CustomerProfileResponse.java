package com.proximus.cloudfusion.example.domain;

import jakarta.validation.constraints.NotBlank;

public record CustomerProfileResponse(@NotBlank String id, String firstName, String lastName, @NotBlank String email) {

    public CustomerProfileResponse(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String email() {
        return email;
    }
}
