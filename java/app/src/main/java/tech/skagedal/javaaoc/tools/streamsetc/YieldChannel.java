package tech.skagedal.javaaoc.tools.streamsetc;

public interface YieldChannel<T> {
    void yield(T value);
}
