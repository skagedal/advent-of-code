import Foundation

func day5() throws {
    let data = try Data(file: .day5)
    
    let a = data.reducingPolymers().count
    print(a)
}

private extension Data {
    func reducingPolymers() -> Data {
        return reduce(into: Data()) { result, polymer in
            if let previous = result.last, polymer.reacts(with: previous) {
                result.removeLast()
            } else {
                result.append(polymer)
            }
        }
    }
}

private extension UInt8 {
    func reacts(with other: UInt8) -> Bool {
        return self ^ other == 32
    }
}
