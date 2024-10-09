package domain.service;

import domain.model.entity.Logs;
import locator.LocatableService;
import util.ResultWrapper;

import java.util.List;

public interface LoggerService extends LocatableService {
    ResultWrapper<List<Logs>> getAll();
    void save(Logs logs);
}
