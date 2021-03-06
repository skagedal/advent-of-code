import Foundation

extension RangeReplaceableCollection where Index == IndexSet.Element {
    func removing(at indices: IndexSet) -> Self {
        var removed = self
        for index in indices.sorted(by: >) {
            removed.remove(at: index)
        }
        return removed
    }
}

extension Collection {
    subscript(safe index: Index) -> Element? {
        guard indices.contains(index) else {
            return nil
        }
        return self[index]
    }

    func subsequences(ofLength length: Int) -> [SubSequence] {
        var index = startIndex
        return Array(count: count - length + 1, createdBy: {
            let result = self[Range(uncheckedBounds: (index, self.index(index, offsetBy: length)))]
            index = self.index(after: index)
            return result
        })
    }
}

func isPalindromic<T>(_ collection: T) -> Bool where T: Collection, T.Element: Equatable {
    return zip(collection, collection.reversed()).allSatisfy(==)
}
