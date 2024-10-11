package domain.repository;

import domain.model.entity.Student;
import domain.util.BaseRepository;
import util.ResultWrapper;

import java.util.List;

public interface StudentRepository extends BaseRepository<Student> {

    ResultWrapper<Integer> getCountOfStudents();
    ResultWrapper<List<Student>> findByTeacherId(int teacherId);
    ResultWrapper<Boolean> removeStudentCourseByStudentId(int studentId, List<Integer> courseIds);
    ResultWrapper<Boolean> addStudentCourseByStudentId(int studentId, List<Integer> courseIds);
    ResultWrapper<Student> upsert(Student student);
    ResultWrapper<List<Integer>> findStudentCourseIdsByStudentId(int studentId);
    ResultWrapper<Boolean> addStudentTeacherByStudentId(int studentId, List<Integer> teacherIds);
    ResultWrapper<Boolean> removeStudentTeacherByStudentId(int studentId);

}
