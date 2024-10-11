package domain.model.entity;

import data.mapper.IgnoreMapping;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {
    private Integer id;
    private String name;
    private Integer teacherId;
    private String description;
    @IgnoreMapping
    private List<Student> students;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
