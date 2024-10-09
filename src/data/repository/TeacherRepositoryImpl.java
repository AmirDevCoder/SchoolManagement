package data.repository;

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
        try (PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO teachers (first_name, last_name, email, dob, national_id)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (national_id) DO UPDATE SET
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                email = EXCLUDED.email,
                dob = EXCLUDED.dob,
                national_id = EXCLUDED.national_id
                RETURNING *
                """)) {
            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getEmail());
            stmt.setDate(4, teacher.getDob());
            stmt.setString(5, teacher.getNationalId());

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return ResultWrapper.ok(Teacher.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .email(resultSet.getString("email"))
                        .dob(resultSet.getDate("dob"))
                        .nationalId(resultSet.getString("national_id"))
                        .createdAt(resultSet.getDate("created_at"))
                        .updatedAt(resultSet.getDate("updated_at"))
                        .build());
            }
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), "Unknown error");
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> delete(Teacher teacher) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM teachers WHERE national_id = ?")) {
            stmt.setString(1, teacher.getNationalId());
            stmt.executeUpdate();
            return ResultWrapper.ok(true);
        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<Teacher>> getAll() {
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM teachers")) {
            List<Teacher> teachers = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                Date dateOfBirth = resultSet.getDate("dob");
                String nationalId = resultSet.getString("national_id");
                Date createdAt = resultSet.getDate("created_at");
                Date updatedAt = resultSet.getDate("updated_at");
                teachers.add(Teacher.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .dob(dateOfBirth)
                        .nationalId(nationalId)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .build()
                );
            }

            return ResultWrapper.ok(teachers);

        } catch (SQLException e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), e.getMessage());
        }
    }
}
