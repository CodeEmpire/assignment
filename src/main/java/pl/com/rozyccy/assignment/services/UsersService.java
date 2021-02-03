package pl.com.rozyccy.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.com.rozyccy.assignment.domain.Request;
import pl.com.rozyccy.assignment.dto.ResponseUser;
import pl.com.rozyccy.assignment.domain.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class UsersService {

    @Autowired
    Environment env;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ResponseUser getUser(String login) {
        var user = getGitHubEndpoint(login);
        saveRequestCountInDb(login);
        return ResponseUser.of(user, doCalculationOverUser(user));
    }

    private User getGitHubEndpoint(String login) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(Objects.requireNonNull(env.getProperty("source.rest.url", String.class)), User.class, login);
    }

    private BigDecimal doCalculationOverUser(User user) {
        BigDecimal calc;
        if (user.getFollowers() == 0) {
            calc = BigDecimal.ZERO;
        }
        else {
            var scale = env.getProperty("user.calculations.scale", Integer.class);
            if (scale == null) {
                scale = 4;
            }
            calc = new BigDecimal(6)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .divide(new BigDecimal(user.getFollowers()), RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(2 + user.getPublicRepos()));
        }
        return calc;
    }

    @Transactional
    public void saveRequestCountInDb(String login) {

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS requests(login VARCHAR(100), request_count NUMBER)");

        System.out.println("Before query");
        List<Request> req = jdbcTemplate.query("SELECT login, request_count FROM requests WHERE login = '" + login + "'",
                (resultSet, rowNum) -> new Request(resultSet.getString("login"), resultSet.getInt("request_count")));

        req.forEach(System.out::println);


        if (req.isEmpty()) {
            jdbcTemplate.execute("INSERT INTO requests (login, request_count) VALUES ('" + login + "', 1)");
        }
        else {
            jdbcTemplate.execute("UPDATE requests SET request_count = request_count + 1 WHERE login = '" + login + "'");

        }
        System.out.println("After query");
        List<Request> req2 = jdbcTemplate.query("SELECT login, request_count FROM requests WHERE login = '" + login + "'",
                (resultSet, rowNum) -> new Request(resultSet.getString("login"), resultSet.getInt("request_count")));

        req2.forEach(System.out::println);

    }
}
