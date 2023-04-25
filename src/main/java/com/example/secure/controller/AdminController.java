package com.example.secure.controller;


import com.example.secure.entity.User;

import com.example.secure.repository.UserRepository;
import com.example.secure.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService serv;
    private final UserRepository userRepository;
    public AdminController(UserService serv, UserRepository userRepository) {
        this.serv = serv;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String adminPage() {
        return "index";
    }

    @GetMapping(value = "")
    public String userList(Model model,Principal principal) {
        User newUser = new User();
        model.addAttribute("users", serv.getAllUsers());
        model.addAttribute("adminUser", userRepository.findByUsername(principal.getName()));
        model.addAttribute("newUser", newUser);
        return "/admin";
    }
    @PostMapping("/add")
    public String createUser(@RequestParam("role") String[] role, User user) {
        serv.saveUser(user,role);
        return "redirect:/admin";
    }
    @PatchMapping(value = "edit/{id}")
    public String updateUser(@ModelAttribute("user") User updatedUser, @PathVariable("id") Long id,@RequestParam("role") String[] role) {
        serv.update(updatedUser, id, role);
        return "redirect:/admin";
    }
    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        serv.removeById(id);
        return "redirect:/admin";
    }
}