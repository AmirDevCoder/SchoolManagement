package domain.model.dto;

import domain.model.entity.Exam;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface ExamDto {
    record Request(String name, int courseId, int teacherId) {
        public Request {
            List<String> errors = new ArrayList<>();

            if (name == null || name.isBlank()) {
                errors.add("name can not be empty or null");
            }
            if (courseId < 0) {
                errors.add("course id can not be negative");
            }
            if (teacherId < 0) {
                errors.add("teacher id can not be negative");
            }

            if (!errors.isEmpty()) {
                throw new IllegalArgumentException("Validation errors: " + String.join(", ", errors));
            }
        }

        public Exam toExam() {
            return Exam.builder()
                    .name(name)
                    .courseId(courseId)
                    .teacherId(teacherId)
                    .build();
        }
    }

    record Response(int id, String name, int courseId, int teacherId, Timestamp createdAt) {

    }
}
