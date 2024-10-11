package domain.util;

import util.ResultWrapper;

import java.util.List;

public interface BaseRepository<T> {
    /// upsert
    ResultWrapper<T> save(T t);
    ResultWrapper<Boolean> delete(T t);
    ResultWrapper<List<T>> getAll();
}
