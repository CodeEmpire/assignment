package pl.com.rozyccy.assignment.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;
import pl.com.rozyccy.assignment.domain.User;
import pl.com.rozyccy.assignment.services.UsersService;

@RestController
public class UserController {

    @Autowired
    UsersService usersService;

    @GetMapping("/users/{login}")
    ResponseEntity<User> getUser(@PathVariable String login) throws JsonProcessingException {
        System.out.println("Endpoint requested. Parameter login is: " + login);
        return new ResponseEntity<>(usersService.getUser(login), HttpStatus.OK);
    }

}
