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
