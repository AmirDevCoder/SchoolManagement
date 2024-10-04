package domain.service;

import domain.entity.Student;
import locator.LocatableService;
import util.ResultWrapper;

import java.util.List;

public interface StudentService extends LocatableService {
    ResultWrapper<List<Student>> getAllStudents();
    ResultWrapper<Long> getCountOfStudents();
    ResultWrapper<Student> save(Student student);
}
