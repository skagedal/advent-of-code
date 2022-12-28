package tech.skagedal.javaaoc.year2015;

import java.util.function.Predicate;
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
        return findMd5Prefix(context.line(), Day04::hasFiveZeros);
    }

    public long part2(AdventContext context) {
        return findMd5Prefix(context.line(), Day04::hasSixZeros);
    }

    private long findMd5Prefix(String line, Predicate<byte[]> stopPredicate) {
        final var digest = DigestUtils.getMd5Digest();
        for (var i = 0; i < Integer.MAX_VALUE; i++) {
            String theString = line + i;
            final var md5 = digest.digest(theString.getBytes());
            if (stopPredicate.test(md5)) {
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
//        AdventOfCodeRunner.example(new Day04(), "abcdef");
//        AdventOfCodeRunner.example(new Day04(), "pqrstuv");
        AdventOfCodeRunner.run(new Day04());
    }
}
