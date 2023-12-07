package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import javax.crypto.spec.PSource;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@AdventOfCode(
    description = "Camel Cards"
)
public class Day07 {
    public long part1(AdventContext context) {
        return Streams.enumerated(context.lines()
            .map(BidHand::parse)
            .sorted())
            .mapToLong(enumeratedHand -> (enumeratedHand.number() + 1) * enumeratedHand.value().bid())
            .sum();
    }

    public record BidHand(
        String hand,
        long bid
    ) implements Comparable {
        static BidHand parse(String line) {
            var split = line.split(" ");
            return new BidHand(
                split[0],
                Long.parseLong(split[1])
            );
        }

        static Pattern FIVE_OF_A_KIND = Pattern.compile("(.)\\1{4}");
        static Pattern FOUR_OF_A_KIND = Pattern.compile("(.)\\1{3}");
        static List<Pattern> FULL_HOUSE = List.of(
            Pattern.compile("(.)\\1{2}(.)\\2"),
            Pattern.compile("(.)\\1(.)\\2{2}")
        );
        static Pattern THREE_OF_A_KIND = Pattern.compile("(.)\\1{2}");
        static Pattern TWO_PAIR = Pattern.compile("(.)\\1.*(.)\\2");
        static Pattern ONE_PAIR = Pattern.compile("(.)\\1");

        HandType classify() {
            var sortedHand = hand.codePoints().sorted().collect(StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append);
            if (FIVE_OF_A_KIND.matcher(sortedHand).find()) {
                return HandType.FIVE_OF_A_KIND;
            }
            if (FOUR_OF_A_KIND.matcher(sortedHand).find()) {
                return HandType.FOUR_OF_A_KIND;
            }
            if (FULL_HOUSE.stream().anyMatch(p -> p.matcher(sortedHand).find())) {
                return HandType.FULL_HOUSE;
            }
            if (THREE_OF_A_KIND.matcher(sortedHand).find()) {
                return HandType.THREE_OF_A_KIND;
            }
            if (TWO_PAIR.matcher(sortedHand).find()) {
                return HandType.TWO_PAIR;
            }
            if (ONE_PAIR.matcher(sortedHand).find()) {
                return HandType.ONE_PAIR;
            }
            return HandType.HIGH_CARD;
        }

        @Override
        public int compareTo(Object other) {
            if (other instanceof BidHand otherHand) {
                var thisType = classify();
                var otherType = otherHand.classify();
                if (thisType.value > otherType.value) {
                    return 1;
                } else if (thisType.value < otherType.value) {
                    return -1;
                } else {
                    return rankValue().compareTo(otherHand.rankValue());
                }
            }
            return 0;
        }

        private String rankValue() {
            return hand.codePoints().map(cp ->
                "23456789TJQKA".indexOf(cp) + 'A'
            ).collect(StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append)
                .toString();
        }
    }

    enum HandType {
        HIGH_CARD(1),
        ONE_PAIR(2),
        TWO_PAIR(3),
        THREE_OF_A_KIND(4),
        FULL_HOUSE(5),
        FOUR_OF_A_KIND(6),
        FIVE_OF_A_KIND(7);

        public final long value;
        HandType(long value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day07(), """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483""");
        AdventOfCodeRunner.run(new Day07());
    }
}
