package pl.com.rozyccy.assignment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Test
    public void testGetUser() throws JsonProcessingException {
        // Given
        // Test for octacat user
        String octocatLogin = "octocat";
        long octocatId = 583231;
        String octocatName = "The Octocat";
        String octocatType = "User";
        String octocatAvatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4";
        ZonedDateTime octocatCreatedAt = ZonedDateTime.of(2011,01,25,18,44,36,0, ZoneId.of("UTC"));

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
    public void testGetMyUser() throws JsonProcessingException {
        // Given
        // Test for codeempire user
        String codeempireLogin = "CodeEmpire";
        long codeempireId = 4615051;
        String codeempireName = "Marcin Rozycki";
        String codeempireType = "User";
        String codeempireAvatarUrl = "https://avatars.githubusercontent.com/u/4615051?v=4";
        ZonedDateTime codeempireCreatedAt = ZonedDateTime.of(2013,06,04,20,51,15,0, ZoneId.of("UTC"));

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
    public void testUserWithZeroFollowers() throws JsonProcessingException {
        // Given
        String login = "m-rozycki";

        // When
        var user = usersService.getUser(login);

        // Then
        assertNotNull(user, "Response should be not null");
        // Calculation for user with 0 followers should be 0
        assertTrue(user.getCalculations().compareTo(BigDecimal.ZERO) == 0);
    }
}