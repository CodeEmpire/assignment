package pl.com.rozyccy.assignment.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testGetUser() {
        // Given
        // Test for octacat user
        String octocatLogin = "octocat";
        long octocatId = 583231;
        String octocatName = "The Octocat";
        String octocatType = "User";
        String octocatAvatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4";
        ZonedDateTime octocatCreatedAt = ZonedDateTime.of(2011,1,25,18,44,36,0, ZoneId.of("UTC"));

        // When
        var user = usersService.getUser(octocatLogin);

        // Then
        assertNotNull(user, "Response should be not null");
        assertEquals(octocatId, user.getId());
        assertEquals(octocatLogin, user.getLogin());
        assertEquals(octocatName, user.getName());
        assertEquals(octocatType, user.getType());
        assertEquals(octocatAvatarUrl, user.getAvatarUrl());
        assertEquals(octocatCreatedAt, user.getCreatedAt());
        // Calculation shouldn't be negative number
        assertTrue(user.getCalculations().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    public void testGetMyUser() {
        // Given
        // Test for codeempire user
        String codeempireLogin = "CodeEmpire";
        long codeempireId = 4615051;
        String codeempireName = "Marcin Rozycki";
        String codeempireType = "User";
        String codeempireAvatarUrl = "https://avatars.githubusercontent.com/u/4615051?v=4";
        ZonedDateTime codeempireCreatedAt = ZonedDateTime.of(2013,6,4,20,51,15,0, ZoneId.of("UTC"));

        // When
        var user = usersService.getUser(codeempireLogin);

        // Then
        assertNotNull(user, "Response should be not null");
        assertEquals(codeempireId, user.getId());
        assertEquals(codeempireLogin, user.getLogin());
        assertEquals(codeempireName, user.getName());
        assertEquals(codeempireType, user.getType());
        assertEquals(codeempireAvatarUrl, user.getAvatarUrl());
        assertEquals(codeempireCreatedAt, user.getCreatedAt());
        // Calculation shouldn't be negative number
        assertTrue(user.getCalculations().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    public void testUserWithZeroFollowers() {
        // Given
        String login = "m-rozycki";

        // When
        var user = usersService.getUser(login);

        // Then
        assertNotNull(user, "Response should be not null");
        // Calculation for user with 0 followers should be 0
        assertEquals(user.getCalculations(), BigDecimal.ZERO);
    }

    @Test
    public void testDbRequestCounting() {
        requestAppToVerifyIsDbCountingRequests("m-rozycki", 1);
    }

    @Test
    public void testDbRequestCountingMultiple() {
        requestAppToVerifyIsDbCountingRequests("m-rozycki", 10);
    }

    private void requestAppToVerifyIsDbCountingRequests(String login, int requestXTimes) {
        var sqlParamLogin = new MapSqlParameterSource("login", login);
        List<Integer> requestCountBefore = namedParameterJdbcTemplate.queryForList(
                "SELECT request_count FROM requests WHERE login = :login",
                sqlParamLogin,
                Integer.class);

        // When
        for (int i = 0; i < requestXTimes; i++) {
            usersService.getUser(login);
        }

        // Then
        List<Integer> requestCountAfter = namedParameterJdbcTemplate.queryForList(
                "SELECT request_count FROM requests WHERE login = :login",
                sqlParamLogin,
                Integer.class);

        assertEquals(1, requestCountBefore.size(), "It should be only one row for login");
        assertEquals(1, requestCountAfter.size(), "It should be only one row for login");
        assertEquals(requestCountBefore.get(0) + requestXTimes, requestCountAfter.get(0));
    }

    @Test
    public void testSQLInjection() {
        String sqlInjection = "x-mrozycki' or '1' = '1";
        List<Integer> sumAllRequestCountBefore = namedParameterJdbcTemplate.query("SELECT sum(request_count) all_request FROM requests ",
                (resultSet, rowNum) -> resultSet.getInt("all_request"));

        try {
            usersService.getUser(sqlInjection);
        }catch (Exception e) {
            // swallow exception
        }

        List<Integer> sumAllRequestCountAfter = namedParameterJdbcTemplate.query("SELECT sum(request_count) all_request FROM requests ",
                (resultSet, rowNum) -> resultSet.getInt("all_request"));

        // login doesn't find, but try of use API write down in DB, so request count increased by one.
        // in DB in field login we will see try of sql injection
        assertEquals(sumAllRequestCountBefore.get(0) + 1, sumAllRequestCountAfter.get(0), "Probably SQL Injected!!!");
    }
}