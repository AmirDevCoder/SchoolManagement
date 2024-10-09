package domain.service;

import domain.model.dto.CourseDto;
import locator.LocatableService;
import util.ResultWrapper;

import java.util.List;

public interface CourseService extends LocatableService {
    ResultWrapper<List<CourseDto.Response>> getAll();
    ResultWrapper<CourseDto.Response> save(CourseDto.Request req);
}
