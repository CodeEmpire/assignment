package pl.com.rozyccy.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {
    String login;
    int requestCount;
}
