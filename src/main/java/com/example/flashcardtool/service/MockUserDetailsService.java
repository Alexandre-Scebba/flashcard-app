//package com.example.flashcardtool.service;
//
//import com.example.flashcardtool.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Service
//@Primary
//public class MockUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private MockUserService mockUserService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = mockUserService.findUserById(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User Not Found with username: " + username);
//        }
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
//    }
//}
