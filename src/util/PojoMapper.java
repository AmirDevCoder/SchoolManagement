package util;

import java.util.List;

public interface PojoMapper<I,O> {
    O mapTo(I input);
    I mapFrom(O output);
    List<O> mapTo(List<I> inputs);
    List<I> mapFrom(List<O> outputs);
}
