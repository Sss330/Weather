package org.example.wearher;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.UserRepository;
import service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class  UserAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUserSuccess() {
        String username = "testUser";
        String password = "testPass";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User mockUser = new User(1L, username, hashedPassword);

        when(userRepository.getUserByLogin(username)).thenReturn(Optional.of(mockUser));

        try (var mockBCrypt = mockStatic(BCrypt.class)) {
            mockBCrypt.when(() -> BCrypt.checkpw(password, mockUser.getPassword())).thenReturn(true);

            Optional<User> result = userService.findUserByLogin(username);

            assertTrue(result.isPresent());
            assertEquals(username, result.get().getLogin());
        }

        verify(userRepository, times(1)).getUserByLogin(username);
    }

    @Test
    void testAuthenticateUser_Failure() {
        String username = "testUser";
        String password = "wrongPass";
        String hashedPassword = BCrypt.hashpw("correctPass", BCrypt.gensalt());
        User mockUser = new User(1L, username, hashedPassword);

        when(userRepository.getUserByLogin(username)).thenReturn(Optional.of(mockUser));

        try (var mockBCrypt = mockStatic(BCrypt.class)) {
            mockBCrypt.when(() -> BCrypt.checkpw(password, mockUser.getPassword())).thenReturn(false);

            Optional<User> user = userService.findUserByLogin(username);
            boolean isAuthenticated = user.map(u -> userService.isPasswordCorrect(password, u)).orElse(false);

            assertFalse(isAuthenticated);
        }

        verify(userRepository, times(1)).getUserByLogin(username);
    }



    @Test
    void testRegisterUser() {
        String username = "newUser";
        String password = "newPass";
        User newUser = new User(null, username, password);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        doNothing().when(userRepository).save(any(User.class));

        User result = userService.saveUser(newUser);
        assertNotNull(result);
        assertEquals(username, result.getLogin());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(username, capturedUser.getLogin());
        assertNotEquals(password, capturedUser.getPassword());
    }
}
