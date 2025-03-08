package com.river.accounts.controller;

import com.river.accounts.dto.CustomerDetailsDto;
import com.river.accounts.dto.ErrorResponseDto;
import com.river.accounts.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Customer Controller", description = "CRUD operations for customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(path = "/fetch-customer-details")
    @Operation(
            summary = "Get customer",
            description = "Get customer by mobile number")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status ok"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number is not valid")
            @Schema(example = "1234567890")
            String mobileNumber) {

        CustomerDetailsDto customerDetailsDto = customerService.fetchCustomerDetails(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDetailsDto);
    }

}
