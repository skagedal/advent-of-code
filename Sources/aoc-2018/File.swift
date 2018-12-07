import Foundation

extension Data {
    init(day: Int) throws {
        let url = URL(fileURLWithPath: "Data/day\(day)_input.txt")
        self = try Data(contentsOf: url)
    }
    
    init(exampleForDay day: Int) throws {
        let url = URL(fileURLWithPath: "Data/day\(day)_example.txt")
        self = try Data(contentsOf: url)
    }
}
