package domain.service;

import domain.model.dto.TeacherDto;
import locator.LocatableService;
import util.ResultWrapper;

import java.util.List;

public interface TeacherService extends LocatableService {
    ResultWrapper<List<TeacherDto.Response>> getAll();
    ResultWrapper<TeacherDto.Response> save(TeacherDto.Request req);
    ResultWrapper<Boolean> delete(String nationalId);
}
