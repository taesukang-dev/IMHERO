package com.imhero.user.service;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.user.domain.Role;
import com.imhero.user.domain.User;
import com.imhero.user.dto.UserDto;
import com.imhero.user.dto.request.LoginRequest;
import com.imhero.user.dto.request.UserRequest;
import com.imhero.user.dto.response.LoginResponse;
import com.imhero.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto save(UserRequest userRequest) {
        User user;
        try {
            user = userRepository.save(User.of(
                    userRequest.getEmail(),
                    bCryptPasswordEncoder.encode(userRequest.getPassword()),
                    userRequest.getUsername(),
                    "N"));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        } catch (Exception e) {
            throw new IllegalArgumentException("올바르지 않은 요청입니다.");
        }
        return UserDto.from(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        User user = getUserByEmailOrElseThrow(loginRequest.getEmail());
        return LoginResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserDto findUserByEmail(String email) {
        return UserDto.from(getUserByEmailOrElseThrow(email));
    }

    public UserDto update(UserRequest userRequest) {
        return UserDto
                .from(getUserByEmailOrElseThrow(userRequest.getEmail())
                        .modify(userRequest.getEmail(),
                                userRequest.getPassword(),
                                userRequest.getUsername(),
                                Role.USER));
    }

    public boolean withdraw(String email) {
        User user = getUserByEmailOrElseThrow(email);
        return user.withdraw();
    }

    @Transactional(readOnly = true)
    public User getUserByEmailOrElseThrow(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
    }

    @Transactional(readOnly = true)
    public User getUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.USER_NOT_FOUND));
    }

}
