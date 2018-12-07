/// Pairs every element with every other element in a sequence,
/// but only once.  So e.g. TrianglePairedSequence(0...3) will
/// generate
/// (0, 1), (0, 2), (0, 3), (1, 2), (1, 3), (2, 3)
struct TrianglePairedSequence<SingleElement>: Sequence {
    struct Iterator: IteratorProtocol {
        mutating func next() -> (SingleElement, SingleElement)? {
            guard let first = collection[safe: firstIndex] else {
                return nil
            }
            secondIndex = collection.index(after: secondIndex)
            guard let second = collection[safe: secondIndex] else {
                firstIndex = collection.index(after: firstIndex)
                secondIndex = firstIndex
                return next()
            }
            
            return (first, second)
        }
        
        let collection: AnyCollection<SingleElement>
        var firstIndex: AnyCollection<SingleElement>.Index
        var secondIndex: AnyCollection<SingleElement>.Index
        
        init(_ collection: AnyCollection<SingleElement>) {
            self.collection = collection
            firstIndex = collection.startIndex
            secondIndex = firstIndex
        }
    }
    
    let collection: AnyCollection<SingleElement>
    
    /// The base type has to be a collection because a Sequence is not multi-pass.
    init<T>(_ collection: T) where T: Collection, T.Element == SingleElement {
        self.collection = AnyCollection(collection)
    }
    
    func makeIterator() -> TrianglePairedSequence<SingleElement>.Iterator {
        return Iterator(collection)
    }
}

extension Collection {
    func trianglePairs() -> TrianglePairedSequence<Element> {
        return TrianglePairedSequence(self)
    }
}
