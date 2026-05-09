package com.backend_desigeo.desigeo_auth_service.service;

import com.backend_desigeo.desigeo_auth_service.dto.CreateUserRequest;
import com.backend_desigeo.desigeo_auth_service.dto.UserDTO;
import com.backend_desigeo.desigeo_auth_service.entity.Role;
import com.backend_desigeo.desigeo_auth_service.entity.User;
import com.backend_desigeo.desigeo_auth_service.exception.UserAlreadyExistsException;
import com.backend_desigeo.desigeo_auth_service.repository.RoleRepository;
import com.backend_desigeo.desigeo_auth_service.repository.UserRepository;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Integer publicDefaultRoleId;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${auth.public-default-role-id}") Integer publicDefaultRoleId) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.publicDefaultRoleId = publicDefaultRoleId;
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user = new User();
        user.setUserId(java.util.UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        Role defaultRole = roleRepository.findById(publicDefaultRoleId)
            .orElseThrow(() -> new IllegalStateException("Default public role not found: " + publicDefaultRoleId));
        user.setRoleId(defaultRole.getRoleId());
        user.setActive(Boolean.TRUE.equals(request.getActive()));
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(Instant.now());

        return mapToDTO(userRepository.save(user));
    }

    private UserDTO mapToDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setRoleId(user.getRoleId());
        dto.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : null);
        dto.setActive(user.isActive());
        return dto;
    }
}
