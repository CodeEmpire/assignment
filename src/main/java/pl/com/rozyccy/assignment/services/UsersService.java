package pl.com.rozyccy.assignment.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.com.rozyccy.assignment.domain.ResponseUser;
import pl.com.rozyccy.assignment.domain.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class UsersService {

    public ResponseUser getUser(String login) {
        var user = getGitHubEndpoint(login);
        var rUser = ResponseUser.of(user, doCalculationOverUser(user));
        System.out.println(rUser.getCalculations());
        return rUser;
    }

    private User getGitHubEndpoint(String login) {
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.getForObject("https://api.github.com/users/{login}", User.class, login);
        System.out.println("Response is " + response);
        return response;
    }

    private BigDecimal doCalculationOverUser(User user) {
        BigDecimal calc;
        if (user.getFollowers() == 0) {
            calc = BigDecimal.ZERO;
        }
        else {
            calc = new BigDecimal(6).setScale(6, RoundingMode.HALF_UP)
                    .divide(new BigDecimal(user.getFollowers()), RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(2 + user.getPublicRepos()));
        }
        return calc;
    }
}
