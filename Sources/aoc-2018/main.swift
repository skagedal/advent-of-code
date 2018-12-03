import Foundation

func die(_ string: String) -> Never {
    print(string)
    exit(0)
}

guard let argument = ProcessInfo.processInfo.arguments.dropFirst().first else {
    die("No arguments")
}

switch argument {
case "1": try day1()
case "2": try day2()
default:
    die("Unknown argument: \(argument)")
}
