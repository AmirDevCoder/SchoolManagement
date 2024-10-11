package domain.model.entity;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Exam {
    private Integer id;
    private String name;
    private Integer courseId;
    private Integer teacherId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
