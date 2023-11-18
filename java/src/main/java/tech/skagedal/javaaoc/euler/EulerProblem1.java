package tech.skagedal.javaaoc.euler;


import tech.skagedal.javaaoc.tools.streamsetc.Streams;

public class EulerProblem1 {
    public long part1() {
        return Streams.<Long>make(channel -> {
                long a = 0, b = 1;
                while (true) {
                    channel.yield(a);
                    final var next = a + b;
                    a = b;
                    b = next;
                }
            })
            .mapToLong(Long::longValue)
            .takeWhile(val -> val <= 4000000)
            .filter(val -> val % 2 == 0)
            .sum();
    }
}
