package tech.skagedal.javaaoc.year2023;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05Test {
    @Test
    void map_simple_values() {
        var map = new Day05.Map(List.of(
            Day05.MapRange.create(0, 2, 5)));

        assertEquals(2, map.map(0));
        assertEquals(6, map.map(4));
        assertEquals(6, map.map(6));
    }

    @Test
    void combine_non_overlapping() {
        var map1 = new Day05.Map(List.of(
            Day05.MapRange.create(2, 4, 5)));
        var map2 = new Day05.Map(List.of(
            Day05.MapRange.create(10, 13, 5)));

        assertMapsTo(map1, map2, 0, 0);
        assertMapsTo(map1, map2, 3, 5);
        assertMapsTo(map1, map2, 8, 8);
        assertMapsTo(map1, map2, 10, 13);
    }

    @Test
    void combine_overlapping() {
        var map1 = new Day05.Map(List.of(
            Day05.MapRange.create(0, 10, 50)));
        var map2 = new Day05.Map(List.of(
            Day05.MapRange.create(20, 30, 10)));

        assertMapsTo(map1, map2, 10, 30);
    }

    private static void assertMapsTo(Day05.Map map1, Day05.Map map2, int initial, int expected) {
        assertEquals(expected, map2.map(map1.map(initial)), "Unexpected value in uncombined mapping");
        assertEquals(expected, map1.then(map2).map(initial), "Unexpected value in combined mapping");
    }
}
