import Foundation

func day1() throws {
    let data = try Data(file: .day1)
    
    let correctA = 416
    let correctB = 56752
    
    let a = day1aValue(data)
    let b = day1bValue(data)
    
    assert(a == correctA)
    assert(b == correctB)
    
    print("1A: \(a)")
    print("1B: \(b)")
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

private func makeInt(_ string: String) -> Int {
    return Int(string)!
}
