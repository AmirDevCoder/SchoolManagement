package domain.model.entity;

import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Logs {
    private String data;
    private String action;
    private Instant time;
}
