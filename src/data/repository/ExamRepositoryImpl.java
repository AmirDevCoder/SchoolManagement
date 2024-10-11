package data.repository;

import data.mapper.EntityMapperFactory;
import data.util.QueryHelper;
import domain.model.entity.Exam;
import domain.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExamRepositoryImpl implements ExamRepository {
    private final Connection connection;

    @Override
    public ResultWrapper<Exam> save(Exam exam) {
        try (PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO exams (name, course_id, teacher_id) VALUES (?, ?, ?)
                ON CONFLICT (name) DO UPDATE SET
                name = EXCLUDED.name,
                course_id = EXCLUDED.course_id,
                teacher_id = EXCLUDED.teacher_id
                RETURNING *
                """)) {
            QueryHelper.setQueryColumn(
                    stmt,
                    exam.getName(),
                    exam.getCourseId(),
                    exam.getTeacherId()
            );
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return ResultWrapper.ok(EntityMapperFactory.fromResultSet(resultSet).mapTo(Exam.class));
            }

            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), "Unknown error");
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> delete(Exam exam) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM exams WHERE name = ?")) {
            stmt.setString(1, exam.getName());
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Exam>> getAll() {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM exams")) {
            ResultSet resultSet = stmt.executeQuery();
            List<Exam> exams = new ArrayList<>();
            while (resultSet.next()) {
                exams.add(EntityMapperFactory.fromResultSet(resultSet).mapTo(Exam.class));
            }

            return ResultWrapper.ok(exams);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), e.getMessage());
        }
    }
}
