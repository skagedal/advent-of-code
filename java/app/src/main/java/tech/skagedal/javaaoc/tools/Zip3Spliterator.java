package tech.skagedal.javaaoc.tools;

import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Zip3Spliterator<T1, T2, T3, R> implements Spliterator<R> {
    private final Spliterator<T1> t1Spliterator;
    private final Spliterator<T2> t2Spliterator;
    private final Spliterator<T3> t3Spliterator;
    private final Function3<T1, T2, T3, R> combiner;

    public Zip3Spliterator(Spliterator<T1> t1Spliterator, Spliterator<T2> t2Spliterator, Spliterator<T3> t3Spliterator,
                           Function3<T1, T2, T3, R> combiner) {
        this.t1Spliterator = t1Spliterator;
        this.t2Spliterator = t2Spliterator;
        this.t3Spliterator = t3Spliterator;
        this.combiner = combiner;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        AtomicBoolean accepted = new AtomicBoolean(false);
        t1Spliterator.tryAdvance(t1 ->
            t2Spliterator.tryAdvance(t2 ->
                t3Spliterator.tryAdvance(t3 -> {
                    action.accept(combiner.apply(t1, t2, t3));
                    accepted.set(true);
                })));
        return accepted.get();
    }

    @Override
    public Spliterator<R> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return SpliteratorUtil.composedEstimateSize(
            t1Spliterator,
            t2Spliterator,
            t3Spliterator
        );
    }

    @Override
    public int characteristics() {
        return SpliteratorUtil.composedCharacteristics(
            t1Spliterator,
            t2Spliterator,
            t3Spliterator
        );
    }
}
