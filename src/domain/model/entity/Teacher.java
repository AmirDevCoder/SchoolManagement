package domain.model.entity;

import data.mapper.IgnoreMapping;
import lombok.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Teacher {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dob;
    private String nationalId;
    @IgnoreMapping
    private List<Student> students;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}