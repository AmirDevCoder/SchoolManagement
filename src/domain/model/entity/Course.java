package domain.model.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Course {
    private Integer id;
    private String name;
    private Integer teacherId;
    private String description;
    private List<Student> students;
    private Date createdAt;
    private Date updatedAt;
}
