//package com.example.flashcardtool.service;
//
//import com.example.flashcardtool.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MockUserService implements UserDetailsService {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder; // Inject the PasswordEncoder
//
//    private List<User> mockUsers = new ArrayList<>();
//
//    public MockUserService() {
//        // You can't use passwordEncoder directly here because it's not yet initialized.
//        // Move the user creation logic to a method called after the bean initialization.
//    }
//
//    // Initialization method
//    @Autowired
//    public void initializeMockUsers() {
//        User user1 = new User();
//        user1.setId("1");
//        user1.setUsername("teacher_tester");
//        user1.setPassword(passwordEncoder.encode("teacher_tester")); // Use the encoder here
//        user1.setEmail("teacher@test.com");
//        user1.setRoles(List.of("TEACHER"));
//
//        User user2 = new User();
//        user2.setId("2");
//        user2.setUsername("student_tester");
//        user2.setPassword(passwordEncoder.encode("student_tester"));
//        user2.setEmail("student@test.com");
//        user2.setRoles(List.of("STUDENT"));
//
//        mockUsers.add(user1);
//        mockUsers.add(user2);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = mockUsers.stream()
//                .filter(u -> u.getUsername().equals(username))
//                .findFirst()
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                new ArrayList<>()
//        );
//    }
//
//    public User findUserById(String username) {
//        return mockUsers.stream()
//                .filter(user -> user.getUsername().equals(username))
//                .findFirst()
//                .orElse(null);
//    }
//
//    public List<User> findAllUsers() {
//        return mockUsers;
//    }
//}
