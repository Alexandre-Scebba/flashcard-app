package com.example.flashcardtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";  // `home.html` adlı bir Thymeleaf template dosyası olduğunu varsayıyoruz
    }
}
