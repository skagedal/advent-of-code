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
