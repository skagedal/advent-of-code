package tech.skagedal.javaaoc.tools;

public interface StreamMaker<T> {
    void yield(T value);
}
