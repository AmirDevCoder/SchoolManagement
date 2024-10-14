package presenter.service;

import domain.model.dto.StudentDto;
import domain.model.entity.Logs;
import domain.model.entity.Student;
import domain.repository.StudentRepository;
import domain.service.LoggerService;
import domain.service.StudentService;
import lombok.RequiredArgsConstructor;
import domain.model.entity.LogsAction;
import util.ResultWrapper;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repo;
    private final LoggerService loggerSvc;

    @Override
    public ResultWrapper<List<StudentDto.Response>> getAll() {
        var res = repo.getAll();
        if (res.isSuccess()) {
            return res.map(students -> students.stream()
                    .map(student -> new StudentDto.Response(
                            student.getId(),
                            String.format("%s %s", student.getFirstName(), student.getLastName()),
                            student.getEmail(),
                            student.getNationalId(),
                            student.getCourses(),
                            student.getCreatedAt()
                    ))
                    .toList()
            );
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), res.getError());
    }

    @Override
    public ResultWrapper<Integer> getCount() {
        return repo.getCountOfStudents();
    }

    @Override
    public ResultWrapper<List<StudentDto.Response>> findByTeacherId(int teacherId) {
        var res = repo.findByTeacherId(teacherId);
        if (res.isSuccess()) {
            return res.map(students -> students.stream()
                    .map(student -> new StudentDto.Response(
                            student.getId(),
                            String.format("%s %s", student.getFirstName(), student.getLastName()),
                            student.getEmail(),
                            student.getNationalId(),
                            student.getCourses(),
                            student.getCreatedAt()
                    )).toList());
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".findByTeacherId"), res.getError());
    }

    @Override
    public ResultWrapper<StudentDto.Response> save(StudentDto.Request req) {
        // TODO: you can fetch Courses from CourseService here first
        var res = repo.save(req.toStudent());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .action(LogsAction.STUDENT_REGISTERED.name())
                    .data(res.getValue().getNationalId())
                    .time(Instant.now())
                    .build());
            return res.map(student -> new StudentDto.Response(
                    student.getId(),
                    String.format("%s %s", student.getFirstName(), student.getLastName()),
                    student.getEmail(),
                    student.getNationalId(),
                    student.getCourses(),
                    student.getCreatedAt()));
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".save"), res.getError());
    }

    @Override
    public ResultWrapper<Boolean> delete(String nationalId) {
        // delete currently works by nationalId
        var res = repo.delete(Student.builder().nationalId(nationalId).build());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .action(LogsAction.STUDENT_REMOVED.name())
                    .data(nationalId)
                    .time(Instant.now())
                    .build());

            return ResultWrapper.ok(true);
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), res.getError());
    }

}
