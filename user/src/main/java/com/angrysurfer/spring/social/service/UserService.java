package com.angrysurfer.spring.social.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.angrysurfer.spring.ResourceNotFoundException;
import com.angrysurfer.spring.broker.spi.BrokerOperation;
import com.angrysurfer.spring.broker.spi.BrokerParam;
import com.angrysurfer.spring.social.dto.UserDTO;
import com.angrysurfer.spring.social.model.Profile;
import com.angrysurfer.spring.social.model.User;
import com.angrysurfer.spring.social.repository.ProfileRepository;
import com.angrysurfer.spring.social.repository.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Service("userService")
public class UserService {

	public record TestUser(Long id, String email, String name) {}
    public record CreateUserReq(
            @Email String email,
            @NotBlank String name
    ) {}

    @BrokerOperation("getById")
    public ResponseEntity<TestUser> getById(@BrokerParam("id") Long id) {
        // pretend lookup
        return ResponseEntity.ok(new TestUser(id, "user"+id+"@acme.com", "User "+id));
    }

    @BrokerOperation("create")
    public ResponseEntity<Map<String, Object>> create(@Valid @BrokerParam("user") CreateUserReq req) {
        // pretend persistence:
        TestUser created = new TestUser(1001L, req.email(), req.name());
        return ResponseEntity.ok(Map.of("created", created));
    }

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	public void delete(Long userId) {
		userRepository.deleteById(userId);
	}

	public Set< UserDTO > findAll() {
		HashSet< User > result = new HashSet<>();
		userRepository.findAll().forEach(result::add);
		return result.stream().map(user -> UserDTO.fromUser(user)).collect(Collectors.toSet());
	}

	public UserDTO findById(Long userId) throws ResourceNotFoundException {
		Optional<User> result = userRepository.findById(userId);
		if (result.isPresent())
			return UserDTO.fromUser(result.get());

		throw new ResourceNotFoundException("User ".concat(Long.toString(userId).concat(" not found.")));
	}

	public UserDTO findByAlias(String alias) throws ResourceNotFoundException {

		UserDTO result;

		Optional<User> user = userRepository.findByAlias(alias);
		if (user.isPresent()) {
			Optional< Profile > profile = profileRepository.findByUserId(user.get().getId());
			if (profile.isPresent()) {
				result = UserDTO.fromUser(user.get());
				result.setProfileImageUrl(profile.get().getProfileImageUrl());
			} else {
				result = UserDTO.fromUser(user.get());
			}

			return result;
		}

		throw new ResourceNotFoundException("User ".concat(alias).concat(" not found."));
	}

	public UserDTO findByEmail(String email) throws ResourceNotFoundException {

		UserDTO result;

		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			Optional< Profile > profile = profileRepository.findByUserId(user.get().getId());
			if (profile.isPresent()) {
				result = UserDTO.fromUser(user.get());
				result.setProfileImageUrl(profile.get().getProfileImageUrl());
			} else {
				result = UserDTO.fromUser(user.get());
			}

			return result;
		}
		throw new ResourceNotFoundException("User ".concat(email).concat(" not found."));
	}

	@BrokerOperation("save")
	public UserDTO save(@BrokerParam("alias") String alias, @BrokerParam("email") String email, @BrokerParam("password") String initialPassword) {
		return UserDTO.fromUser(userRepository.save(new User(alias, email, null)));
	}

	public UserDTO save(UserDTO newUser) {
		return UserDTO.fromUser(userRepository.save(User.fromDTO(newUser)));
	}

	public UserDTO update(User user) {
		return UserDTO.fromUser(userRepository.save(user));
	}

}