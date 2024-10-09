package domain.model.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Teacher {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dob;
    private String nationalId;
    private Date createdAt;
    private Date updatedAt;
}