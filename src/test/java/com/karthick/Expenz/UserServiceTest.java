package com.karthick.Expenz;

import com.karthick.Expenz.entity.User;
import com.karthick.Expenz.exception.BadRequestException;
import com.karthick.Expenz.repository.UserRepository;
import com.karthick.Expenz.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User getTestUserData() {
        User user = new User();
        user.setId(1);
        user.setUsername("Kang");
        user.setEmail("kang@marvel.com");
        user.setPassword("encrypted password");
        return user;
    }

    @Test
    public void testFindUserById() {
        User mockUser = getTestUserData();
        when(userRepository.findById(mockUser.getId())).thenReturn((Optional.of(mockUser)));

        User validUser = userService.findUserById(mockUser.getId());
        Executable wrongId = () -> userService.findUserById(2);

        assertEquals(mockUser, validUser);
        assertThrows(NoSuchElementException.class, wrongId);
    }

    @Test
    public void testCreateNewUser() {
        User mockUser = getTestUserData();
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(passwordEncoder.encode(mockUser.getPassword())).thenReturn("encrypted password");

        User user = userService.createNewUser(mockUser);

        assertEquals(mockUser, user);
        verify(userRepository, times(1)).save(mockUser);
    /*
        # need to clarify how to pass invalid type to primitive types
        Executable invalidUser = () -> userService.createNewUser(mockUser);
        assertThrows(BadRequestException.class, invalidUser);
     */
    }

    @Test
    public void testUpdateUserByFields() {
        User mockUser = getTestUserData();
        when(userRepository.findById(mockUser.getId())).thenReturn((Optional.of(mockUser)));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        Map<String, Object> updatedFields = Map.of("email", "kangtheconqueror@marvel.com");
        User validUser = userService.updateUserByFields(mockUser.getId(), updatedFields);
        Executable wrongId = () -> userService.updateUserByFields(2, updatedFields);

        Map<String, Object> invalidFieldType = Map.of("email", 616);
        Executable invalidUser = () -> userService.updateUserByFields(mockUser.getId(), invalidFieldType);

        assertEquals(mockUser, validUser);
        assertThrows(NoSuchElementException.class, wrongId);
        assertThrows(BadRequestException.class, invalidUser);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    public void testDeleteUserById() {
        User mockUser = getTestUserData();
        when(userRepository.findById(mockUser.getId())).thenReturn((Optional.of(mockUser)));

        String deleteMsg = userService.deleteUserById(mockUser.getId());
        Executable wrongId = () -> userService.deleteUserById(2);

        assertEquals("user has been deleted", deleteMsg);
        assertThrows(NoSuchElementException.class, wrongId);
        verify(userRepository, times(1)).delete(mockUser);
    }
}
