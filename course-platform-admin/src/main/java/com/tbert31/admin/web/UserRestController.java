package com.tbert31.admin.web;

import com.tbert31.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserRestController {

    private UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public boolean checkIfEmailExists(@RequestParam(name = "email", defaultValue = "") String email){
        return userService.loadUserByEmail(email) != null ;
    }


}
