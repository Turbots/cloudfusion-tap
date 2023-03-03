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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        return customerProfileResponse
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all customer profiles.", method = "GET", tags = "Customer Profile CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer profiles retrieved successfully."
            )
    })
    @GetMapping("/")
    public ResponseEntity<List<CustomerProfileResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
