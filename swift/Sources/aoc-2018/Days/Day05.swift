import Foundation

private let a: UInt8 = 97
private let z: UInt8 = 122
private let caseBit: UInt8 = 32

import Foundation

struct Day05: AdventDay2018 {
    let day = 5
    let knownAnswerToExampleForFirstPart = "10"
    let knownAnswerToFirstPart = "9704"
    let knownAnswerToExampleForSecondPart = "4"
    let knownAnswerToSecondPart = "6942"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        return data
            .firstLineAsData
            .reducingPolymers()
            .count
            .toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        return data
            .firstLineAsData
            .shortestPolymer()
            .toString
    }
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
