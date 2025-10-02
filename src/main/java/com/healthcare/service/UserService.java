package com.healthcare.service;

import com.healthcare.entity.User;
import com.healthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    
    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public List<User> findByRole(User.Role role) {
        return userRepository.findByRole(role);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    public User updateProfile(User user, String newUsername, String newPassword) {
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            user.setUsername(newUsername.trim());
        }
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.save(user);
    }
    
    public User createUser(String username, String password, String fullName, String email, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        user.setEnabled(true);
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}