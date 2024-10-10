package domain.model.dto;

import domain.model.entity.Teacher;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface TeacherDto {
    record Request(String firstName, String lastName, String email, Date dob, String nationalId) {
        public Request {
            List<String> errors = new ArrayList<>();

            if (firstName == null || firstName.isBlank()) {
                errors.add("First name cannot be null or blank");
            }
            if (lastName == null || lastName.isBlank()) {
                errors.add("Last name cannot be null or blank");
            }
            //!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
            if (email == null || email.isBlank()) {
                errors.add("Invalid email address");
            }
            if (dob == null) {
                errors.add("Date of birth cannot be null");
            }
            if (nationalId == null || nationalId.isBlank() || nationalId.length() < 10) {
                errors.add("National ID must be at least 10 characters long");
            }

            if (!errors.isEmpty()) {
                throw new IllegalArgumentException("Validation error: " + String.join(", ", errors));
            }
        }

        public Teacher toTeacher() {
            return Teacher.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .dob(dob)
                    .nationalId(nationalId)
                    .build();
        }
    }

    record Response(int id, String fullName, String email, String nationalId, Date createdAt) {
    }
}
