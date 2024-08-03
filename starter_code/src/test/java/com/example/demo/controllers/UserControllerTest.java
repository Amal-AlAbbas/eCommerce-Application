package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
    @Before
    public void setup(){
        userController = new UserController();
        userDetailsService = new UserDetailsServiceImpl();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        TestUtils.injectObjects(userDetailsService, "userRepository", userRepository);

    }
    @Test
    public void create_user_happy_path(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }
    @Test
    public void findById(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        when(userRepository.findById(u.getId())).thenReturn(java.util.Optional.ofNullable(u));
        final ResponseEntity<User> res = userController.findById(u.getId());
        User u1 = res.getBody();
        assertNotNull(u1);
        assertEquals(u1.getId(), u.getId());
        assertEquals(u1.getUsername(), u.getUsername());
        assertEquals(u1.getPassword(), u.getPassword());
    }
    @Test
    public void findByUserName() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        User u = response.getBody();
        assertNotNull(u);
        when(userRepository.findByUsername("test")).thenReturn(u);

        final ResponseEntity<User> res = userController.findByUserName("test");

        User u1 = res.getBody();
        assertNotNull(u1);
        assertEquals(u1.getId(), u.getId());
        assertEquals(u1.getUsername(), u.getUsername());
        assertEquals(u1.getPassword(), u.getPassword());
    }

    @Test

    public void userDetailsTest(){
        User user = new User();
        user.setUsername("test");
        String password = "password";
        user.setPassword(password);
        user.setId(0L);
        when(userRepository.findByUsername("test")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("test");
        assertNotNull(userDetails);
        Collection<? extends GrantedAuthority> authorityCollection = userDetails.getAuthorities();
        assertNotNull(authorityCollection);
        assertEquals(0, authorityCollection.size());
        assertEquals(password, userDetails.getPassword());
        assertEquals("test", userDetails.getUsername());
    }
}
