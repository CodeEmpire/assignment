package pl.com.rozyccy.assignment.dto;

import lombok.Builder;
import lombok.Data;
import pl.com.rozyccy.assignment.domain.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class ResponseUser {
    private long id;
    private String login;
    private String name;
    private String type;
    private String avatarUrl;
    private ZonedDateTime createdAt;
    private BigDecimal calculations;

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
