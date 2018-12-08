struct GroupingSequence<SingleElement>: Sequence {
    struct Iterator: IteratorProtocol {
        mutating func next() -> [SingleElement]? {
            if collection.count >= count {
                let result = collection.prefix(count)
                collection = collection.dropFirst(count)
                return Array(result)
            }
            return nil
        }
        
        private let count: Int
        private var collection: AnyCollection<SingleElement>
        
        init(_ groupingSequence: GroupingSequence<SingleElement>) {
            self.count = groupingSequence.count
            self.collection = groupingSequence.collection
        }
    }
    
    private let count: Int
    private let collection: AnyCollection<SingleElement>
    
    init<T>(count: Int, _ collection: T) where T: Collection, T.Element == SingleElement {
        self.count = count
        self.collection = AnyCollection(collection)
    }
    
    func makeIterator() -> GroupingSequence<SingleElement>.Iterator {
        return Iterator(self)
    }
}

extension Collection {
    func grouping(count: Int) -> GroupingSequence<Element> {
        return GroupingSequence(count: count, self)
    }
}
