package domain.service;

import domain.model.dto.ExamDto;
import locator.LocatableService;
import util.ResultWrapper;

import java.util.List;

public interface ExamService extends LocatableService {
    ResultWrapper<List<ExamDto.Response>> getAll();
    ResultWrapper<ExamDto.Response> save(ExamDto.Request req);
}
