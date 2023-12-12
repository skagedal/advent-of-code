package tech.skagedal.javaaoc.year2022;

import org.junit.jupiter.api.Test;
import tech.skagedal.javaaoc.year2023.Day12;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {
    @Test
    void simpleTests() {
        // Skip blanks
        assertEquals(1, countArrangements("...", List.of()));
        // No match
        assertEquals(0, countArrangements("....", List.of(1)));
        // Simple match
        assertEquals(1, countArrangements("#", List.of(1)));
        assertEquals(1, countArrangements("##", List.of(2)));
        assertEquals(1, countArrangements("###", List.of(3)));
        assertEquals(1, countArrangements("#.", List.of(1)));
        assertEquals(1, countArrangements("#.#", List.of(1, 1)));
        assertEquals(1, countArrangements("##.###", List.of(2, 3)));
        assertEquals(1, countArrangements("#.##.###", List.of(1,2,3)));
        // No match
        assertEquals(0, countArrangements("#.##.###", List.of(1, 3)));
        // Unknown match
        assertEquals(2, countArrangements("??", List.of(1)));
    }

    @Test
    void exampleTests() {
        assertEquals(
            1,
            parseAndCountArrangements("???.### 1,1,3")
        );
        assertEquals(
            4,
            parseAndCountArrangements(".??..??...?##. 1,1,3")
        );
        assertEquals(
            10,
            parseAndCountArrangements("?###???????? 3,2,1")
        );
        //            .??..??...?##. 1,1,3
        //            ?#?#?#?#?#?#?#? 1,3,1,6
        //            ????.#...#... 4,1,1
        //            ????.######..#####. 1,6,5
        //
    }

    private static long parseAndCountArrangements(String s) {
        return Day12.ConditionRecord.fromString(s).numberOfPossibleArrangements();
    }

    private static long countArrangements(String condition, List<Integer> groups) {
        return new Day12.ConditionRecord(condition, groups).numberOfPossibleArrangements();
    }
}
