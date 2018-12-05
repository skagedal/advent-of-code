import Foundation

private let a: UInt8 = 97
private let z: UInt8 = 122
private let caseBit: UInt8 = 32

func day5() throws {
    let data = try Data(file: .day5)
    
    print(data.reducingPolymers().count)
    print(data.shortestPolymer())
}

private extension UInt8 {
    func reacts(with other: UInt8) -> Bool {
        return self ^ other == caseBit
    }
}

private extension Sequence where Element == UInt8 {
    func shortestPolymer() -> Int {
        return (a...z).map({
            self.skippingCaseInsensitive(lowerCasedAscii: $0)
                .reducingPolymers()
                .count
        }).min(by: <)!
    }
    
    func reducingPolymers() -> Data {
        return reduce(into: Data()) { result, polymer in
            if let previous = result.last, polymer.reacts(with: previous) {
                result.removeLast()
            } else {
                result.append(polymer)
            }
        }
    }
    
    func skippingCaseInsensitive(lowerCasedAscii: UInt8) -> LazyFilterSequence<Self> {
        return lazy.filter({ $0 | caseBit != lowerCasedAscii })
    }
}
