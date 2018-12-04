import Foundation

func die(_ string: String) -> Never {
    print(string)
    exit(0)
}

func runDay(day: Int) throws {
    switch day {
    case 1: try day1()
    case 2: try day2()
    case 3: try day3()
    case 4: try day4()
    default:
        die("Unknown day: \(day)")
    }
}

guard let argument = ProcessInfo.processInfo.arguments.dropFirst().first else {
    for day in 1...4 {
        try runDay(day: day)
    }
    exit(0)
}

guard let day = Int(argument) else {
    die("Not a day argument: \(argument)")
}
try runDay(day: day)
