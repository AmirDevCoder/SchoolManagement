package util;

import java.util.List;

public interface BaseRepository<T> {
    /// upsert
    ResultWrapper<T> save(T t);
    void delete(T t);
    ResultWrapper<List<T>> getAll();
}
