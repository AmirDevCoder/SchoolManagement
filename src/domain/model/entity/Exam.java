package domain.model.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Exam {
    private Integer id;
    private String name;
    private Integer courseId;
    private Integer teacherId;
    private Date createdAt;
    private Date updatedAt;
}
