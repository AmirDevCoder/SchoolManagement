package presenter.service;

import domain.model.dto.CourseDto;
import domain.model.entity.Logs;
import domain.model.entity.LogsAction;
import domain.repository.CourseRepository;
import domain.service.CourseService;
import domain.service.LoggerService;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository repo;
    private final LoggerService loggerSvc;

    @Override
    public ResultWrapper<List<CourseDto.Response>> getAll() {
        var res = repo.getAll();
        if (res.isSuccess()) {
            return res.map((courses -> courses.stream()
                    .map((course -> new CourseDto.Response(course.getId(), course.getCreatedAt()))).toList()));
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), res.getError());
    }

    @Override
    public ResultWrapper<CourseDto.Response> save(CourseDto.Request req) {
        var res = repo.save(req.toCourse());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .courseName(req.name())
                    .action(LogsAction.COURSE_ADDED.name())
                    .time(Instant.now())
                    .build());
            return res.map((course) -> new CourseDto.Response(course.getId(), course.getCreatedAt()));
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".save"), res.getError());
    }
}
