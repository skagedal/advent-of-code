import Foundation

func die(_ string: String) -> Never {
    print(string)
    exit(0)
}

let year: Year

var arguments = ProcessInfo.processInfo.arguments.dropFirst()
if arguments.contains(where: equals("--2015")) {
    year = .year2015
    arguments.removeAll(where: equals("--2015"))
} else {
    year = .year2018
}

let allDays: [Year: [AdventDay]] = [
    .year2015: [
        Year2015.Day01(), Year2015.Day02(), Year2015.Day03(), Year2015.Day04(), Year2015.Day05(),
        Year2015.Day06(), Year2015.Day07(), Year2015.Day08(), Year2015.Day09(), Year2015.Day10(),
        Year2015.Day11(), Year2015.Day12(), Year2015.Day13(), Year2015.Day14(), Year2015.Day15(),
        Year2015.Day16(), Year2015.Day17(), Year2015.Day18(), Year2015.Day19(), Year2015.Day20(),
        Year2015.Day21(), Year2015.Day22(), Year2015.Day23(), Year2015.Day24(), Year2015.Day25()
    ],
    .year2018: [
        Day01(), Day02(), Day03(), Day04(), Day05(), Day06(), Day07(), Day08(), Day09(), Day10(),
        Day11(), Day12(), Day13(), Day14(), Day15(), Day16(), Day17(), Day18(), Day19(), Day20(),
        Day21(), Day22(), Day23(), Day24(), Day25()
    ]
]

let days = allDays[year]!

enum Part: String {
    case first = "A"
    case second = "B"
}

func timed<T>(_ action: (() throws -> T)) rethrows -> (TimeInterval, T) {
    let start = DispatchTime.now()
    let answer = try action()
    let end = DispatchTime.now()
    let nanoTime = end.uptimeNanoseconds - start.uptimeNanoseconds
    let timeInterval = TimeInterval(nanoTime) / 1_000_000_000
    return (timeInterval, answer)
    
}

extension AdventDay {
    func data() throws -> Data {
        let url = FileManager.default.homeDirectoryForCurrentUser
            .appending(components: ".aoc", "data", "\(year)", String(format: "day%02d_input.txt", day))
        return try Data(contentsOf: url)
    }
    
    func exampleData() throws -> Data {
        let url = FileManager.default.homeDirectoryForCurrentUser
            .appending(components: ".aoc", "data", "\(year)", String(format: "day%02d_example.txt", day))
        return try Data(contentsOf: url)
    }
    
    func run(_ part: Part) throws {
        do {
            if let exampleData = try? exampleData() {
                let (time, answer) = try timed({ try answerToExampleForPart(part, data: exampleData) })
                printAndCheckDiff(part: part, isExample: true, answer: answer, knownAnswer: knownAnswer(toExampleFor: part), time: time)
            }
            let (time, answer) = try timed({ try answerToPart(part, data: try data()) })
            printAndCheckDiff(part: part, isExample: false, answer: answer, knownAnswer: knownAnswer(to: part), time: time)
        } catch AdventError.unimplemented {
            printNotImplemented(part: part)
        }
    }
    
    func runBoth() throws {
        try run(.first)
        try run(.second)
    }
    
    func answerToPart(_ part: Part, data: Data) throws -> String {
        switch part {
        case .first:
            return try answerToFirstPart(data)
        case .second:
            return try answerToSecondPart(data)
        }
    }
    
    func answerToExampleForPart(_ part: Part, data: Data) throws -> String {
        switch part {
        case .first:
            return try answerToExampleForFirstPart(data)
        case .second:
            return try answerToExampleForSecondPart(data)
        }
    }

    func knownAnswer(to part: Part) -> String {
        switch part {
        case .first:
            return knownAnswerToFirstPart
        case .second:
            return knownAnswerToSecondPart
        }
    }
    
    func knownAnswer(toExampleFor part: Part) -> String {
        switch part {
        case .first:
            return knownAnswerToExampleForFirstPart
        case .second:
            return knownAnswerToExampleForSecondPart
        }
    }
    
    private func printAndCheckDiff(part: Part, isExample: Bool, answer: String, knownAnswer: String, time: TimeInterval) {
        let example = isExample ? " (example)" : ""
        let prefix: String
        let moreInfo: String?
        if knownAnswer != Answer.unknown {
            if knownAnswer == answer {
                prefix = "✅ "
                moreInfo = nil
            } else {
                prefix = "🛑 "
                moreInfo = "       Expected \(knownAnswer), got \(answer)"
            }
        } else {
            prefix = "   "
            moreInfo = nil
        }
        let formattedTime = String(format: "%.3f s", time)
        print("\(prefix)\(year.year)-\(day)\(part.rawValue): \(answer)\(example) [\(formattedTime)]")
        if let moreInfo = moreInfo {
            print(moreInfo)
        }
    }
    
    private func printNotImplemented(part: Part) {
        print("   \(day)\(part.rawValue) - not implemented")
    }
}


guard let argument = arguments.first else {
    for day in days {
        do {
            try day.runBoth()
            print()
        } catch CocoaError.fileReadNoSuchFile {
            print("No such file")
        } catch AdventError.unimplemented {
            // Ignore
        }
    }
    exit(0)
}

func runDay(dayNumber: Int, part: Part? = nil) throws {
    let day = days.first(where: { $0.day == dayNumber })!
    if let part = part {
        try day.run(part)
    } else {
        try day.runBoth()
    }
}

let regex = try RegularExpression(pattern: "^(\\d+)([ABab])?$")
guard let match = regex.firstMatch(in: argument) else {
    die("Not a day argument: \(argument)")
}

let ranges = match.ranges()
let dayNumber = Int(ranges[1])!
let part = ranges[safe: 2].map({ Part(rawValue: $0)! })
try runDay(dayNumber: dayNumber, part: part)
