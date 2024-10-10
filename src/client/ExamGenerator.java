package client;

import java.util.List;
import java.util.Random;

class ExamGenerator {
    private static final List<String> examNames = List.of(
            "Midterm Exam", "Final Exam", "Quiz 1", "Quiz 2",
            "Practical Exam", "Oral Exam", "Comprehensive Test",
            "Mock Exam", "Assessment Test", "Project Evaluation"
    );

    private static final Random random = new Random();

    public static String getExamName() {
        return examNames.get(random.nextInt(examNames.size()));
    }
}
