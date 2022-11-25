prefix operator ^

/// This operator transforms a key path to a closure.  This enables
/// you to write things like `addresses.map(^\.zipCode)` to get an
/// array of zip codes.
prefix func ^ <Root, Value> (keyPath: KeyPath<Root, Value>) -> (Root) -> Value {
    return { $0[keyPath: keyPath] }
}

func isLessThan<Element, Value>(for keyPath: KeyPath<Element, Value>) -> ((Element, Element) -> Bool) where Value: Comparable {
    return { $0[keyPath: keyPath] < $1[keyPath: keyPath] }
}

func isLessThan<Element, Value>(for keyPaths: [KeyPath<Element, Value>]) -> ((Element, Element) -> Bool) where Value: Comparable {
    return { lhs, rhs in
        for keyPath in keyPaths {
            let a = lhs[keyPath: keyPath]
            let b = rhs[keyPath: keyPath]
            if a != b {
                return a < b
            }
        }
        return false
    }
}

