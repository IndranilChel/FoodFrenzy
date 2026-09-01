package com.foodfrenzy.foodfrenzyapp.controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.loginCredentials.AdminLogin;
import com.foodfrenzy.foodfrenzyapp.services.ProductServices;
import com.foodfrenzy.foodfrenzyapp.services.UserServices;

@Controller
public class HomeController {

    @Autowired
    private ProductServices productServices;

    @Autowired
    private UserServices userService;

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/location")
    public String location() {

        return "Locate_us";
    }

    @GetMapping("/about")
    public String about() {

        return "About";
    }

    @GetMapping("/login")
    public String login(Model model) {

        model.addAttribute("adminLogin", new AdminLogin());

        return "Login";
    }
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "Forgot_Password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {

            model.addAttribute(
                    "message",
                    "Passwords do not match."
            );

            return "Forgot_Password";
        }

        boolean updated =
                userService.updatePasswordByEmail(email, newPassword);

        if (updated) {

            return "redirect:/login";
        }

        model.addAttribute(
                "message",
                "No account found with this email."
        );

        return "Forgot_Password";
    }

    @GetMapping("/register")
    public String registerPage(Model model){

        model.addAttribute("userRegistration", new User());

        return "register";
    }

    @PostMapping("/saveUser")
    public String saveUser(User user){

        userService.addUser(user);

        return "redirect:/login";
    }
    @PostMapping("/customerLogin")
    public String userLogin(@RequestParam("uemail") String email,
                            @RequestParam("upassword") String password,
                            Model model,
                            HttpSession session){

        boolean status =
                userService.validateLoginCredentials(email, password);

        if(status){

            User user = userService.getUserByEmail(email);

            session.setAttribute("loggedUser", user);

            return "redirect:/products";
        }

        else{

            model.addAttribute("loginError",
                    "Invalid Email or Password");

            return "Login";
        }
    }
}