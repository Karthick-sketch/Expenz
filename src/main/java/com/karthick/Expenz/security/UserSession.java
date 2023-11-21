package com.karthick.Expenz.security;

import com.karthick.Expenz.common.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSession {
    public long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImp userDetails) {
                return userDetails.getId();
            }
        }
        return Constants.NOT_FOUND;
    }
}
