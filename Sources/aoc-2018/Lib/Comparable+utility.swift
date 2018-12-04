func min<Root, Value>(on keyPath: KeyPath<Root, Value>, _ a: Root, _ b: Root) -> Value where Value: Comparable {
    return min(a[keyPath: keyPath], b[keyPath: keyPath])
}

func max<Root, Value>(on keyPath: KeyPath<Root, Value>, _ a: Root, _ b: Root) -> Value where Value: Comparable {
    return max(a[keyPath: keyPath], b[keyPath: keyPath])
}

