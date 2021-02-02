package pl.com.rozyccy.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.com.rozyccy.assignment.domain.ResponseUser;
import pl.com.rozyccy.assignment.domain.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class UsersService {

    @Autowired
    Environment env;

    public ResponseUser getUser(String login) {
        var user = getGitHubEndpoint(login);
        var rUser = ResponseUser.of(user, doCalculationOverUser(user));
        System.out.println(rUser.getCalculations());
        return rUser;
    }

    private User getGitHubEndpoint(String login) {
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.getForObject(Objects.requireNonNull(env.getProperty("source.rest.url", String.class)), User.class, login);
        System.out.println("Response is " + response);
        return response;
    }

    private BigDecimal doCalculationOverUser(User user) {
        BigDecimal calc;
        if (user.getFollowers() == 0) {
            calc = BigDecimal.ZERO;
        }
        else {
            var scale = Objects.requireNonNull(env.getProperty("user.calculations.scale", Integer.class));
            calc = new BigDecimal(6)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .divide(new BigDecimal(user.getFollowers()), RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(2 + user.getPublicRepos()));
        }
        return calc;
    }
}
