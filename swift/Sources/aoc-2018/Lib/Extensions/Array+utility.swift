extension Array {
    init(count: Int, createdBy factory: () -> Element) {
        self = []
        reserveCapacity(count)
        for _ in 0..<count {
            append(factory())
        }
    }
}
