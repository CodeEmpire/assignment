package pl.com.rozyccy.assignment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;
import pl.com.rozyccy.assignment.domain.User;

import java.util.Map;

@Service
public class UsersService {

    public User getUser(String login) throws JsonProcessingException {
        var resp = getGitHubEndpoint(login);
        return resp;
    }

    private User getGitHubEndpoint(String login) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.getForObject("https://api.github.com/users/{login}", User.class, login);
        System.out.println("Response is " + response);
        return response;
    }

}
