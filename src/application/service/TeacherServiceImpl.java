package application.service;

import domain.model.dto.TeacherDto;
import domain.model.entity.Logs;
import domain.model.entity.LogsAction;
import domain.model.entity.Teacher;
import domain.repository.TeacherRepository;
import domain.service.LoggerService;
import domain.service.TeacherService;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository repo;
    private final LoggerService loggerSvc;

    @Override
    public ResultWrapper<List<TeacherDto.Response>> getAll() {
        var res = repo.getAll();
        if (res.isSuccess()) {
            return res.map(teachers -> teachers.stream()
                    .map(teacher -> new TeacherDto.Response(
                            teacher.getId(),
                            String.format("%s %s", teacher.getFirstName(), teacher.getLastName()),
                            teacher.getEmail(),
                            teacher.getNationalId(),
                            teacher.getCreatedAt()
                    ))
                    .toList()
            );
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), res.getError());
    }

    @Override
    public ResultWrapper<TeacherDto.Response> save(TeacherDto.Request req) {
        var res = repo.save(req.toTeacher());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .action(LogsAction.TEACHER_REGISTERED.name())
                    .userNationalId(res.getValue().getNationalId())
                    .time(Instant.now())
                    .build());
            return res.map(teacher -> new TeacherDto.Response(
                    teacher.getId(),
                    String.format("%s %s", teacher.getFirstName(), teacher.getLastName()),
                    teacher.getEmail(),
                    teacher.getNationalId(),
                    teacher.getCreatedAt()));
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".save"), res.getError());
    }

    @Override
    public ResultWrapper<Boolean> delete(String nationalId) {
        // delete currently works by nationalId
        var res = repo.delete(Teacher.builder().nationalId(nationalId).build());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .action(LogsAction.TEACHER_REMOVED.name())
                    .userNationalId(nationalId)
                    .time(Instant.now())
                    .build());

            return ResultWrapper.ok(true);
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), res.getError());
    }
}
