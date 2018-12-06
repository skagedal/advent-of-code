import Foundation

extension Data {
    init(day: Int) throws {
        let url = URL(fileURLWithPath: "Data/day\(day)_input.txt")
        self = try Data(contentsOf: url)
    }
}
