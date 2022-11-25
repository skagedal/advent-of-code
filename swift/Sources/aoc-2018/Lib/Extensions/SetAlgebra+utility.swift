func union<T>(_ a: T, _ b: T) -> T where T: SetAlgebra {
    return a.union(b)
}

func isElementOf<T>(_ set: T) -> ((T.Element) -> Bool) where T: SetAlgebra {
    return { set.contains($0) }
}
