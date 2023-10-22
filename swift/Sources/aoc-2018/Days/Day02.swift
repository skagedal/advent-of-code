import Foundation

struct Day02: AdventDay2018 {
    let day = 2
    let knownAnswerToFirstPart = "5704"
    let knownAnswerToSecondPart = "umdryabviapkozistwcnihjqx"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        return data
            .lines
            .map({ $0.boxIDCheck() })
            .reduce(.zero, +)
            .checkSum()
            .toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        return Array(data.lines)
            .trianglePairs()
            .first(where: { simpleDiffSum($0.0, $0.1) == 1 })
            .map({ commonLetters($0.0, $0.1) })!
    }
    
    func alternativeAnswerToSecondPart(_ data: Data) throws -> String {
        var previous: Set<String> = []
        for line in data.lines {
            for previousLine in previous {
                if simpleDiffSum(line, previousLine) == 1 {
                    return commonLetters(line, previousLine)
                }
            }
            previous.insert(line)
        }
        fatalError("No solution to day 2B")
    }
}

private struct BoxIDCheck {
    let twos: Int
    let threes: Int
}

private extension BoxIDCheck {
    static func +(_ lhs: BoxIDCheck, _ rhs: BoxIDCheck) -> BoxIDCheck {
        return BoxIDCheck(twos: lhs.twos + rhs.twos,
                          threes: lhs.threes + rhs.threes)
    }
    
    static let zero = BoxIDCheck(twos: 0, threes: 0)
    
    func checkSum() -> Int {
        return twos * threes
    }
}

private extension Sequence where Element: Hashable {
    func boxIDCheck() -> BoxIDCheck {
        let counts = countsForElements()
        return BoxIDCheck(twos: counts[2] != nil ? 1 : 0,
                          threes: counts[3] != nil ? 1 : 0)
    }
}

private func commonLetters(_ a: String, _ b: String) -> String {
    return String(Array(a).removing(at: diffingIndices(a, b)))
}
