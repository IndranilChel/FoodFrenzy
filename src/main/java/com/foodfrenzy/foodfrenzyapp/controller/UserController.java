package com.foodfrenzy.foodfrenzyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserServices services;


    // =========================================================
    // ADD USER PAGE
    // =========================================================

    @GetMapping("/addUser")
    public String addUserPage() {

        return "Add_User";
    }


    // =========================================================
    // SAVE NEW USER
    // =========================================================

    @PostMapping("/addingUser")
    public String addUser(
            @ModelAttribute User user) {

        services.addUser(user);

        return "redirect:/admin/services";
    }


    // =========================================================
    // PROFILE PAGE
    // =========================================================

    @GetMapping("/profile")
    public String profilePage(
            Model model,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        if (loggedInUser == null) {

            return "redirect:/login";
        }


        model.addAttribute(
                "user",
                loggedInUser
        );


        return "Profile";
    }


    // =========================================================
    // EDIT PROFILE PAGE
    // =========================================================

    @GetMapping("/editProfile")
    public String editProfilePage(
            Model model,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        if (loggedInUser == null) {

            return "redirect:/login";
        }


        model.addAttribute(
                "user",
                loggedInUser
        );


        return "Edit_Profile";
    }


    // =========================================================
    // SAVE EDITED PROFILE
    // =========================================================

    @PostMapping("/editProfile")
    public String editProfile(
            @ModelAttribute User user,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        if (loggedInUser == null) {

            return "redirect:/login";
        }


        services.updateUser(
                user,
                loggedInUser.getU_id()
        );


        // Get updated user from database
        User updatedUser =
                services.getUser(
                        loggedInUser.getU_id()
                );


        // Update session
        session.setAttribute(
                "loggedInUser",
                updatedUser
        );


        return "redirect:/profile";
    }


    // =========================================================
    // CHANGE PASSWORD PAGE
    // =========================================================

    @GetMapping("/changePassword")
    public String changePasswordPage(
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        if (loggedInUser == null) {

            return "redirect:/login";
        }


        return "Change_Password";
    }


    // =========================================================
    // UPDATE PASSWORD
    // =========================================================

    @PostMapping("/changePassword")
    public String changePassword(
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        if (loggedInUser == null) {

            return "redirect:/login";
        }


        /*
         * Password-change logic can be added here
         * after confirming your User entity's
         * password field and UserServices methods.
         */

        return "redirect:/profile";
    }


    // =========================================================
    // UPDATE USER PAGE - ADMIN
    // =========================================================

    @GetMapping("/updateUser/{id}")
    public String updateUserPage(
            @PathVariable int id,
            Model model) {

        User user =
                services.getUser(id);


        if (user == null) {

            return "redirect:/admin/services";
        }


        model.addAttribute(
                "user",
                user
        );


        return "Update_User";
    }


    // =========================================================
    // SAVE UPDATED USER - ADMIN
    // =========================================================

    @PostMapping("/updateUser/{id}")
    public String updateUser(
            @PathVariable int id,
            @ModelAttribute User user) {

        services.updateUser(
                user,
                id
        );


        return "redirect:/admin/services";
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(
            @PathVariable int id) {

        services.deleteUser(id);

        return "redirect:/admin/services";
    }

}