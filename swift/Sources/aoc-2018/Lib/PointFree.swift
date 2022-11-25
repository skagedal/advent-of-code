// Various unsorted point-free helpers

func identity<T>(_ t: T) -> T {
    return t
}

func flip<T, U>(_ tuple: (T, U)) -> (U, T) {
    return (tuple.1, tuple.0)
}
