package presentation.service;

import domain.model.dto.ExamDto;
import domain.model.entity.Logs;
import domain.model.entity.LogsAction;
import domain.repository.ExamRepository;
import domain.service.ExamService;
import domain.service.LoggerService;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository repo;
    private final LoggerService loggerSvc;

    @Override
    public ResultWrapper<List<ExamDto.Response>> getAll() {
        var res = repo.getAll();
        if (res.isSuccess()) {
            return res.map(exams -> exams.stream()
                    .map(exam -> new ExamDto.Response(exam.getId(), exam.getName(), exam.getCourseId(), exam.getTeacherId(), exam.getCreatedAt())).toList());
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), res.getError());
    }

    @Override
    public ResultWrapper<ExamDto.Response> save(ExamDto.Request req) {
        var res = repo.save(req.toExam());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .data(req.name())
                    .action(LogsAction.EXAM_ADDED.name())
                    .time(Instant.now())
                    .build());
            return res.map(exam -> new ExamDto.Response(exam.getId(), exam.getName(), exam.getCourseId(), exam.getTeacherId(), exam.getCreatedAt()));
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".save"), res.getError());
    }
}
