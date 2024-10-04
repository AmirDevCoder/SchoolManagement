package data.repository;

import domain.entity.Student;
import domain.repository.UserRepository;
import util.ResultWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ResultWrapper<Student> save(Student student) {
        try (PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO students (first_name, last_name, dob, national_id, gpu)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (national_id) DO UPDATE
                SET first_name = EXCLUDED.first_name,
                    last_name = EXCLUDED.last_name,
                    dob = EXCLUDED.dob,
                    national_id = EXCLUDED.national_id,
                    gpu = EXCLUDED.gpu
                RETURNING id
                """)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, student.getDob());
            stmt.setString(4, student.getNationalId());
            stmt.setFloat(5, student.getGpu());

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                student.setId(userId);
            }
        } catch (SQLException e) {
            return ResultWrapper.err(this.getClass().getSimpleName().concat("UserRepositoryImpl.save"), e.getMessage());
        }
        return ResultWrapper.ok(student);
    }

    @Override
    public void delete(Student student) {

    }

    @Override
    public ResultWrapper<List<Student>> getAll() {
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM students")) {
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                // todo: use mapping for map the db-entity into java-class
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date dateOfBirth = resultSet.getDate("dob");
                String nationalId = resultSet.getString("national_id");
                float gpu = resultSet.getFloat("gpu");
                students.add((Student) Student.Builder()
                        .setGpu(gpu)
                        .setId(id)
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setDob(dateOfBirth)
                        .setNationalId(nationalId)
                );
            }

            return ResultWrapper.ok(students);

        } catch (SQLException e) {
            return ResultWrapper.err(this.getClass().getSimpleName().concat("UserRepositoryImpl.getAll"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Long> getCountOfStudents() {
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT COUNT(*) FROM students")) {
            resultSet.next();
            return ResultWrapper.ok(resultSet.getLong(1));

        } catch (SQLException e) {
            return ResultWrapper.err(this.getClass().getSimpleName().concat("UserRepositoryImpl.getCountOfStudents"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> existsByNationalId(long nationalId) {
//        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT EXISTS(SELECT 1 FROM students WHERE id = )")) {
//
//        } catch (SQLException e) {

//        }
        return ResultWrapper.ok(true);
    }
}
