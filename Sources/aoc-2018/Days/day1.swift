import Foundation

private func makeInt(_ string: String) -> Int {
    return Int(string)!
}

func day1a() throws {
    let data = try Data(file: .day1)
    let result = data
        .lines
        .map(makeInt)
        .reduce(0, +)
    
    print(result)
}

func day1b() throws {
    let data = try Data(file: .day1)
    let changes = Array(data.lines.map(makeInt))
    var reached: Set<Int> = []
    var value: Int = 0
    while true {
        for change in changes {
            value += change
            if reached.contains(value) {
                print(value)
                return
            }
            reached.insert(value)
        }
    }
}
