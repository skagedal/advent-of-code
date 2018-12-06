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

extension AdventDay {
    func run() throws {
        let data = try Data(day: day)
        let firstAnswer = try answerToFirstPart(data)
        print("\(day)A: \(firstAnswer)")
        
        let secondAnswer = try answerToSecondPart(data)
        print("\(day)B: \(secondAnswer)")
    }
}
guard let argument = ProcessInfo.processInfo.arguments.dropFirst().first else {
    for day in days {
        do {
            try day.run()
        } catch CocoaError.fileReadNoSuchFile {
            // Ignore
        } catch AdventError.unimplemented {
            // Ignore
        }
    }
    exit(0)
}
//
//guard let day = Int(argument) else {
//    die("Not a day argument: \(argument)")
//}
//try runDay(day: day)
