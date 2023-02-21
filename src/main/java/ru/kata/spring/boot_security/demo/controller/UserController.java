package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

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
    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/user")
    public String user(ModelMap model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = "/admin")
    public String home(ModelMap model) {
        List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping(value = "/admin/new")
    public String newUser(ModelMap model) {
        User user = new User();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "new";
    }

    @PostMapping("/admin/new")
    public String create(ModelMap model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "new";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin";
    }


    @GetMapping("/user/{id}")
    public String show(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", userService.getById(id));
        return "user";
    }


    @GetMapping("/admin/edit/{id}")
    public String edit(ModelMap model, @PathVariable("id") Long id) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        return "edit";
    }

    @PatchMapping("/admin/edit/{id}")
    public String update(ModelMap model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "edit";
        }
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}


