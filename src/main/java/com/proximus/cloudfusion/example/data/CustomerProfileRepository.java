package com.proximus.cloudfusion.example.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfileEntity, UUID> {
}
