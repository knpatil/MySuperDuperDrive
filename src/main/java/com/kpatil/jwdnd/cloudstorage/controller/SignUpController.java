package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.User;
import com.kpatil.jwdnd.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signUpView() {
        logger.info("received signup request ...");
        return "signup";
    }

    @PostMapping
    public String signUpUser(@ModelAttribute User user, Model model) {
        logger.info("Signing up user " + user.getUsername());
        String signupError = null;
        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = String.format("The username '%s' already exists.", user.getUsername());
            model.addAttribute("signupError", signupError);
            logger.info("User already exists.");
        } else {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
                model.addAttribute("singupError", signupError);
                logger.warn("Error in signing up user " + user.getUsername());
            } else {
                model.addAttribute("signupSuccess", true);
                logger.info("sign up successful.");
            }
        }
        return "signup";
    }
}
