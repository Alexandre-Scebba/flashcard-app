package com.example.flashcardtool.controller;

import com.example.flashcardtool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;

    // Parola sıfırlama isteği sayfasını gösterir
    @GetMapping("/forgetpassword")
    public String showForgotPasswordPage() {
        return "/forgetpassword";  // forgot-password.html sayfasını render eder
    }

    // Parola sıfırlama isteği işlemi
    @PostMapping("/forgetpassword")
    public String processForgotPassword(@RequestParam("email") String email) {
        userService.sendPasswordResetLink(email);  // Parola sıfırlama bağlantısını e-posta olarak gönderir
        return "redirect:/login?resetLinkSent";
    }

    // Parola sıfırlama formunu gösterir
    @GetMapping("/newPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "newPassword";  // reset-password.html sayfasını render eder
    }

    // Parola sıfırlama işlemini tamamlar
    @PostMapping("/newPassword")
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        userService.updatePassword(token, password);  // Yeni parolayı günceller
        return "redirect:/login?resetSuccess";  // Başarılı mesaj için login sayfasına yönlendir
    }
}
