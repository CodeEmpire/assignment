package pl.com.rozyccy.assignment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users/{login}")
    String getUser(@PathVariable String login) {
        System.out.println("Endpoint requested. Parameter login is: " + login);
        return "Hello " + login;
    }

}
