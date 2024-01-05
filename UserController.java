package com.hositamtam.plypockets.controller;

import com.hositamtam.plypockets.dto.LoginDto;
import com.hositamtam.plypockets.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")

public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인&회원가입", description = "사용자의 정보가 DB존재 유무에 따라 회원가입/로그인이 성립됩니다.")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        ResponseEntity<?> responseEntity = userService.loginUser(loginDto);

        return responseEntity;
    }

}
