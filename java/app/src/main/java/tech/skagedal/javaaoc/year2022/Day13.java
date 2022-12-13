package tech.skagedal.javaaoc.year2022;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.math.Longs;

@AdventOfCode
public class Day13 {
    // Part 1

    public long part1(AdventContext context) {
        return Streams.enumerated(
                Streams.splitting(context.lines(), String::isBlank)
                    .map(list -> list.stream().map(Day13::parseLine).toList())
                    .map(Packet.ListPacket::new))
            .filter(enumeratedListPacket -> Streams.isSorted(enumeratedListPacket.value().packets.stream()))
            .mapToLong(elist -> elist.number() + 1)
            .sum();
    }

    // Part 2

    private static final Packet DIVIDER_PACKET_1 = list(list(intx(2)));
    private static final Packet DIVIDER_PACKET_2 = list(list(intx(6)));
    private static final List<Packet> DIVIDER_PACKETS = List.of(DIVIDER_PACKET_1, DIVIDER_PACKET_2);

    public long part2(AdventContext context) {
        return Streams.enumerated(
            Stream.concat(
                context.lines()
                    .filter(Predicate.not(String::isBlank))
                    .map(Day13::parseLine),
                DIVIDER_PACKETS.stream()
            ).sorted())
            .filter(enumeratedPacket -> DIVIDER_PACKETS.contains(enumeratedPacket.value()))
            .mapToLong(enumeratedPacket -> enumeratedPacket.number() + 1)
            .reduce(1, Longs::multiply);
    }

    // Parsing

    private static final Pattern TOKENS = Pattern.compile("\\[|[0-9]+|]");

    public static Packet parseLine(String line) {
        final var iterator = TOKENS.matcher(line).results().map(MatchResult::group).iterator();
        final var firstToken = iterator.next();
        return parseToken(firstToken, iterator);
    }

    private static Packet parseToken(String token, Iterator<String> tokens) {
        if (token.equals("[")) {
            return parseList(tokens);
        }
        return new Packet.IntegerPacket(Integer.parseInt(token));
    }

    private static Packet parseList(Iterator<String> tokens) {
        final var list = new ArrayList<Packet>();
        while (tokens.hasNext()) {
            final var token = tokens.next();
            if (token.equals("]")) {
                return new Packet.ListPacket(list);
            }
            list.add(parseToken(token, tokens));
        }
        throw new IllegalStateException("Unexpected end");
    }

    // Data structure

    public sealed interface Packet extends Comparable<Packet> {
        record IntegerPacket(int i) implements Packet {
            @Override
            public int compareTo(Packet other) {
                return switch (other) {
                    case IntegerPacket ip -> i - ip.i;
                    case ListPacket lp -> new ListPacket(List.of(this)).compareTo(other);
                };
            }
        }

        record ListPacket(List<Packet> packets) implements Packet {
            @Override
            public int compareTo(Packet other) {
                return switch (other) {
                    case IntegerPacket ip -> compareTo(new ListPacket(List.of(ip)));
                    case ListPacket lp -> {
                        final var left = packets.iterator();
                        final var right = lp.packets.iterator();
                        while (left.hasNext() || right.hasNext()) {
                            if (!left.hasNext()) yield -1;
                            if (!right.hasNext()) yield 1;
                            final var compared = left.next().compareTo(right.next());
                            if (compared != 0) yield compared;
                        }
                        yield 0;
                    }
                };
            }
        }
    }

    // Data construction helpers

    public static Day13.Packet list(Day13.Packet... packets) {
        return new Day13.Packet.ListPacket(List.of(packets));
    }

    public static Day13.Packet intx(int i) {
        return new Day13.Packet.IntegerPacket(i);
    }
}
