package tech.skagedal.javaaoc.tools;

import java.util.Spliterator;
import java.util.function.Consumer;

public class StringSpliterator implements Spliterator<String> {
    private final String[] strings;
    private int nextIndex = 0;

    public StringSpliterator(String[] strings) {
        this.strings = strings;
    }

    @Override
    public boolean tryAdvance(Consumer<? super String> action) {
        if (nextIndex >= strings.length) {
            return false;
        } else {
            action.accept(strings[nextIndex]);
            nextIndex++;
            return true;
        }
    }

    @Override
    public Spliterator<String> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return strings.length - nextIndex;
    }

    @Override
    public int characteristics() {
        return ORDERED | IMMUTABLE | SIZED;
    }
}
