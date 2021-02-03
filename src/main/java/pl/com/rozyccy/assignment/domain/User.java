package pl.com.rozyccy.assignment.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class User {
    private long id;
    private String login;
    private String name;
    private String type;
    @JsonAlias("avatar_url")
    private String avatarUrl;
    @JsonAlias("created_at")
    private ZonedDateTime createdAt;
    private int followers;
    @JsonAlias("public_repos")
    private int publicRepos;
}
