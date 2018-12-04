import Foundation

/// This should soon be in Swift stdlib, taken from commit:
/// https://github.com/apple/swift/pull/16099/commits/25b2fd8f4c1691609e2cb486dfde6371860f1590
extension Sequence {
    @inlinable
    public func count(where predicate: (Element) throws -> Bool) rethrows -> Int {
        var count = 0
        for e in self {
            if try predicate(e) {
                count += 1
            }
        }
        return count
    }
}

extension Sequence where Element: Equatable {
    func countingOccurrences(of element: Element) -> Int {
        return reduce(0, { $0 + ($1 == element ? 1 : 0) })
    }
}

extension Sequence where Element: Hashable {
    func allElementCounts() -> [Element: Int] {
        return Dictionary(uniqueKeysWithValues: Set(self).map { element in
            (element, countingOccurrences(of: element))
        })
    }
    
    func countsForElements() -> [Int: [Element]] {
        return Dictionary(grouping: Set(self), by: { element in
            countingOccurrences(of: element)
        })
    }
}

// MARK: - Diffing

/// Calculates the number of places `sequenceA` and `sequenceB` where the elements are different,
/// i.e. not equal to each other.  If one sequence is shorter than the other, it will only look
/// at as many elements as are in the shorter sequence.
///
/// Complexity: O(n)
func simpleDiffSum<T>(_ sequenceA: T, _ sequenceB: T) -> Int where T: Sequence, T.Element: Equatable {
    return zip(sequenceA, sequenceB)
        .map({ $0.0 == $0.1 ? 0 : 1 })
        .reduce(0, +)
}

func diffingIndices<T>(_ sequenceA: T, _ sequenceB: T) -> IndexSet where T: Sequence, T.Element: Equatable {
    let indices = zip(sequenceA, sequenceB)
        .enumerated()
        .compactMap({ enumeratedElements -> Int? in
            let (index, elements) = enumeratedElements
            let (a, b) = elements
            return a == b ? nil : index
        })
    return IndexSet(indices)
}
