package domain.model.dto;

import domain.model.entity.Course;
import domain.model.entity.Student;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface StudentDto {
    record Request(String firstName, String lastName, String email, Date dob, String nationalId,
                   List<Integer> courseIds) {
        public Request {
            List<String> errors = new ArrayList<>();

            if (firstName == null || firstName.isBlank()) {
                errors.add("First name cannot be null or blank");
            }
            if (lastName == null || lastName.isBlank()) {
                errors.add("Last name cannot be null or blank");
            }
            // !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
            if (email == null) {
                errors.add("Invalid email address");
            }
            if (dob == null) {
                errors.add("Date of birth cannot be null");
            }
            if (nationalId == null || nationalId.length() < 10) {
                errors.add("National ID must be at least 10 characters long");
            }
            if (courseIds.isEmpty()) {
                errors.add("Courses cannot be empty");
            }

            if (!errors.isEmpty()) {
                throw new IllegalArgumentException("Validation error: " + String.join(", ", errors));
            }
        }

        public Student toStudent() {
            return Student.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .courses(courseIds.stream().map(id -> Course.builder().id(id).build()).toList())
                    .dob(dob)
                    .nationalId(nationalId)
                    .build();
        }
    }

    record Response(int id, String fullName, String email, String nationalId, List<Course> courses, Timestamp createdAt) {
    }
}
