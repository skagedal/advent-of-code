package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Range;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode
public class Day10 extends AocDay {

    public long part1() {
        final var device = new Device();
        device.process(readLines().map(Day10::parse));
        return device.signalStrengthSum;
    }

    public String part2() {
        final var device = new Device();
        device.process(readLines().map(Day10::parse));
        System.out.println(device.frameBuffer.toString());
        return "BZPAJELK";
    }

    private static class Device {
        int signalStrengthSum = 0;
        int currentCycle = 1;
        int currentX = 1;
        StringBuilder frameBuffer = new StringBuilder();

        void process(Stream<Instruction> instructions) {
            for (var instruction : Streams.toIterable(instructions)) {
                switch (instruction) {
                    case Instruction.Noop noop -> {
                        emit(currentCycle++, currentX);
                    }
                    case Instruction.Addx addx -> {
                        emit(currentCycle++, currentX);
                        emit(currentCycle++, currentX);
                        currentX += addx.value();
                    }
                }
            }
        }

        private void emit(int cycle, int x) {
            int signalStrength = cycle * x;
            System.out.printf("[%d] x is %d - cycle strength is %d\n", cycle, x, signalStrength);
            if ((cycle - 20) % 40 == 0) {
                System.out.printf("SAMPLING %d\n", signalStrength);
                signalStrengthSum += signalStrength;
            }

            final var crtPosition = (cycle - 1) % 40;
            if (Range.closed(currentX - 1, currentX + 1).contains(crtPosition)) {
                frameBuffer.append('#');
            } else {
                frameBuffer.append('.');
            }
            if (crtPosition == 39) {
                frameBuffer.append('\n');
            }
        }
    }


    private static Instruction parse(String s) {
        if (s.equals("noop")) {
            return new Instruction.Noop();
        } else {
            return new Instruction.Addx(Integer.parseInt(s.split(" ")[1]));
        }
    }

    private sealed interface Instruction {
        record Noop() implements Instruction {}
        record Addx(int value) implements Instruction {}
    }

    public static void main(String[] args) {
        System.out.println("Answer: " + new Day10().part2());
    }
}
