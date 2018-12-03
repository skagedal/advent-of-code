import Foundation

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

func day2() throws {
    print(day2aValue(try Data(file: .day2)))
}

func day2aValue(_ data: Data) -> Int {
    return data
        .lines
        .map({ $0.boxIDCheck() })
        .reduce(.zero, +)
        .checkSum()
}

