package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class MainController {

    private UserServiceImpl userService;
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @Autowired
    public void setRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public MainController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String user(ModelMap model, Principal principal) {

        User authenticatedUser = userService.getUserByEmail(principal.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("authenticatedUserRoles", authenticatedUser.getRoles());

        return "user";
    }

    @GetMapping("/admin")
    public String admin(ModelMap model, @ModelAttribute("newUser") User newUser,
                        Principal principal) {
        User authenticatedUser = userService.getUserByEmail(principal.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("authenticatedUserRoles", authenticatedUser.getRoles());
        model.addAttribute("users", userService.listAll());
        model.addAttribute("roles", roleRepository.findAll());

        return "admin";
    }

    // New user
    @PostMapping("/admin")
    public String create(@ModelAttribute("user") User newUser) {
        // Encrypt password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userService.save(newUser);
        return "redirect:/admin";
    }


    // Edit user
    @PatchMapping("/admin/edit")
    public String update(@ModelAttribute("editedUser") User editedUser) {
        userService.update(editedUser);
        return "redirect:/admin";
    }

    // Delete user
    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }


}


