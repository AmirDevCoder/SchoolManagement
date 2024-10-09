package data.repository;

import domain.model.entity.Grade;
import domain.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.sql.Connection;
import java.util.List;

@RequiredArgsConstructor
public class GradeRepositoryImpl implements GradeRepository {
    private final Connection connection;

    @Override
    public ResultWrapper<Grade> save(Grade grade) {
        throw new RuntimeException("implement me");
    }

    @Override
    public ResultWrapper<Boolean> delete(Grade grade) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public ResultWrapper<List<Grade>> getAll() {
        throw new RuntimeException("implement me");
    }
}
