import Foundation

struct Day01: AdventDay2018 {
    let day = 1
    let knownAnswerToFirstPart = "553"
    let knownAnswerToSecondPart = "78724"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        return data
            .nonEmptyLines
            .map(makeInt)
            .reduce(0, +)
            .toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        let changes = Array(data.nonEmptyLines.map(makeInt))
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
