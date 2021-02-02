package pl.com.rozyccy.assignment.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

@Data
@Builder
public class ResponseUser {
    long id;
    String login;
    String name;
    String type;
    String avatarUrl;
    ZonedDateTime createdAt;
    BigDecimal calculations;

    public static ResponseUser of(User user) {
        var calc = new BigDecimal(6).setScale(6)
                .divide(new BigDecimal(user.getFollowers()), RoundingMode.HALF_UP)
                .multiply(new BigDecimal(2 + user.getPublicRepos()));
        return ResponseUser.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .type(user.getType())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .calculations(calc)
                .build();
    }
}
