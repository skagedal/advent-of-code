package tech.skagedal.javaaoc.tools;

import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Zip2Spliterator<T1, T2, U> implements Spliterator<U> {
    private final Spliterator<T1> t1Spliterator;
    private final Spliterator<T2> t2Spliterator;
    private final BiFunction<T1, T2, U> combiner;

    public Zip2Spliterator(Spliterator<T1> t1Spliterator, Spliterator<T2> t2Spliterator, BiFunction<T1, T2, U> combiner) {
        this.t1Spliterator = t1Spliterator;
        this.t2Spliterator = t2Spliterator;
        this.combiner = combiner;
    }

    @Override
    public boolean tryAdvance(Consumer<? super U> action) {
        AtomicBoolean accepted = new AtomicBoolean(false);
        t1Spliterator.tryAdvance(t1 ->
            t2Spliterator.tryAdvance(t2 -> {
                action.accept(combiner.apply(t1, t2));
                accepted.set(true);
            })
        );
        return accepted.get();
    }

    @Override
    public Spliterator<U> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Math.min(t1Spliterator.estimateSize(), t2Spliterator.estimateSize());
    }

    @Override
    public int characteristics() {
        return
            // Characteristics that are always set
            ORDERED |
            // Characteristics we inherit from zipped spliterators if both have them
            (t1Spliterator.characteristics() & t2Spliterator.characteristics() & (SIZED | IMMUTABLE | NONNULL));
    }
}
