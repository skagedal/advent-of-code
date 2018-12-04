extension Dictionary {
    func sorted<T>(byValue keyPath: KeyPath<Value, T>) -> [Element] where T: Comparable {
        return sorted(by: { isLessThan(for: keyPath)($0.value, $1.value) })
    }
    
    func sorted<T>(byValue keyPaths: [KeyPath<Value, T>]) -> [Element] where T: Comparable {
        return sorted(by: { isLessThan(for: keyPaths)($0.value, $1.value) })
    }
    
    func min<T>(byValue keyPath: KeyPath<Value, T>) -> Element? where T: Comparable {
        return self.min(by: { isLessThan(for: keyPath)($0.value, $1.value) })
    }
    
    func min<T>(byValue keyPaths: [KeyPath<Value, T>]) -> Element? where T: Comparable {
        return self.min(by: { isLessThan(for: keyPaths)($0.value, $1.value) })
    }
    
    func max<T>(byValue keyPath: KeyPath<Value, T>) -> Element? where T: Comparable {
        return self.max(by: { isLessThan(for: keyPath)($0.value, $1.value) })
    }
    
    func max<T>(byValue keyPaths: [KeyPath<Value, T>]) -> Element? where T: Comparable {
        return self.max(by: { isLessThan(for: keyPaths)($0.value, $1.value) })
    }
}
