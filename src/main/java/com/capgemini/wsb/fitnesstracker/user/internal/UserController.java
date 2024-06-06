package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {
    private final UserServiceImpl userService;
    @GetMapping
    @ResponseBody
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/delete/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }

    @PostMapping("/update")
    public void updateUserEmail(@RequestBody UserDto userDto){
        userService.updateUser(userDto);
    }

    @GetMapping("/user-details/{userId}")
    public Optional<User> getUserDetails(@PathVariable long userId){
        return userService.getUserDetails(userId);
    }

    @GetMapping("/email/{emailPrefix}")
    public List<UserDto> getAllUsersByEmail(@PathVariable String emailPrefix){
        return userService.findAllUsersWithEmailPrefix(emailPrefix);
    }

    @GetMapping("/age/{age}")
    public List<UserDto> getAllUsersAgeAbove(@PathVariable long age){
        return userService.findAllUsersWithAgeAbove(age);
    }

    @PostMapping
    public User addUser(@RequestBody UserDto userDto) throws InterruptedException {
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");
        return userService.createUser(userDto);
    }

}