package com.imhero.user.controller;

import com.imhero.config.exception.Response;
import com.imhero.user.components.AuthenticatedUser;
import com.imhero.user.dto.UserDto;
import com.imhero.user.dto.request.LoginRequest;
import com.imhero.user.dto.request.UserRequest;
import com.imhero.user.dto.response.LoginResponse;
import com.imhero.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticatedUser authenticatedUser;

    @PostMapping("/api/v1/users")
    public Response<UserDto> join(@RequestBody UserRequest userRequest) {
        return Response.success(userService.save(userRequest));
    }

    @PostMapping("/api/v1/users/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return Response.success(userService.login(loginRequest));
    }
}
