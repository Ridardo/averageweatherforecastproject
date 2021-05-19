package com.example.WeatherForecastProject.controller;

import com.example.WeatherForecastProject.domain.Role;
import com.example.WeatherForecastProject.domain.User;
import com.example.WeatherForecastProject.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    @Transactional
    public String addUser(User user, Map<String, Object> model) {
        if (user.getUsername().length() < 3 || user.getUsername().length() > 15)
        {
            userRepo.deleteByUsername(user.getUsername());
            model.put("message", "Некорректная длина никнейма! От 4 до 15 символов.");
            return "registration";
        }
        if (user.getPassword().length() < 3 || user.getPassword().length() > 15)
        {
            userRepo.deleteByUsername(user.getUsername());
            model.put("message", "Некорректная длина пароля! От 4 до 15 символов.");
            return "registration";
        }
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.put("message", "Пользователь с таким логином уже существует!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
        return "redirect:/login";
    }
}
