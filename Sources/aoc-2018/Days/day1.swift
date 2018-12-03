import Foundation

private func makeInt(_ string: String) -> Int {
    return Int(string)!
}

func day1() throws {
    let data = try Data(file: .day1)
    
    print("1A: \(day1aValue(data))")
    print("1B: \(day1bValue(data))")
}

func day1aValue(_ data: Data) -> Int {
    return data
        .lines
        .map(makeInt)
        .reduce(0, +)
}

func day1bValue(_ data: Data) -> Int {
    let changes = Array(data.lines.map(makeInt))
    var reached: Set<Int> = []
    var value: Int = 0
    while true {
        for change in changes {
            value += change
            if reached.contains(value) {
                return value
            }
            reached.insert(value)
        }
    }
    fatalError("Never reached the right value")
}
