package tech.skagedal.javaaoc.year2015;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;

@AdventOfCode(
    description = "The Ideal Stocking Stuffer"
)
public class Day04 {
    public long part1(AdventContext context) {
        final var digest = DigestUtils.getMd5Digest();
        final var str = context.line();
        for (var i = 0; i < Integer.MAX_VALUE; i++) {
            String theString = str + i;
            final var md5 = digest.digest(theString.getBytes());
            if (hasFiveZeros(md5)) {
                return i;
            }
        }

        throw new IllegalStateException("no answer");
    }

    public long part2(AdventContext context) {
        final var digest = DigestUtils.getMd5Digest();
        final var str = context.line();
        for (var i = 0; i < Integer.MAX_VALUE; i++) {
            String theString = str + i;
            final var md5 = digest.digest(theString.getBytes());
            if (hasSixZeros(md5)) {
                return i;
            }
        }

        throw new IllegalStateException("no answer");
    }

    private static boolean hasFiveZeros(byte[] md5) {
        return Hex.encodeHexString(md5).startsWith("00000");
    }

    private static boolean hasSixZeros(byte[] md5) {
        return Hex.encodeHexString(md5).startsWith("000000");
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day04(), "abcdef");
        AdventOfCodeRunner.example(new Day04(), "pqrstuv");
        AdventOfCodeRunner.run(new Day04());
    }
}
