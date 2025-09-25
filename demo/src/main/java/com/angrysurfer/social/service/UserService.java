package com.angrysurfer.social.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.angrysurfer.broker.spi.BrokerOperation;
import com.angrysurfer.broker.spi.BrokerParam;
import com.angrysurfer.social.ResourceNotFoundException;
import com.angrysurfer.social.dto.UserDTO;
import com.angrysurfer.social.model.Profile;
import com.angrysurfer.social.model.User;
import com.angrysurfer.social.repository.ProfileRepository;
import com.angrysurfer.social.repository.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Service("userService")
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public record TestUser(Long id, String email, String name) {

    }

    public record CreateUserReq(
            @Email String email,
            @NotBlank String name
            ) {

    }

    
    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        log.info("UserService initialized");
    }

    @BrokerOperation("getById")
    public TestUser getById(@BrokerParam("id") Long id) {
        log.info("Get by id {}", id);
        // pretend lookup
        return new TestUser(id, "user" + id + "@acme.com", "User " + id);
    }

    @BrokerOperation("create")
    public Map<String, Object> create(@Valid @BrokerParam("user") CreateUserReq req) {
        log.info("Create user {}", req.email);
        // pretend persistence:
        TestUser created = new TestUser(1001L, req.email(), req.name());
        return Map.of("created", created);
    }

    public void delete(Long userId) {
        log.info("Delete user id {}", userId);
        userRepository.deleteById(userId);
    }

    public Set<UserDTO> findAll() {
        log.info("Find all users");
        HashSet<User> result = new HashSet<>();
        userRepository.findAll().forEach(result::add);
        return result.stream().map(user -> user.toDTO()).collect(Collectors.toSet());
    }

    public UserDTO findById(Long userId) throws ResourceNotFoundException {
        log.info("Find user by id {}", userId);
        Optional<User> result = userRepository.findById(userId);
        if (result.isPresent()) {
            return result.get().toDTO();
        }

        throw new ResourceNotFoundException("User ".concat(Long.toString(userId).concat(" not found.")));
    }

    public UserDTO findByAlias(String alias) throws ResourceNotFoundException {
        log.info("Find user by alias {}", alias);
        UserDTO result;

        Optional<User> user = userRepository.findByAlias(alias);
        if (user.isPresent()) {
            Optional<Profile> profile = profileRepository.findByUserId(user.get().getId());
            if (profile.isPresent()) {
                result = user.get().toDTO();
                result.setProfileImageUrl(profile.get().getProfileImageUrl());
            } else {
                result = user.get().toDTO();
            }

            return result;
        }

        throw new ResourceNotFoundException("User ".concat(alias).concat(" not found."));
    }

    public UserDTO findByEmail(String email) throws ResourceNotFoundException {
        log.info("Find user by email {}", email);
        UserDTO result;

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Optional<Profile> profile = profileRepository.findByUserId(user.get().getId());
            if (profile.isPresent()) {
                result = user.get().toDTO();
                result.setProfileImageUrl(profile.get().getProfileImageUrl());
            } else {
                result = user.get().toDTO();
            }

            return result;
        }
        throw new ResourceNotFoundException("User ".concat(email).concat(" not found."));
    }

    public UserDTO save(@BrokerParam("alias") String alias, @BrokerParam("email") String email, @BrokerParam("password") String initialPassword) {
        log.info("Save user {}", alias);
        return userRepository.save(new User(alias, email, null)).toDTO();
    }

    public UserDTO save(UserDTO newUser) {
        log.info("Save user {}", newUser.getAlias());
        User user = new User(newUser.getAlias(), newUser.getEmail(), newUser.getAvatarUrl());
        return userRepository.save(user).toDTO();
    }

    public UserDTO update(User user) {
        log.info("Update user {}", user.getAlias());
        return userRepository.save(user).toDTO();
    }

}
