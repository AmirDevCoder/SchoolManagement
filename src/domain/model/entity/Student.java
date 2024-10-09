package domain.model.entity;

import lombok.*;

import java.sql.Date;
import java.util.List;

@Builder
@Data
public class Student {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dob;
    private String nationalId;
    private List<Course> courses;
    private Date createdAt;
    private Date updatedAt;
}