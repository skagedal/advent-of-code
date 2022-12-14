package tech.skagedal.javaaoc.tools;

public interface YieldChannel<T> {
    void yield(T value);
}
