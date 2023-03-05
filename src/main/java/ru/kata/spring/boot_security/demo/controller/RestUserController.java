package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exceptionhandling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RestUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " +
                    id + " in the Database");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);

        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id + " in the Database");
        }
        userService.deleteById(id);
        return new ResponseEntity<>("User with ID = " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getAuthenticatedUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
