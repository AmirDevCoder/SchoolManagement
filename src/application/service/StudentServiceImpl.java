package application.service;

import domain.entity.Student;
import domain.repository.UserRepository;
import domain.service.StudentService;
import util.ResultWrapper;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final UserRepository studentRepo;

    public StudentServiceImpl(UserRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Override
    public ResultWrapper<List<Student>> getAllStudents() {
        return studentRepo.getAll();
    }

    @Override
    public ResultWrapper<Long> getCountOfStudents() {
        return studentRepo.getCountOfStudents();
    }

    @Override
    public ResultWrapper<Student> save(Student student) {
        return studentRepo.save(student);
    }

}
