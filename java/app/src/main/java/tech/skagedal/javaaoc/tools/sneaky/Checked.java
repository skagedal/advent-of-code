package tech.skagedal.javaaoc.tools.sneaky;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class Checked {

    public static <T> Supplier<T> supplier(SneakySupplier<T> supplier) {
        return () -> {
            try {
                return supplier.supply();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }
    public static IntSupplier intSupplier(SneakyIntSupplier supplier) {
        return () -> {
            try {
                return supplier.supply();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    public static Runnable runnable(SneakyRunnable runnable) {
        return () -> {
            try {
                runnable.run();
                ;
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

}
