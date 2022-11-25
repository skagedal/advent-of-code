import Foundation

let year = "2015"

let url = URL(fileURLWithPath: "Sources/aoc-2018/Year\(year)")
try? FileManager.default.createDirectory(at: url, withIntermediateDirectories: true)

func createDay(_ day: Int) {
    let dayString = String(format: "%02d", day)
    let swift = """
import Foundation

extension Year\(year) {
    struct Day\(dayString): AdventDay\(year) {
        let day = \(day)

        func answerToFirstPart(_ data: Data) throws -> String {
            throw AdventError.unimplemented
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            throw AdventError.unimplemented
        }
    }
}

""".data(using: .utf8)!

    let fileURL = url.appendingPathComponent("Year\(year)-Day\(dayString).swift")
    try? swift.write(to: fileURL)
}

for day in 1...25 {
    createDay(day)
}
