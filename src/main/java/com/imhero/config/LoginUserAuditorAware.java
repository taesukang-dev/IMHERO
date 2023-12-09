package com.imhero.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginUserAuditorAware implements AuditorAware<String> {

    private final HttpSession httpSession;

    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext context = (SecurityContext) httpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (Objects.isNull(context)) {
            return Optional.empty();
        }
        String userName = context.getAuthentication().getName();

        if (userName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userName);
    }
}