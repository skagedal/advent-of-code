import Foundation

func die(_ string: String) -> Never {
    print(string)
    exit(0)
}

guard let argument = ProcessInfo.processInfo.arguments.dropFirst().first else {
    die("No arguments")
}

switch argument {
case "1a":
    try day1a()
case "1b":
    try day1b()
default:
    die("Unknown argument: \(argument)")
}
