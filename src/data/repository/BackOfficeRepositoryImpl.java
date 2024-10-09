package data.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import domain.model.entity.BackOffice;
import domain.repository.BackOfficeRepository;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BackOfficeRepositoryImpl implements BackOfficeRepository {
    private final MongoCollection<BackOffice> collection;

    @Override
    public ResultWrapper<BackOffice> save(BackOffice backOffice) {
        try {
            var res = collection.insertOne(backOffice);
            backOffice.setId(Objects.requireNonNull(res.getInsertedId()).asDBPointer().getId());
            return ResultWrapper.ok(backOffice);
        } catch (Exception e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".save"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<Boolean> delete(BackOffice backOffice) {
        try {
            collection.deleteOne(Filters.eq("nationalId", backOffice.getNationalId()));
            return ResultWrapper.ok(true);
        } catch (Exception e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".delete"), e.getMessage());
        }
    }

    @Override
    public ResultWrapper<List<BackOffice>> getAll() {
        try {
            return ResultWrapper.ok(collection.find().into(new ArrayList<>()));
        } catch (Exception e) {
            return ResultWrapper.err(getClass().getSimpleName().concat(".getAll"), e.getMessage());
        }
    }
}
