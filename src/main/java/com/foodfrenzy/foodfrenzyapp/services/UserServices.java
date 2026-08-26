package com.foodfrenzy.foodfrenzyapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.repositories.UserRepository;

@Component
public class UserServices {

    @Autowired
    private UserRepository userRepository;


    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<User> getAllUser() {

        return (List<User>) this.userRepository.findAll();
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    public User getUser(int id) {

        Optional<User> optional =
                this.userRepository.findById(id);

        return optional.orElse(null);
    }


    // =========================================================
// GET USER BY EMAIL
// =========================================================

    public User getUserByEmail(String email) {

        return this.userRepository.findUserByUemail(email);
    }


// =========================================================
// RESET PASSWORD BY EMAIL
// =========================================================

    public boolean updatePasswordByEmail(
            String email,
            String newPassword) {

        User user =
                this.userRepository.findUserByUemail(email);

        if (user == null) {
            return false;
        }

        user.setUpassword(newPassword);

        this.userRepository.save(user);

        return true;
    }


    // =========================================================
    // ADD USER
    // =========================================================

    public void addUser(User user) {

        this.userRepository.save(user);
    }


    // =========================================================
    // ADMIN UPDATE USER
    // =========================================================

    public void updateUser(User user, int id) {

        user.setU_id(id);

        this.userRepository.save(user);
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    public void deleteUser(int id) {

        this.userRepository.deleteById(id);
    }


    // =========================================================
    // LOGIN VALIDATION
    // =========================================================

    public boolean validateLoginCredentials(
            String email,
            String password) {

        List<User> users =
                (List<User>) this.userRepository.findAll();

        for (User u : users) {

            if (u != null
                    && u.getUpassword().equals(password)
                    && u.getUemail().equals(email)) {

                return true;
            }
        }

        return false;
    }


    // =========================================================
    // SAVE USER
    // =========================================================

    public void saveUser(User user) {

        this.userRepository.save(user);
    }


    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    public boolean changePassword(
            User user,
            String currentPassword,
            String newPassword) {

        if (user == null) {
            return false;
        }

        // Check old password
        if (!user.getUpassword().equals(currentPassword)) {
            return false;
        }

        // Set new password
        user.setUpassword(newPassword);

        // Save to database
        this.userRepository.save(user);

        return true;
    }
}