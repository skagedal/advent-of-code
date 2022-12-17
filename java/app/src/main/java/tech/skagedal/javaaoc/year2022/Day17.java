package tech.skagedal.javaaoc.year2022;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;

@AdventOfCode
public class Day17 {
    public long part1(AdventContext context) {
        return new Game(context.line(), context.explain(), 2022).run();
    }

    public long part2(AdventContext context) {
        return new Game(context.line(), context.explain(), 1000000000000L).run();
    }

    private static final byte[][] shapes = {
        {
            0b0011110
        },
        {
            0b0001000,
            0b0011100,
            0b0001000
        },
        {
            0b0000100,
            0b0000100,
            0b0011100,
        },
        {
            0b0010000,
            0b0010000,
            0b0010000,
            0b0010000
        },
        {
            0b0011000,
            0b0011000
        }
    };

    static class Game {
        private final String instructions;
        private final boolean explain;
        private final long rounds;
        private int currentShape = 0;
        private int currentInstruction = 0;
        private byte[] buf = new byte[4096];
        private int topOfBuf = 0;
        private int fullLine = -1;
        private long previouslyAchievedScore = 0;
        private Map<String, StoredState> map = new HashMap<>();

        private static Base64.Encoder encoder = Base64.getEncoder();

        Game(String instructions, boolean explain, long rounds) {
            this.instructions = instructions;
            this.explain = explain;
            this.rounds = rounds;
        }

        long run() {
            for (long i = 0; i < rounds; i++) {
                StoredState previous = detectCycle(i);
                if (previous != null) {
                    final var cycleLength = i - previous.rounds();
                    final var skippedCycles = (rounds - i) / cycleLength;
                    i += cycleLength * skippedCycles;

                    final var scoreCycle = previouslyAchievedScore + topOfBuf - previous.score();
                    previouslyAchievedScore += scoreCycle * skippedCycles;
                }
                placeRock();
                if (explain) {
                    System.out.printf("== %d ==\n", i);
                    printRocks();
                }
            }
            return previouslyAchievedScore + topOfBuf;
        }

        record StoredState(long rounds, long score) {}

        StoredState detectCycle(long rounds) {
            int len = topOfBuf - (fullLine + 1);
            final var byteBuffer = ByteBuffer.allocate(len + 8);
            byteBuffer.put(buf, fullLine + 1, len);
            byteBuffer.putInt(currentShape);
            byteBuffer.putInt(currentInstruction);

            final var str = encoder.encodeToString(byteBuffer.array());

            final var previous = map.get(str);
            if (previous != null) {
                return previous;
            }
            map.put(str, new StoredState(rounds, previouslyAchievedScore + topOfBuf));
            return null;
        }

        private void printRocks() {
            for (int i = topOfBuf - 1; i >= 0; i--) {
                System.out.println(
                    String.format("%7s", Integer.toBinaryString(buf[i]))
                        .replaceAll("[0\\s]", "·")
                        .replaceAll("1", "■"));
            }
        }

        private void placeRock() {
            byte[] rock = Arrays.copyOf(shapes[currentShape], shapes[currentShape].length);
            currentShape = (currentShape + 1) % shapes.length;

            ensureSize(topOfBuf + 3 + rock.length);
            var currentPosition = topOfBuf + 3;

            while(true) {
                char instruction = instructions.charAt(currentInstruction);
                currentInstruction = (currentInstruction + 1) % instructions.length();

                attemptInstruction(instruction, rock, currentPosition);
                if (isPositionValid(rock, currentPosition - 1)) {
                    currentPosition--;
                } else {
                    break;
                }
            };
            stopRock(rock, currentPosition);
        }

        private boolean isPositionValid(byte[] rock, int position) {
            if (position < 0) {
                return false;
            }
            for (var i = 0; i < rock.length; i++) {
                if ((buf[position + i] & rock[rock.length - i - 1]) != 0) {
                    return false;
                }
            }
            return true;
        }

        private void attemptInstruction(char instruction, byte[] rock, int position) {
            switch (instruction) {
                case '<' -> {
                    for (var i = 0; i < rock.length; i++) {
                        // Flows over right edge?
                        if ((rock[rock.length - i - 1] & 0b1000000) != 0) return;
                        // Clashes with stopped rock?
                        if ((buf[position + i] & rock[rock.length - i - 1] << 1) != 0) return;
                    }
                    for (var i = 0; i < rock.length; i++) {
                        rock[i] <<= 1;
                    }
                }
                case '>' -> {
                    for (var i = 0; i < rock.length; i++) {
                        // Flows over right edge?
                        if ((rock[rock.length - i - 1] & 0b0000001) != 0) return;
                        // Clashes with stopped rock?
                        if ((buf[position + i] & rock[rock.length - i - 1] >> 1) != 0) return;
                    }
                    for (var i = 0; i < rock.length; i++) {
                        rock[i] >>= 1;
                    }
                }
                default -> throw new IllegalStateException("Unknown instruction: " + instruction);
            }
        }

        private void stopRock(byte[] rock, int start) {
            topOfBuf = Integer.max(topOfBuf, start + rock.length);
            for (int i = 0; i < rock.length; i++) {
                buf[start + i] |= rock[rock.length - i - 1];
                if (buf[start + i] == 0b1111111) {
                    fullLine = start + i;
                }
            }
        }

        private void ensureSize(long size) {
            if (size >= buf.length) {
                if (fullLine >= size-buf.length) {
                    previouslyAchievedScore += fullLine + 1;
                    final var newBuf = new byte[buf.length];
                    System.arraycopy(buf, fullLine + 1, newBuf, 0, buf.length - fullLine - 1);
                    buf = newBuf;
                    topOfBuf -= (fullLine + 1);
                    fullLine = -1;
                } else {
                    buf = Arrays.copyOf(buf, buf.length * 2);
                }
            }
        }
    }

    public static void main(String[] args) {
//        AdventOfCodeRunner.example(
//            new Day17(),
//            ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
//        );
        AdventOfCodeRunner.run(new Day17());
    }

}
