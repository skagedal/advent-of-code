package tech.skagedal.javaaoc.tools;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AocOcr {
    // https://github.com/bsoyka/advent-of-code-ocr/tree/main

    public String scan(String text) {
        return scan(text.lines().toList());
    }

    private String scan(List<String> list) {
        final var rows = list.size();
        final var cols = list.get(0).length();
        return null;
    }

    private static Map.Entry<String, String> entry(String a, String b) {
        return Map.entry(a, b);
    }
    private static Map.Entry<String, String> letter(String letter, String font) {
        return Map.entry(font, letter);
    }

    private static Map<String, String> ALPHABET_6 = Stream.of(
        letter("A", ".##.\n#..#\n#..#\n####\n#..#\n#..#"),
        letter("B", "###.\n#..#\n###.\n#..#\n#..#\n###."),
        letter("C", ".##.\n#..#\n#...\n#...\n#..#\n.##."),
        letter("E", "####\n#...\n###.\n#...\n#...\n####"),
        letter("F", "####\n#...\n###.\n#...\n#...\n#..."),
        letter("G", ".##.\n#..#\n#...\n#.##\n#..#\n.###"),
        letter("H", "#..#\n#..#\n####\n#..#\n#..#\n#..#"),
        letter("I", ".###\n..#.\n..#.\n..#.\n..#.\n.###"),
        letter("J", "..##\n...#\n...#\n...#\n#..#\n.##."),
        letter("K", "#..#\n#.#.\n##..\n#.#.\n#.#.\n#..#"),
        letter("L", "#...\n#...\n#...\n#...\n#...\n####"),
        letter("O", ".##.\n#..#\n#..#\n#..#\n#..#\n.##."),
        letter("P", "###.\n#..#\n#..#\n###.\n#...\n#..."),
        letter("R", "###.\n#..#\n#..#\n###.\n#.#.\n#..#"),
        letter("S", ".###\n#...\n#...\n.##.\n...#\n###."),
        letter("U", """
            #..#
            #..#
            #..#
            #..#
            #..#
            .##."""),
        letter("Y", """
            #...
            #...
            .#.#
            ..#.
            ..#.
            ..#."""),
        letter("Z", """
            ####
            ...#
            ..#.
            .#..
            #...
            ####""")
        )
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
}
