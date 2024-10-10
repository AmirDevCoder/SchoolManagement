package domain.model.dto;

import domain.model.entity.Course;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface CourseDto {
    record Request(String name, int teacherId, String description) {
        public Request {
            List<String> errors = new ArrayList<>();

            if (name == null || name.isBlank()) {
                errors.add("Name cannot be blank or empty");
            }
            if (teacherId < 0) {
                errors.add("Teacher Id cannot be negative");
            }

            if (!errors.isEmpty()) {
                throw new IllegalArgumentException("Validation errors: " + String.join(", ", errors));
            }
        }

        public Course toCourse() {
            return Course.builder()
                    .name(name)
                    .teacherId(teacherId)
                    .description(description)
                    .build();
        }
    }

    record Response(int id, Date createdAt) {
    }
}
