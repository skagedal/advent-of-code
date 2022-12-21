package tech.skagedal.javaaoc.tools.collections;

import java.util.HashMap;
import java.util.Map;

public class Counter<T> {

    private Map<T, Integer> counts = new HashMap<>();

    public void increase(T value) {
        counts.put(value, getCount(value));
    }

    public int getCount(T flower) {
        return counts.getOrDefault(flower, 0);
    }

}
