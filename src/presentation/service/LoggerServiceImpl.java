package presentation.service;

import domain.model.entity.Logs;
import domain.repository.LoggerRepository;
import domain.service.LoggerService;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.util.List;

@RequiredArgsConstructor
public class LoggerServiceImpl implements LoggerService {
    private final LoggerRepository repo;

    @Override
    public ResultWrapper<List<Logs>> getAll() {
        return repo.getAll();
    }

    @Override
    public void save(Logs logs) {
        repo.save(logs);
    }
}
