package com.capgemini.wsb.fitnesstracker.user.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService = new UserServiceImpl(null, null);
    private UserController userController;
    private final long USER_ID = 101L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void getAllUsers() {
        userController.getAllUsers();
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void deleteUser() {
        userController.deleteUser(USER_ID);
        verify(userService, times(1)).deleteUser(USER_ID);
    }

    @Test
    void updateUserEmail() {
        var userDto = new UserDto(USER_ID, "Mark", "Darcy", LocalDate.of(2000, 01, 15), "email@email.com");
        userController.updateUserEmail(userDto);
        verify(userService, times(1)).updateUser(userDto);
    }

    @Test
    void getUserDetails() {
        userController.getUserDetails(USER_ID);
        verify(userService, times(1)).getUserDetails(USER_ID);
    }

    @Test
    void getAllUsersByEmail() {
        var prefix = "prefix";
        userController.getAllUsersByEmail(prefix);
        verify(userService, times(1)).findAllUsersWithEmailPrefix(prefix);
    }

    @Test
    void getAllUsersAgeAbove() {
        var age = 55;
        userController.getAllUsersAgeAbove(age);
        verify(userService, times(1)).findAllUsersWithAgeAbove(age);
    }

    @Test
    void addUser() throws InterruptedException {
        var userDto = new UserDto(null, "Mark", "Darcy", LocalDate.of(2000, 01, 15), "email@email.com");
        userController.addUser(userDto);
        verify(userService, times(1)).createUser(userDto);
    }
}