import Foundation

/// Represents a sequence of Data split at a certain value. Skips the last item if it is empty.
struct DataSplitSequence: Sequence {
    typealias Element = Data
    
    struct Iterator: IteratorProtocol {
        private let sequence: DataSplitSequence
        private var index: Int = -1
        
        init(_ sequence: DataSplitSequence) {
            self.sequence = sequence
        }
        
        typealias Element = Data
        
        public mutating func next() -> Data? {
            guard index < sequence.data.count else {
                return nil
            }
            let startIndex = index + 1
            repeat {
                index += 1
            } while index < sequence.data.count && sequence.data[index] != sequence.separator
            if (startIndex == sequence.data.count) {
                return nil
            }
            return sequence.data[startIndex..<index]
        }
    }
    
    private let data: Data
    private let separator: UInt8
    
    init(data: Data, separator: UInt8) {
        self.data = data
        self.separator = separator
    }
    
    func makeIterator() -> DataSplitSequence.Iterator {
        return Iterator(self)
    }
}

extension Data {
    func splitSequence(separator: UInt8) -> DataSplitSequence {
        return DataSplitSequence(data: self, separator: separator)
    }
    
    var lines: LazyMapSequence<DataSplitSequence, String> {
        return splitSequence(separator: ASCII.lineFeed).lazy.map { String(data: $0, encoding: .utf8)! }
    }
    
    var firstLineAsData: Data {
        return lines.first(where: { _ in true })!.data(using: .utf8)!
    }
}
