func equals<Value>(_ value: Value) -> ((Value) -> Bool) where Value: Equatable {
    return { value == $0 }
}
