package presentation.service;

import domain.repository.GradeRepository;
import domain.service.GradeService;
import domain.service.LoggerService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository repo;
    private final LoggerService loggerSvc;
}
