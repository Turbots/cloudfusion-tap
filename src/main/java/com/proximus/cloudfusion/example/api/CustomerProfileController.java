package com.proximus.cloudfusion.example.api;

import com.proximus.cloudfusion.example.domain.CustomerProfileCreateRequest;
import com.proximus.cloudfusion.example.domain.CustomerProfileResponse;
import com.proximus.cloudfusion.example.domain.CustomerProfileService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@OpenAPIDefinition(
        info = @Info(
                title = "Customer Profile Management API",
                version = "1.0"),
        tags = @Tag(
                name = "Customer Profile REST API"))
@CrossOrigin
@RestController
@RequestMapping("/api/customer-profiles")
public class CustomerProfileController {

    private final CustomerProfileService service;

    public CustomerProfileController(CustomerProfileService service) {
        this.service = service;
    }

    @Operation(summary = "Saves provided customer profile.", method = "POST", tags = "Customer Profile CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer profile successfully saved.",
                    headers = @Header(
                            name = "Location",
                            description = "Contains path which can be used to retrieve saved profile. Last element is it's ID.",
                            required = true,
                            schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Passed customer profile is invalid."
            )
    })
    @PostMapping
    public ResponseEntity<CustomerProfileResponse> create(@Valid @RequestBody CustomerProfileCreateRequest body) {
        var customerProfileResponse = service.create(body);
        return ResponseEntity
                .created(URI.create("/api/customer-profiles/" + customerProfileResponse.id()))
                .body(customerProfileResponse);
    }

    @Operation(summary = "Get customer profile.", method = "GET", tags = "Customer Profile CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer profile retrieved successfully."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer profile not found."
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerProfileResponse> get(@PathVariable("id") String id) {
        var customerProfileResponse = service.getById(id);
        return customerProfileResponse.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(customerProfileResponse.get());
    }
}
