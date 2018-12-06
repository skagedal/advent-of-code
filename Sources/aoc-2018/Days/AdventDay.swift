import Foundation

protocol AdventDay {
    var day: Int { get }
    func answerToFirstPart(_ data: Data) throws -> String
    func answerToSecondPart(_ data: Data) throws -> String
}

enum AdventError: Swift.Error {
    case unimplemented
}
