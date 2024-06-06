package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    private UserServiceImpl userService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, new UserMapper());

        user1 = new User(1L,
                "firstName1",
                "lastName1",
                LocalDate.of(2000, 1, 15),
                "email");
        user2 = new User(2L,
                "firstName2",
                "lastName2",
                LocalDate.of(1980, 1, 15),
                "email2");
        when(userRepository.findAll()).thenReturn(
                List.of(
                        user1,
                        user2,
                        new User(3L,
                                "firstName3",
                                "lastName3",
                                LocalDate.of(1950, 2, 16),
                                "thisIsDifferent")));
    }

    @Test
    void findAllUsersWithEmailPrefix() {
        var emailPrefix = userService.findAllUsersWithEmailPrefix("ema");
        assertEquals(2, emailPrefix.size());
        var foundEmails = emailPrefix
                .stream()
                .map(UserDto::email)
                .collect(Collectors.toSet());
        assertEquals(Set.of("email", "email2"), foundEmails);

        var differentMail = userService.findAllUsersWithEmailPrefix("this");
        assertEquals(1, differentMail.size());
    }

    @Test
    void updateUser() {
        userService.updateUser(new UserDto(202L, "name", "name2", LocalDate.of(1999, 9, 19), "newEmail"));
        verify(userRepository, times(1)).updateUserEmail("newEmail", 202L);
    }

    @Test
    void deleteUser() {
        var userId = 2L;
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void createUser() {
        var userDto = new UserDto(null, "firstName", "lastName", LocalDate.of(2000, 5, 30), "someMail");
        userService.createUser(userDto);
        verify(userRepository, times(1)).save(new User("firstName", "lastName", LocalDate.of(2000, 5, 30), "someMail"));
    }

    @Test
    void createUserExistingId() {
        var userDto = new UserDto(2L, "firstName", "lastName", LocalDate.of(2000, 5, 30), "someMail");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDto));
    }

    @Test
    void getUserDetails() {
        long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user2));
        var userDetails = userService.getUserDetails(2L);
        assertTrue(userDetails.isPresent());
        var user = userDetails.get();
        assertEquals(2L, user.getId());
        assertEquals("firstName2", user.getFirstName());
        assertEquals("lastName2", user.getLastName());
        assertEquals("email2", user.getEmail()); // since we want to provide all information about user
        assertEquals(LocalDate.of(1980, 1, 15), user.getBirthdate());
    }

    @Test
    void findAllUsers() {
        var allUsers = userService.findAllUsers();
        assertEquals(3, allUsers.size());
        var userDto = allUsers.get(0);
        assertEquals(1L, userDto.Id());
        assertEquals("firstName1", userDto.firstName());
        assertEquals("lastName1", userDto.lastName());
        assertNull(userDto.email()); // since we want to provide only id and user's names
        assertNull(userDto.birthdate());
        var userDto1 = allUsers.get(1);
        assertEquals(2L, userDto1.Id());
        var userDto2 = allUsers.get(2);
        assertEquals(3L, userDto2.Id());
    }

    @Test
    void findAllUsersWithAgeAbove() {
        var users100yo = userService.findAllUsersWithAgeAbove(100L);
        assertEquals(0, users100yo.size());
        var usersOlderThan1Year = userService.findAllUsersWithAgeAbove(1L);
        assertEquals(3L, usersOlderThan1Year.size());
        var usersOlderThan25Years = userService.findAllUsersWithAgeAbove(25L);
        assertEquals(2, usersOlderThan25Years.size());
        assertEquals(Set.of(2L, 3L), usersOlderThan25Years.stream().map(UserDto::Id).collect(Collectors.toSet()));
        var userOlderThan50Years = userService.findAllUsersWithAgeAbove(50L);
        assertEquals(1L, userOlderThan50Years.size());
        assertEquals(3L, userOlderThan50Years.get(0).Id());
    }
}