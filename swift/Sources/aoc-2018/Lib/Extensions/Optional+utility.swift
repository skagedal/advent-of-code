extension Optional where Wrapped: Collection {
    var isEmptyOrNil: Bool {
        switch self {
        case .none:
            return true
        case .some(let wrapped):
            return wrapped.isEmpty
        }
    }
}
