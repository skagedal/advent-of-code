func equals<Value>(_ value: Value) -> ((Value) -> Bool) where Value: Equatable {
    return { value == $0 }
}

func isRepeated<T>(_ collection: T) -> Bool where T: Collection, T.Element: Equatable {
    guard let first = collection.first else {
        return false
    }
    return collection.dropFirst().allSatisfy(equals(first))
}

