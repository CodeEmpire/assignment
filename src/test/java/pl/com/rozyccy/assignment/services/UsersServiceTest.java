package pl.com.rozyccy.assignment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Test
    public void testGetUser() throws JsonProcessingException {
        var response = usersService.getUser("octocat");

        assertNotNull(response, "Response should be not null");
    }
}