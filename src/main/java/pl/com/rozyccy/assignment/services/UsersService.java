package pl.com.rozyccy.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.com.rozyccy.assignment.domain.User;
import pl.com.rozyccy.assignment.dto.ResponseUser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class UsersService {

    @Autowired
    private Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    public ResponseUser getUser(String login) {
        // Changing login to lowerCase, because for endpoint user "CodeEmpire" and "codeempire" is the same
        var lowercaseLogin = login.toLowerCase();
        saveRequestCountInDb(lowercaseLogin);
        var user = getGitHubEndpoint(lowercaseLogin);
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
        // Create table if not exists
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS requests(login VARCHAR(100), request_count NUMBER)");

        // Prepare parameter for sql
        var sqlLoginParameter = new MapSqlParameterSource("login", login);

        // Get request count for login
        List<Integer> req = parameterJdbcTemplate.queryForList("SELECT request_count FROM requests WHERE login = :login",
                sqlLoginParameter,
                Integer.class);

        // If row for login doesn't exists than insert row else update existed row
        if (req.isEmpty()) {
            parameterJdbcTemplate.batchUpdate("INSERT INTO requests (login, request_count) VALUES (:login, 1)",
                    new MapSqlParameterSource[]{sqlLoginParameter});
        }
        else {
            parameterJdbcTemplate.batchUpdate("UPDATE requests SET request_count = request_count + 1 WHERE login = :login",
                    new MapSqlParameterSource[]{sqlLoginParameter});

        }
    }
}
