package com.karthick.Expenz.security;

import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserSession {
    private UserService userService;

    public long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            return user.getId();
        }
        return SecurityConstants.NOT_FOUND;
    }
}
