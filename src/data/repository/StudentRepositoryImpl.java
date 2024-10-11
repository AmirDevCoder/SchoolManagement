package data.repository;

import data.mapper.EntityMapperFactory;
import data.util.QueryHelper;
import domain.model.entity.Course;
import domain.model.entity.Student;
import domain.repository.CourseRepository;
import domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {
    private final Connection connection;
    private final CourseRepository courseRepo;

    @Override
    public ResultWrapper<Student> save(Student student) {
        Throwable throwable = null;
        try {
            connection.setAutoCommit(false);

            var desireCoursesRes = courseRepo.findCoursesByIds(student.getCourses().stream().map(Course::getId).toList());
            if (!desireCoursesRes.isSuccess()) {
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), desireCoursesRes.getError());
            }

            var upsertRes = upsert(student);
            if (!upsertRes.isSuccess()) {
                connection.rollback();
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), upsertRes.getError());
            }

            var currentStudentCoursesIds = findStudentCourseIdsByStudentId(student.getId());
            if (!currentStudentCoursesIds.isSuccess()) {
                connection.rollback();
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), currentStudentCoursesIds.getError());
            }

            if (!currentStudentCoursesIds.getValue().isEmpty()) {
                // todo: instead of removing all old data, check one-by-one and update new rows
                var removeCurrentStudentCourse = removeStudentCourseByStudentId(student.getId(), currentStudentCoursesIds.getValue());
                if (!removeCurrentStudentCourse.isSuccess()) {
                    connection.rollback();
                    return ResultWrapper.err(getClass().getSimpleName().concat(".save"), removeCurrentStudentCourse.getError());
                }
            }

            var addNewStudentCourse = addStudentCourseByStudentId(student.getId(), desireCoursesRes.getValue().stream().map(Course::getId).toList());
            if (!addNewStudentCourse.isSuccess()) {
                connection.rollback();
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), addNewStudentCourse.getError());
            }

            if (!currentStudentCoursesIds.getValue().isEmpty()) {
                var removeStudentTeacher = removeStudentTeacherByStudentId(student.getId());
                if (!removeStudentTeacher.isSuccess()) {
                    connection.rollback();
                    return ResultWrapper.err(getClass().getSimpleName().concat(".save"), removeStudentTeacher.getError());
                }
            }

            var addStudentTeacher = addStudentTeacherByStudentId(student.getId(), desireCoursesRes.getValue().stream().map(Course::getTeacherId).toList());
            if (!addStudentTeacher.isSuccess()) {
                connection.rollback();
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), addStudentTeacher.getError());
            }

            student.setCourses(desireCoursesRes.getValue());
            connection.commit();

            return ResultWrapper.ok(student);

        } catch (SQLException e) {
            throwable = new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                if (throwable != null) {
                    throwable.addSuppressed(e);
                } else {
                    throwable = new RuntimeException("Failed to reset auto-commit", e);
                }
            }
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".save"), throwable.getMessage());
    }

    @Override
    public ResultWrapper<Boolean> delete(Student student) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM students WHERE national_id = ?")) {
            stmt.setString(1, student.getNationalId());
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Student>> getAll() {
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM students")) {
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(EntityMapperFactory.fromResultSet(resultSet).mapTo(Student.class));
            }

            return ResultWrapper.ok(students);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Integer> getCountOfStudents() {
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT COUNT(*) FROM students")) {
            if (resultSet.next()) {
                return ResultWrapper.ok(resultSet.getInt(1));
            }

            return ResultWrapper.err(getClass().getSimpleName().concat(".getCountOfStudents"), "Unknown error");
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".getCountOfStudents"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Student>> findByTeacherId(int teacherId) {
        try (PreparedStatement stmt = connection.prepareStatement("""
                SELECT * FROM students s
                JOIN student_teacher st ON s.id = st.student_id
                JOIN teachers t ON t.id = st.teacher_id
                WHERE t.id = ?
                """)) {
            stmt.setInt(1, teacherId);
            ResultSet resultSet = stmt.executeQuery();
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(EntityMapperFactory.fromResultSet(resultSet).mapTo(Student.class));
            }

            return ResultWrapper.ok(students);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".findByTeacherId"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> removeStudentCourseByStudentId(int studentId, List<Integer> courseIds) {
        String placeHolder = String.join(",", courseIds.stream().map((id) -> "?").toArray(String[]::new));
        String query = "DELETE FROM enrollments WHERE student_id = ? AND course_id IN (" + placeHolder + ")";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            for (int i = 0; i < courseIds.size(); i++) {
                stmt.setInt(i + 2, courseIds.get(i));
            }
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".removeStudentCourses"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> addStudentCourseByStudentId(int studentId, List<Integer> courseIds) {
        String query = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Integer courseId : courseIds) {
                QueryHelper.setQueryColumn(stmt, studentId, courseId);
                stmt.addBatch();
            }
            int[] result = stmt.executeBatch();
            for (int i : result) {
                if (i == PreparedStatement.EXECUTE_FAILED) {
                    return ResultWrapper.err(getClass().getSimpleName().concat(".addCourses"), "One of the insertions failed");
                }
            }

            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".addCourses"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Student> upsert(Student student) {
        try (PreparedStatement upsertStudentStmt = connection.prepareStatement("""
                INSERT INTO students (first_name, last_name, email, dob, national_id)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (national_id) DO UPDATE SET
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                email = EXCLUDED.email,
                dob = EXCLUDED.dob,
                national_id = EXCLUDED.national_id
                RETURNING *
                """)) {
            QueryHelper.setQueryColumn(
                    upsertStudentStmt,
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getDob(),
                    student.getNationalId()
            );
            ResultSet resultSet = upsertStudentStmt.executeQuery();
            if (resultSet.next()) {
                return ResultWrapper.ok(EntityMapperFactory.fromResultSet(resultSet).mapTo(Student.class));
            } else {
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), "Failed to insert or update student");
            }

        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), "Failed to insert or update student" + e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Integer>> findStudentCourseIdsByStudentId(int studentId) {
        try (PreparedStatement loadStudentActiveCoursesStmt = connection.prepareStatement("SELECT course_id FROM enrollments WHERE student_id = ?")) {
            loadStudentActiveCoursesStmt.setInt(1, studentId);
            ResultSet resultSet = loadStudentActiveCoursesStmt.executeQuery();
            List<Integer> courseIds = new ArrayList<>();
            while (resultSet.next()) {
                courseIds.add(resultSet.getInt("course_id"));
            }

            return ResultWrapper.ok(courseIds);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), "Failed to Load Student Active Courses " + e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> addStudentTeacherByStudentId(int studentId, List<Integer> teacherIds) {
        String query = "INSERT INTO student_teacher (student_id, teacher_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Integer teacherId : teacherIds) {
                QueryHelper.setQueryColumn(stmt, studentId, teacherId);
                stmt.addBatch();
            }

            int[] result = stmt.executeBatch();
            for (int i : result) {
                if (i == PreparedStatement.EXECUTE_FAILED) {
                    return ResultWrapper.err(getClass().getSimpleName().concat(".addStudentTeacherByStudentId"), "One of the insertions failed");
                }
            }

            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".addStudentTeacherByStudentId"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> removeStudentTeacherByStudentId(int studentId) {
        String query = "DELETE FROM student_teacher WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".removeStudentTeacherByStudentId"), e.getMessage());
        }
    }

}
