package com.app.MediQuirk.controller.Admin;

import com.app.MediQuirk.model.UserProfile;
import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.services.UserProfileService;
import com.app.MediQuirk.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    @Autowired
    private final UserProfileService userProfileService;

    @Autowired
    private final UsersService usersService;

    // GET request to show add user profile form
    @GetMapping("/userprofiles/add")
    public String showAddForm(Model model) {
        model.addAttribute("userProfile", new UserProfile());
        return "Admin/userprofiles/add-userprofile";
    }

    // POST request to add a new user profile
    @PostMapping("/userprofiles/add")
    public String addUserProfile(@Valid UserProfile userProfile, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/userprofiles/add-userprofile";
        }
        userProfileService.addUserProfile(userProfile);
        return "redirect:/userprofiles";
    }

    // GET request to show list of user profiles
    @GetMapping("/userprofiles")
    public String listUserProfiles(Model model) {
        List<UserProfile> userProfiles = userProfileService.getAllUserProfiles();
        model.addAttribute("userProfiles", userProfiles);
        return "Admin/userprofiles/userprofile-list";
    }

    // GET request to show user profile edit form
    @GetMapping("/userprofiles/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        UserProfile userProfile = userProfileService.getUserProfileById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user profile Id: " + id));
        model.addAttribute("userProfile", userProfile);
        return "Admin/userprofiles/update-userprofile";
    }

    // POST request to update user profile
    @PostMapping("/userprofiles/update/{id}")
    public String updateUserProfile(@PathVariable("id") Long id, @Valid UserProfile userProfile, BindingResult result, Model model) {
        if (result.hasErrors()) {
            userProfile.setUserProfileId(id); // Ensure the ID is set before returning the form with errors
            return "Admin/userprofiles/update-userprofile";
        }
        userProfile.setUserProfileId(id); // Ensure the ID is set before updating
        userProfileService.updateUserProfile(userProfile);
        return "redirect:/userprofiles";
    }

    // GET request for deleting user profile
    @GetMapping("/userprofiles/delete/{id}")
    public String deleteUserProfile(@PathVariable("id") Long id, Model model) {
        UserProfile userProfile = userProfileService.getUserProfileById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user profile Id: " + id));
        userProfileService.deleteUserProfileById(id);
        return "redirect:/userprofiles";
    }

    // GET request to show user profile details
    @GetMapping("/userprofiles/details/{id}")
    public String showUserProfileDetails(@PathVariable("id") Long id, Model model) {
        UserProfile userProfile = userProfileService.getUserProfileById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user profile Id: " + id));
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("user", userProfile.getUser());
        return "Admin/userprofiles/userprofile-details";
    }

    // GET request to show add user form for a user profile
    @GetMapping("/userprofiles/{userProfileId}/add-user")
    public String showAddUserForm(@PathVariable("userProfileId") Long userProfileId, Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("userProfileId", userProfileId);
        return "Admin/userprofiles/add-user";
    }

    // POST request to add a user for a user profile
    @PostMapping("/userprofiles/{userProfileId}/add-user")
    public String addUser(@PathVariable("userProfileId") Long userProfileId, @Valid Users user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/userprofiles/add-user";
        }
        UserProfile userProfile = userProfileService.getUserProfileById(userProfileId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user profile Id: " + userProfileId));
        userProfile.setUser(user);
        usersService.addUser(user);
        userProfileService.updateUserProfile(userProfile);
        return "redirect:/userprofiles";
    }
}
