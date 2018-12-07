import Foundation

func die(_ string: String) -> Never {
    print(string)
    exit(0)
}

let days: [AdventDay] = [
    Day01(), Day02(), Day03(), Day04(), Day05(), Day06(), Day07(), Day08(), Day09(), Day10(),
    Day11(), Day12(), Day13(), Day14(), Day15(), Day16(), Day17(), Day18(), Day19(), Day20(),
    Day21(), Day22(), Day23(), Day24(), Day25()
]

enum Part: String {
    case first = "A"
    case second = "B"
}

extension AdventDay {
    func run(_ part: Part) throws {
        let answer = try answerToPart(part, data: try Data(day: day))
        print("\(day)\(part.rawValue): \(answer)")
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
}
guard let argument = ProcessInfo.processInfo.arguments.dropFirst().first else {
    for day in days {
        do {
            try day.runBoth()
        } catch CocoaError.fileReadNoSuchFile {
            // Ignore
        } catch AdventError.unimplemented {
            // Ignore
        }
        print()
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
