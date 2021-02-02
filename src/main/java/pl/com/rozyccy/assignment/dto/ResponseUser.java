package pl.com.rozyccy.assignment.dto;

import lombok.Builder;
import lombok.Data;
import pl.com.rozyccy.assignment.domain.User;

import java.math.BigDecimal;
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

    public static ResponseUser of(User user, BigDecimal calculations) {
        return ResponseUser.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .type(user.getType())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .calculations(calculations)
                .build();
    }
}
