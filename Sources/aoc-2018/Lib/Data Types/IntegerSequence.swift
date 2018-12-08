import Foundation

struct IntegerDataSequence: Sequence {
    typealias Element = Int

    struct Iterator: IteratorProtocol {
        mutating func next() -> Int? {
            guard let found = skipNonIntegers() else {
                return nil
            }
            return parseInteger(start: found.start, sign: found.sign)
        }
        
        private mutating func skipNonIntegers() -> (start: Int, sign: Int)? {
            var sign: Int = 1
            while let c = dataIterator.next() {
                if c == ASCII.hyphenMinus {
                    sign = -1
                } else if let int = ASCII.integer(c) {
                    return (start: int, sign: sign)
                } else {
                    sign = 1
                }
            }
            return nil
        }
        
        private mutating func parseInteger(start: Int, sign: Int) -> Int {
            var result = start
            while let c = dataIterator.next(), let int = ASCII.integer(c) {
                result = result * 10 + int
            }
            return result * sign
        }
        
        var dataIterator: Data.Iterator
        
        init(_ sequence: IntegerDataSequence) {
            dataIterator = sequence.data.makeIterator()
        }
    }

    private let data: Data

    init(_ data: Data) {
        self.data = data
    }
    
    func makeIterator() -> IntegerDataSequence.Iterator {
        return Iterator(self)
    }
}

extension Data {
    var integers: IntegerDataSequence {
        return IntegerDataSequence(self)
    }
}
