package domain.service;

import domain.model.dto.BackOfficeDto;
import locator.LocatableService;
import util.ResultWrapper;

import java.util.List;

public interface BackOfficeService extends LocatableService {
    ResultWrapper<BackOfficeDto.Response> save(BackOfficeDto.Request req);
    ResultWrapper<List<BackOfficeDto.Response>> getAll();
    ResultWrapper<Boolean> delete(String nationalId);
}
