package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.CustomUserDetailService;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private CustomUserDetailService userService;

    @Autowired
    public void setUserService(CustomUserDetailService customUserDetailService) {
        this.userService = customUserDetailService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/user")
    public String userPage(ModelMap model, Principal principal) {
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
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }


//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") Long id, ModelMap model) {
//        model.addAttribute("user", userService.getById(id));
//        return "show_user";
//    }
//
//
//
//    @PostMapping
//    public String create(@ModelAttribute("user") User user) {
//        userService.save(user);
//        return "redirect:/";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String edit(ModelMap model, @PathVariable("id") Long id) {
//        model.addAttribute("user", userService.getById(id));
//        return "edit_user";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
//        userService.update(id, user);
//        return "redirect:/";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Long id) {
//        userService.delete(id);
//        return "redirect:/";
//    }
}
