package domain.service;

import domain.model.dto.StudentDto;
import domain.model.entity.Student;
import locator.LocatableService;
import util.ResultWrapper;

import java.util.List;

public interface StudentService extends LocatableService {
    ResultWrapper<List<StudentDto.Response>> getAll();
    ResultWrapper<StudentDto.Response> save(StudentDto.Request req);
    ResultWrapper<Boolean> delete(String nationalId);
    ResultWrapper<Integer> getCount();
    ResultWrapper<List<StudentDto.Response>> findByTeacherId(int teacherId);
}
