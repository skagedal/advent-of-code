package tech.skagedal.javaaoc.year2022;

import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;

public class Day10 extends AocDay {
    private int signalStrengthSum;

    public long part1() {
        var currentCycle = 1;
        var currentX = 1;
        for (var instruction : Streams.toIterable(readExampleLines().map(Day10::parse))) {
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
        return signalStrengthSum;
    }

    private void emit(int cycle, int x) {
        int signalStrength = cycle * x;
        System.out.printf("[%d] x is %d - cycle strength is %d\n", cycle, x, signalStrength);
        if ((cycle - 20) % 40 == 0) {
            System.out.printf("SAMPLING %d\n", signalStrength);
            signalStrengthSum += signalStrength;
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
        System.out.println("Answer: " + new Day10().part1());
    }
}
