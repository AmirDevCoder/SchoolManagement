package data.repository;

import com.mongodb.client.MongoCollection;
import domain.model.entity.Logs;
import domain.repository.LoggerRepository;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LoggerRepositoryImpl implements LoggerRepository {
    //TODO: use elastic-search for log instead of mongoDB
    private final MongoCollection<Logs> collection;

    @Override
    public ResultWrapper<Logs> save(Logs logs) {
        try {
            collection.insertOne(logs);
            return ResultWrapper.empty();
        } catch (Exception e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> delete(Logs logs) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public ResultWrapper<List<Logs>> getAll() {
        try {
            List<Logs> logs = collection.find().into(new ArrayList<>());
            return ResultWrapper.ok(logs);
        } catch (Exception e) {
            return ResultWrapper.err(getClass().getSimpleName().concat("getAll"), e.getMessage());
        }
    }
}
