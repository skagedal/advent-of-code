import Foundation

struct Day01: AdventDay {
    let day = 1
    
    /// 416
    func answerToFirstPart(_ data: Data) throws -> String {
        return data
            .lines
            .map(makeInt)
            .reduce(0, +)
            .toString
    }
    
    /// 56752
    func answerToSecondPart(_ data: Data) throws -> String {
        let changes = Array(data.lines.map(makeInt))
        var reached: Set<Int> = []
        var value: Int = 0
        while true {
            for change in changes {
                value += change
                if reached.contains(value) {
                    return value.toString
                }
                reached.insert(value)
            }
        }
        fatalError("Never reached the right value")
    }
}

private func makeInt(_ string: String) -> Int {
    return Int(string)!
}
