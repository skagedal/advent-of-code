import Foundation

extension Int {
    var toString: String {
        return "\(self)"
    }
}

func first<T>(_ tuple: (T, T)) -> T {
    return tuple.0
}

func second<T>(_ tuple: (T, T)) -> T {
    return tuple.1
}

func first<T>(_ tuple: (T, T, T)) -> T {
    return tuple.0
}

func second<T>(_ tuple: (T, T, T)) -> T {
    return tuple.1
}

func third<T>(_ tuple: (T, T, T)) -> T {
    return tuple.2
}
