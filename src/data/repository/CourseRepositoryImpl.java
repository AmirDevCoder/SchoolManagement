package data.repository;

import data.mapper.EntityMapperFactory;
import data.util.QueryConst;
import data.util.QueryHelper;
import domain.model.entity.Course;
import domain.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {
    private final Connection connection;

    @Override
    public ResultWrapper<Course> save(Course course) {
        try (PreparedStatement stmt = connection.prepareStatement(QueryConst.UPSERT_COURSES)) {
            QueryHelper.setQueryColumn(
                    stmt,
                    course.getName(),
                    course.getTeacherId(),
                    course.getDescription()
            );
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return ResultWrapper.ok(EntityMapperFactory.fromResultSet(resultSet).mapTo(Course.class));
            }

            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), "Unknown error");
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> delete(Course course) {
        try (PreparedStatement stmt = connection.prepareStatement(QueryConst.DELETE_COURSES_BY_NAME)) {
            stmt.setString(1, course.getName());
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Course>> getAll() {
        try (PreparedStatement stmt = connection.prepareStatement(QueryConst.FETCH_ALL_COURSES)) {
            ResultSet resultSet = stmt.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(EntityMapperFactory.fromResultSet(resultSet).mapTo(Course.class));
            }

            return ResultWrapper.ok(courses);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Course>> findCoursesByIds(List<Integer> ids) {
        String placeholders = String.join(",", ids.stream().map(id -> "?").toArray(String[]::new));
        String sql = "SELECT * FROM courses WHERE id IN (" + placeholders + ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                preparedStatement.setInt(i + 1, ids.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(EntityMapperFactory.fromResultSet(resultSet).mapTo(Course.class));
            }

            return ResultWrapper.ok(courses);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".findCoursesByIds"), e.getMessage());
        }
    }
}
