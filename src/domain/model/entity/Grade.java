package domain.model.entity;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Grade {
    private Integer id;
    private Integer studentId;
    private Integer exmId;
    private Double grade;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
