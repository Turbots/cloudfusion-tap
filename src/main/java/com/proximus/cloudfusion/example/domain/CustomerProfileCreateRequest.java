package com.proximus.cloudfusion.example.domain;


import jakarta.validation.constraints.NotBlank;

public record CustomerProfileCreateRequest(@NotBlank(message = "First name cannot be empty.") String firstName,
                                           @NotBlank(message = "Last name cannot be empty.") String lastName,
                                           @NotBlank(message = "Email cannot be empty.") String email) {

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
