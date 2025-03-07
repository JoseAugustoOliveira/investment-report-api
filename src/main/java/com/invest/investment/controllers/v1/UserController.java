package com.invest.investment.controllers.v1;

import com.invest.investment.entites.mongo.User;
import com.invest.investment.models.requests.CreateUserRequest;
import com.invest.investment.models.responses.CreatedUserResponse;
import com.invest.investment.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User")
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Register new User")
    public ResponseEntity<CreatedUserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        var response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "Get User by cpf")
    public CreatedUserResponse getUserByCpf(@PathVariable String cpf) {
        return userService.getUserByCpf(cpf);
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<User> getAllUsers() {
        return userService.listAllUsers();
    }

    @PutMapping("/update-user")
    @Operation(summary = "Update User by cpf")
    public ResponseEntity<String> updateUserByCpf(@Valid @RequestBody CreateUserRequest request) {
        userService.updatedUserByCpf(request);
        return ResponseEntity.ok().body("User updated successfully");
    }

    @PutMapping("/inactive-user/{cpf}")
    @Operation(summary = "Inactive User by cpf")
    public ResponseEntity<String> inactiveUserByCpf(@PathVariable String cpf) {
        String response = userService.inactiveUserByCpf(cpf);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reactive-user/{cpf}")
    @Operation(summary = "Reactive User by cpf")
    public ResponseEntity<String> reactiveUserByCpf(@PathVariable String cpf) {
        String response = userService.reactiveUserByCpf(cpf);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "Delete User by cpf")
    public ResponseEntity<String> deleteUserByCpf(@PathVariable String cpf) {
        userService.deleteByCpf(cpf);
        return ResponseEntity.ok().build();
    }
}
