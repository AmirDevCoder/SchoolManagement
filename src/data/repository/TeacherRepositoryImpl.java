package data.repository;

import data.mapper.EntityMapperFactory;
import data.util.QueryHelper;
import domain.model.entity.Teacher;
import domain.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TeacherRepositoryImpl implements TeacherRepository {
    private final Connection connection;

    @Override
    public ResultWrapper<Teacher> save(Teacher teacher) {
        try (PreparedStatement stmt = connection.prepareStatement(QueryHelper.UPSERT_TEACHERS)) {
            QueryHelper.setQueryColumn(
                    stmt,
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    teacher.getEmail(),
                    teacher.getDob(),
                    teacher.getNationalId()
            );
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return ResultWrapper.ok(EntityMapperFactory.fromResultSet(resultSet).mapTo(Teacher.class));
            }

            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), "Unknown error");
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> delete(Teacher teacher) {
        try (PreparedStatement stmt = connection.prepareStatement(QueryHelper.DELETE_TEACHER_BY_NATIONAL_ID)) {
            stmt.setString(1, teacher.getNationalId());
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Teacher>> getAll() {
        try (ResultSet resultSet = connection.createStatement().executeQuery(QueryHelper.FETCH_ALL_TEACHERS)) {
            List<Teacher> teachers = new ArrayList<>();
            while (resultSet.next()) {
                teachers.add(EntityMapperFactory.fromResultSet(resultSet).mapTo(Teacher.class));
            }

            return ResultWrapper.ok(teachers);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), e.getMessage());
        }
    }
}
