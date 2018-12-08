extension Array {
    init(count: Int, createdBy factory: () -> Element) {
        self = []
        reserveCapacity(count)
        for _ in 1...count {
            append(factory())
        }
    }
}
