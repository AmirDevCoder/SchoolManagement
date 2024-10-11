package domain.model.entity;

import data.mapper.IgnoreMapping;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

// TODO:
//  it's better to implement entities immutable
//  to avoiding race-condition or unexpected behavior(may be by using record class type)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dob;
    private String nationalId;
    @IgnoreMapping
    private List<Course> courses;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}