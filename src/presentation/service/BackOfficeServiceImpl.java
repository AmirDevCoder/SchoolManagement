package presentation.service;

import domain.model.dto.BackOfficeDto;
import domain.model.entity.BackOffice;
import domain.model.entity.Logs;
import domain.model.entity.LogsAction;
import domain.repository.BackOfficeRepository;
import domain.service.BackOfficeService;
import domain.service.LoggerService;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class BackOfficeServiceImpl implements BackOfficeService {
    private final BackOfficeRepository repo;
    private final LoggerService loggerSvc;

    @Override
    public ResultWrapper<BackOfficeDto.Response> save(BackOfficeDto.Request req) {
        var res = repo.save(req.toBackOffice());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .action(LogsAction.BACKOFFICE_ADDED.name())
                    .userNationalId(req.nationalId())
                    .time(Instant.now())
                    .build());

            return res.map(backOffice -> new BackOfficeDto.Response(
                    backOffice.getId().toString(), String.format("%s %s", backOffice.getFirstName(), backOffice.getLastName()))
            );
        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".save"), res.getError());
    }

    @Override
    public ResultWrapper<List<BackOfficeDto.Response>> getAll() {
        var res = repo.getAll();
        if (res.isSuccess()) {
            return res.map(backOffices -> backOffices.stream()
                    .map(backOffice -> new BackOfficeDto.Response(
                            backOffice.getId().toString(), String.format("%s %s", backOffice.getFirstName(), backOffice.getLastName())
                    )).toList());

        }

        return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), res.getError());
    }

    @Override
    public ResultWrapper<Boolean> delete(String nationalId) {
        // backOffice deleted by just nationalId
        var res = repo.delete(BackOffice.builder().nationalId(nationalId).build());
        if (res.isSuccess()) {
            loggerSvc.save(Logs.builder()
                    .action(LogsAction.BACKOFFICE_REMOVED.name())
                    .userNationalId(nationalId)
                    .time(Instant.now())
                    .build());

            return ResultWrapper.ok(true);
        }
        return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), res.getError());
    }
}
