package com.example.authapi.controller;

import com.example.authapi.dto.CreateUserDTO;
import com.example.authapi.dto.LoginUserDTO;
import com.example.authapi.dto.RecoveryJwtTokenDTO;
import com.example.authapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Endpoints endpoints para autenticação, registros de usuários e testes")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuário", description = "Autentica um usuário com base nas credenciais fornecidas e retorna um token JWT.")
    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso")
    public ResponseEntity<RecoveryJwtTokenDTO> authenticateUser(@RequestBody LoginUserDTO loginUserDto) {
        RecoveryJwtTokenDTO token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "Registro de usuário", description = "Registra um novo usuário no sistema.")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDTO createUserDto) {
        userService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/test")
    @Operation(summary = "Teste de autenticação", description = "Testa a autenticação de um usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/customer")
    @Operation(summary = "Teste de autenticação de cliente", description = "Testa o retorno da autenticação de um cliente.")
    @ApiResponse(responseCode = "200", description = "Cliente autenticado com sucesso")
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/administrator")
    @Operation(summary = "Teste de autenticação de administrador", description = "Testa o retorno da autenticação de um administrador.")
    @ApiResponse(responseCode = "200", description = "Administrador autenticado com sucesso")
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
    }

}
