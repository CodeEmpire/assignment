package pl.com.rozyccy.assignment.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class User {
    long id;
    String login;
    String name;
    String type;
    @JsonAlias("avatar_url")
    String avatarUrl;
    @JsonAlias("created_at")
    ZonedDateTime createdAt;
    int followers;
    @JsonAlias("public_repos")
    int publicRepos;
}
