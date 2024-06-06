package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAllUsersWithEmailPrefix(String emailPrefix) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user
                        .getEmail()
                        .toLowerCase()
                        .startsWith(emailPrefix.toLowerCase()))
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto) {
        userRepository.updateUserEmail(userDto.email(), Objects.requireNonNull(userDto.Id()));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        log.info("Creating User {}", userDto);
        if (userDto.Id() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        var user = userMapper.toEntity(userDto);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserDetails(final Long userId) {
        return userRepository.findById(userId);
    }


    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toAllUsersMapping)
                .sorted(Comparator.comparing(userDto -> Objects.requireNonNull(userDto.Id())))
                .toList();
    }

    @Override
    public List<UserDto> findAllUsersWithAgeAbove(long age) {
        var minimalBirthdayDate = LocalDate.now().minusYears(age);
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getBirthdate().isBefore(minimalBirthdayDate))
                .map(userMapper::toAllUsersMapping)
                .toList();
    }
}