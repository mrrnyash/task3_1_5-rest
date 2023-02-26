package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private UserService userService;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        List<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUserRoles", currentUserRoles);
        return "user";
    }

    @GetMapping("/admin")
    public String admin(ModelMap model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());
        model.addAttribute("currentUser", userService.getUserByEmail(userDetails.getUsername()));
        model.addAttribute("currentUserRoles", currentUserRoles);
        model.addAttribute("users", userService.listAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleRepository.findAll());

        return "admin";
    }

    // New user
    @PostMapping("/admin")
    public String create(@ModelAttribute("user") User user) {
        List<Role> roles = roleRepository.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin";
    }


    // Edit user
    @PatchMapping("/admin/{id}")
    public String update(ModelMap model, @ModelAttribute("user") User updatedUser,
                         @PathVariable("id") Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());


        model.addAttribute("currentUserRoles", currentUserRoles);

        User currentUpdatedUser = userService.getById(id);
        currentUpdatedUser.setFirstName(updatedUser.getEmail());
        currentUpdatedUser.setLastName(updatedUser.getLastName());
        currentUpdatedUser.setAge(updatedUser.getAge());
        currentUpdatedUser.setEmail(updatedUser.getEmail());
        currentUpdatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        currentUpdatedUser.setRoles(updatedUser.getRoles());

        userService.update(id, currentUpdatedUser);
        return "redirect:/admin";
    }

    // Delete user
    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }


}


