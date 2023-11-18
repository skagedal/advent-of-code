package tech.skagedal.javaaoc.tools.sneaky;

public interface SneakySupplier<T> {
    T supply() throws Exception;
}
