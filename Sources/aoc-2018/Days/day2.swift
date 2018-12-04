import Foundation

func day2() throws {
    let data = try Data(file: .day2)
    
    let correctA = 7688
    let correctB = "lsrivmotzbdxpkxnaqmuwcchj"

    let a = day2aValue(data)
    let b = day2bValue(data)
    let b2 = day2bValue_alt2(data)
    
    assert(a == correctA)
    assert(b == correctB)
    
    print("2A: \(a)")
    print("2B: \(b)")
    print("    \(b2)")
}


struct BoxIDCheck {
    let twos: Int
    let threes: Int
}

extension BoxIDCheck {
    static func +(_ lhs: BoxIDCheck, _ rhs: BoxIDCheck) -> BoxIDCheck {
        return BoxIDCheck(twos: lhs.twos + rhs.twos,
                          threes: lhs.threes + rhs.threes)
    }
    
    static let zero = BoxIDCheck(twos: 0, threes: 0)
    
    func checkSum() -> Int {
        return twos * threes
    }
}

extension Sequence where Element: Hashable {
    func boxIDCheck() -> BoxIDCheck {
        let counts = countsForElements()
        return BoxIDCheck(twos: counts[2] != nil ? 1 : 0,
                          threes: counts[3] != nil ? 1 : 0)
    }
}

func day2aValue(_ data: Data) -> Int {
    return data
        .lines
        .map({ $0.boxIDCheck() })
        .reduce(.zero, +)
        .checkSum()
}

func day2bValue(_ data: Data) -> String {
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

func day2bValue_alt2(_ data: Data) -> String{
    return Array(data.lines)
        .trianglePairs()
        .first(where: { simpleDiffSum($0.0, $0.1) == 1 })
        .map({ commonLetters($0.0, $0.1) })!
}

private func commonLetters(_ a: String, _ b: String) -> String {
    return String(Array(a).removing(at: diffingIndices(a, b)))
}
