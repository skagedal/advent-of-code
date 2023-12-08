package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@AdventOfCode(
    description = "Camel Cards"
)
public class Day07 {
    public long part1(AdventContext context) {
        return Streams.enumerated(context.lines()
            .map(BidHand::parse)
            .sorted(BidHand.comparator(false, "23456789TJQKA")))
            .mapToLong(enumeratedHand -> (enumeratedHand.number() + 1) * enumeratedHand.value().bid())
            .sum();
    }

    public long part2(AdventContext context) {
        return Streams.enumerated(context.lines()
                .map(BidHand::parse)
                .sorted(BidHand.comparator(true, "J23456789TQKA")))
            .mapToLong(enumeratedHand -> (enumeratedHand.number() + 1) * enumeratedHand.value().bid())
            .sum();
    }

    record HandClassifier(HandType handType, List<Pattern> pattern, Map<Integer, HandType> joker) {}

    static List<HandClassifier> classifiers = List.of(
        new HandClassifier(
            HandType.FIVE_OF_A_KIND,
            List.of(Pattern.compile("(.)\\1{4}")),
            Map.of()
        ),
        new HandClassifier(
            HandType.FOUR_OF_A_KIND,
            List.of(Pattern.compile("(.)\\1{3}")),
            Map.of(
                1, HandType.FIVE_OF_A_KIND,
                4, HandType.FIVE_OF_A_KIND
            )
        ),
        new HandClassifier(
            HandType.FULL_HOUSE,
            List.of(
                Pattern.compile("(.)\\1{2}(.)\\2"),
                Pattern.compile("(.)\\1(.)\\2{2}")
            ),
            Map.of(
                2, HandType.FIVE_OF_A_KIND,
                3, HandType.FIVE_OF_A_KIND
            )
        ),
        new HandClassifier(
            HandType.THREE_OF_A_KIND,
            List.of(Pattern.compile("(.)\\1{2}")),
            Map.of(
                1, HandType.FOUR_OF_A_KIND,
                3, HandType.FOUR_OF_A_KIND
            )
        ),
        new HandClassifier(
            HandType.TWO_PAIR,
            List.of(Pattern.compile("(.)\\1.*(.)\\2")),
            Map.of(
                1, HandType.FULL_HOUSE,
                2, HandType.FOUR_OF_A_KIND
            )
        ),
        new HandClassifier(
            HandType.ONE_PAIR,
            List.of(Pattern.compile("(.)\\1")),
            Map.of(
                1, HandType.THREE_OF_A_KIND,
                2, HandType.THREE_OF_A_KIND
            )
        ),
        new HandClassifier(
            HandType.HIGH_CARD,
            List.of(Pattern.compile(".")),
            Map.of(
                1, HandType.ONE_PAIR
            )
        )
    );

    public record BidHand(
        String hand,
        long bid
    ) {
        static BidHand parse(String line) {
            var split = line.split(" ");
            return new BidHand(
                split[0],
                Long.parseLong(split[1])
            );
        }

        public static Comparator<BidHand> comparator(boolean useJokers, String rankOrdering) {
            return Comparator
                .comparingLong((BidHand bidHand) -> bidHand.typeValue(useJokers))
                .thenComparing(bidHand -> bidHand.rankValue(rankOrdering));
        }

        HandType classify(boolean useJokers) {
            var sortedHand = hand.codePoints().sorted().collect(StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append);

            var jokerCount = useJokers ? hand.codePoints().filter(c -> c == 'J').count() : 0;

            classifiers.stream()
                .findFirst()
                .map(HandClassifier::handType)

        }

        public long typeValue(boolean useJokers) {
            return classify(useJokers).value;
        }

        private String rankValue(String rankOrdering) {
            return hand.codePoints().map(cp ->
                rankOrdering.indexOf(cp) + 'A'
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
