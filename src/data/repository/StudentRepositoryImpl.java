package data.repository;

import data.mapper.EntityMapperFactory;
import data.util.QueryConst;
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
            Student savedStudent = upsertRes.getValue();

            var currentEnrollmentIds = findEnrollmentIdsByStudentId(savedStudent.getId());
            if (!currentEnrollmentIds.isSuccess()) {
                connection.rollback();
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), currentEnrollmentIds.getError());
            }

            if (!currentEnrollmentIds.getValue().isEmpty()) {
                var removeCurrentEnrollment = removeEnrollmentByStudentId(
                        savedStudent.getId(),
                        currentEnrollmentIds.getValue().stream()
                                .filter(id -> !desireCoursesRes.getValue().stream().map(Course::getId).toList().contains(id))
                                .toList()
                );
                if (!removeCurrentEnrollment.isSuccess()) {
                    connection.rollback();
                    return ResultWrapper.err(getClass().getSimpleName().concat(".save"), removeCurrentEnrollment.getError());
                }
            }

            var addNewEnrollment = addEnrollmentByStudentId(
                    savedStudent.getId(),
                    desireCoursesRes.getValue().stream()
                            .map(Course::getId)
                            .filter(id -> !currentEnrollmentIds.getValue().contains(id))
                            .toList()
            );
            if (!addNewEnrollment.isSuccess()) {
                connection.rollback();
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), addNewEnrollment.getError());
            }

            // TODO :instead of removing all rows from student_teacher table ,it's better to filter it like deletion in enrollment table(above query)
            if (!currentEnrollmentIds.getValue().isEmpty()) {
                var removeStudentTeacher = removeStudentTeacherByStudentId(savedStudent.getId());
                if (!removeStudentTeacher.isSuccess()) {
                    connection.rollback();
                    return ResultWrapper.err(getClass().getSimpleName().concat(".save"), removeStudentTeacher.getError());
                }
            }

            // TODO :instead of adding all rows to student_teacher table ,it's better to filter it like insertion in enrollment table(above query)
            var addStudentTeacher = addStudentTeacherByStudentId(savedStudent.getId(), desireCoursesRes.getValue().stream().map(Course::getTeacherId).toList());
            if (!addStudentTeacher.isSuccess()) {
                connection.rollback();
                return ResultWrapper.err(getClass().getSimpleName().concat(".save"), addStudentTeacher.getError());
            }

            savedStudent.setCourses(desireCoursesRes.getValue());
            connection.commit();

            return ResultWrapper.ok(savedStudent);
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
        try (PreparedStatement stmt = connection.prepareStatement(QueryConst.DELETE_STUDENT_BY_NATIONAL_ID)) {
            stmt.setString(1, student.getNationalId());
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Student>> getAll() {
        try (ResultSet resultSet = connection.createStatement().executeQuery(QueryConst.FETCH_ALL_STUDENTS)) {
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
        try (ResultSet resultSet = connection.createStatement().executeQuery(QueryConst.COUNT_OF_STUDENTS)) {
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
        try (PreparedStatement stmt = connection.prepareStatement(QueryConst.FETCH_BY_TEACHER_ID)) {
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
    public ResultWrapper<Boolean> removeEnrollmentByStudentId(int studentId, List<Integer> courseIds) {
        // TODO: check for placeHolder feature exist in JDBC or not
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
            return ResultWrapper.err(getClass().getSimpleName().concat(".removeEnrollment"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> addEnrollmentByStudentId(int studentId, List<Integer> courseIds) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)")) {
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
        try (PreparedStatement upsertStudentStmt = connection.prepareStatement(QueryConst.UPSERT_STUDENTS)) {
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
    public ResultWrapper<List<Integer>> findEnrollmentIdsByStudentId(int studentId) {
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
