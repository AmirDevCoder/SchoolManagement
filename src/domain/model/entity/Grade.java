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
public class Grade {
    private Integer id;
    private Integer studentId;
    private Integer exmId;
    private double grade;
    private Date createdAt;
    private Date updatedAt;
}
