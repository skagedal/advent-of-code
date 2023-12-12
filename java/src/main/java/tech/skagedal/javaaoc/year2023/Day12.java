package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Ints;
import tech.skagedal.javaaoc.tools.math.Longs;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AdventOfCode(
    description = ""
)
public class Day12 {
    public long part1(AdventContext context) {
        return context.lines()
            .mapToLong(Day12::numberOfPossibleArrangements)
            .sum();
    }

    public long part2(AdventContext context) {
        return 0;
    }

    private static long numberOfPossibleArrangements(String s) {
        var conditionRecord = ConditionRecord.fromString(s);
        return conditionRecord.numberOfPossibleArrangements();
    }

    public record ConditionRecord(CharSequence condition, List<Integer> groups) {
        public static ConditionRecord fromString(String s) {
            var parts = s.split(" ");
            return new ConditionRecord(
                parts[0],
                Ints.inString(parts[1]).toList()
            );
        }


        public long numberOfPossibleArrangements() {
            if (condition.isEmpty()) {
                return groups.isEmpty() ? 1 : 0;
            }
            return switch (condition.charAt(0)) {
                case '.' ->
                        new ConditionRecord(condition.subSequence(1, condition.length()), groups).numberOfPossibleArrangements();
                case '#' -> {
                    if (groups.isEmpty()) {
                        yield 0;
                    }
                    var matchLength = groups.getFirst();
                    var pattern = Pattern.compile("^([#?]{%s}[\\.?]?).*$".formatted(matchLength));
                    var matcher = pattern.matcher(condition);
                    if (matcher.matches()) {
                        var remainingCondition = condition.subSequence(matcher.group(1).length(), condition.length());
                        var remainingGroups = groups.subList(1, groups.size());
                        yield new ConditionRecord(remainingCondition, remainingGroups)
                            .numberOfPossibleArrangements();
                    } else {
                        yield 0;
                    }
                }
                case '?' -> {
                    var firstCase = new ConditionRecord("#" + condition.subSequence(1, condition.length()), groups);
                    var secondCase = new ConditionRecord("." + condition.subSequence(1, condition.length()), groups);
                    yield firstCase.numberOfPossibleArrangements() + secondCase.numberOfPossibleArrangements();
                }
                default -> throw new IllegalArgumentException();
            };
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day12(), """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1""");
    }
}
