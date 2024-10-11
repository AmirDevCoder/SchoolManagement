package domain.repository;

import domain.model.entity.Student;
import domain.util.BaseRepository;
import util.ResultWrapper;

import java.util.List;

public interface StudentRepository extends BaseRepository<Student> {

    ResultWrapper<Integer> getCountOfStudents();
    ResultWrapper<List<Student>> findByTeacherId(int teacherId);
    ResultWrapper<Boolean> removeEnrollmentByStudentId(int studentId, List<Integer> courseIds);
    ResultWrapper<Boolean> addEnrollmentByStudentId(int studentId, List<Integer> courseIds);
    ResultWrapper<Student> upsert(Student student);
    ResultWrapper<List<Integer>> findEnrollmentIdsByStudentId(int studentId);
    ResultWrapper<Boolean> addStudentTeacherByStudentId(int studentId, List<Integer> teacherIds);
    ResultWrapper<Boolean> removeStudentTeacherByStudentId(int studentId);

}
