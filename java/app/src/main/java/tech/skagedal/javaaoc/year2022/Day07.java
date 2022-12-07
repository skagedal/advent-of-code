package tech.skagedal.javaaoc.year2022;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;

public class Day07 extends AocDay {
    public long part1() {
        return readTree().depthFirstStream()
            .filter(node -> node instanceof DirectoryNode)
            .filter(node -> node.getSize() <= 100000)
            .mapToLong(Node::getSize)
            .sum();
    }

    public long part2() {
        final var tree = readTree();
        final var totalUsed = tree.getSize();
        final var targetUsed = 70000000 - 30000000;
        final var minimumToDelete = totalUsed - targetUsed;

        return tree.depthFirstStream()
            .filter(node -> node instanceof DirectoryNode)
            .filter(node -> node.getSize() >= minimumToDelete)
            .mapToLong(Node::getSize)
            .min()
            .orElseThrow();
    }

    private Node readTree() {
        final Iterable<Line> lines = () -> readLines().map(Day07::parseLine).iterator();
        final Stack<String> currentPath = new Stack<>();
        final var root = new DirectoryNode("/");

        for (var line : lines) {
            switch (line) {
                case CdCommand cd -> {
                    switch (cd.relativeDirectory()) {
                        case "/" -> currentPath.removeAllElements();
                        case ".." -> currentPath.pop();
                        default -> currentPath.push(cd.relativeDirectory());
                    }
                }
                case LsCommand ls -> {}
                case DirectoryListing dir -> root.addDirectory(currentPath, dir.directoryName());
                case FileListing file -> root.addFile(currentPath, file.fileName(), file.size());
            }
        }
        return root;
    }

    sealed interface Line { }
    record CdCommand(String relativeDirectory) implements Line { }
    record LsCommand() implements Line { }
    record DirectoryListing(String directoryName) implements Line {}
    record FileListing(long size, String fileName) implements Line {}

    private static final Pattern CD_PATTERN = Pattern.compile("^\\$\scd\s([a-z.]+|/|\\.\\.)$");
    private static final Pattern LS_PATTERN = Pattern.compile("^\\$\sls$");
    private static final Pattern DIR_PATTERN = Pattern.compile("^dir\s([a-z.]+)$");
    private static final Pattern FILE_PATTERN = Pattern.compile("^([0-9]+)\s([a-z.]+)$");

    record LineFactory(Pattern pattern, Function<MatchResult, Line> lineSupplier) { }

    private static final List<LineFactory> factories = List.of(
        new LineFactory(CD_PATTERN, match -> new CdCommand(match.group(1))),
        new LineFactory(LS_PATTERN, match -> new LsCommand()),
        new LineFactory(DIR_PATTERN, match -> new DirectoryListing(match.group(1))),
        new LineFactory(FILE_PATTERN, match -> new FileListing(Long.parseLong(match.group(1)), match.group(2)))
    );

    public static Line parseLine(String line) {
        return factories.stream()
            .flatMap(factory -> {
                final var matcher = factory.pattern().matcher(line);
                if (matcher.matches()) {
                    return Stream.of(factory.lineSupplier.apply(matcher.toMatchResult()));
                } else {
                    return Stream.of();
                }
            })
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not parse line: " + line));
    }

    public interface Node {
        String getName();
        long getSize();
        Stream<Node> depthFirstStream();
    }

    public static class FileNode implements Node {
        private final String name;
        private final long size;

        public FileNode(String name, long size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public Stream<Node> depthFirstStream() {
            return Stream.of(this);
        }
    }
    public static class DirectoryNode implements Node {
        private static final IdentityHashMap<Node, Long> cache = new IdentityHashMap<>();
        private final String name;

        Map<String, Node> children = new HashMap<>();

        public DirectoryNode(String name) {
            this.name = name;
        }

        public void addDirectory(List<String> path, String name) {
            addNode(path.iterator(), new DirectoryNode(name));
        }

        public void addFile(List<String> path, String fileName, long size) {
            addNode(path.iterator(), new FileNode(fileName, size));
        }

        public void addNode(Iterator<String> path, Node node) {
            if (path.hasNext()) {
                if (children.get(path.next()) instanceof DirectoryNode dir) {
                    dir.addNode(path, node);
                } else {
                    throw new RuntimeException("Directory does not exist in path" + node.getName());
                }
            } else {
                if (children.containsKey(node.getName())) {
                    throw new RuntimeException("Traversed same directory twice");
                }
                children.put(node.getName(), node);
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            Long value = cache.get(this);
            if (value != null) {
                return value;
            }
            final var computedValue = children.values().stream().mapToLong(Node::getSize).sum();
            cache.put(this, computedValue);
            return computedValue;
        }

        @Override
        public Stream<Node> depthFirstStream() {
            return Stream.concat(
                Streams.fromIterator(children.values().iterator()).flatMap(Node::depthFirstStream),
                Stream.of(this)
            );
        }
    }
}
