import Foundation

struct File {
    fileprivate let url: URL
    private init(_ path: String) {
        self.url = URL(fileURLWithPath: path)
    }
    
    static let day1 = File("Data/day1_input.txt")
    static let day2 = File("Data/day2_input.txt")
    static let day3 = File("Data/day3_input.txt")
    static let day4 = File("Data/day4_input.txt")
}

extension URL {
    init(file: File) {
        self = file.url
    }
}

extension Data {
    init(file: File) throws {
        self = try Data(contentsOf: URL(file: file))
    }
}
