import Foundation

enum Year {
    case year2015
    case year2018

    var year: Int {
        switch self {
        case .year2015: return 2015
        case .year2018: return 2018
        }
    }
}

protocol AdventDay {
    var day: Int { get }
    var year: Year { get }
    
    func answerToFirstPart(_ data: Data) throws -> String
    func answerToSecondPart(_ data: Data) throws -> String
    
    var knownAnswerToFirstPart: String { get }
    var knownAnswerToSecondPart: String { get }
    var knownAnswerToExampleForFirstPart: String { get }
    var knownAnswerToExampleForSecondPart: String { get }
    
    func answerToExampleForFirstPart(_ data: Data) throws -> String
    func answerToExampleForSecondPart(_ data: Data) throws -> String
}

extension AdventDay {
    var year: Year {
        return .year2018
    }
}

enum Year2015 { }
enum Year2018 { }

protocol AdventDay2015: AdventDay { }
protocol AdventDay2018: AdventDay { }

extension AdventDay2015 { var year: Year { return .year2015 }}
extension AdventDay2018 { var year: Year { return .year2018 }}

enum Answer {
    static let unknown = "❔"
}

extension AdventDay {
    func answerToExampleForFirstPart(_ data: Data) throws -> String {
        return try answerToFirstPart(data)
    }
    
    func answerToExampleForSecondPart(_ data: Data) throws -> String {
        return try answerToSecondPart(data)
    }

    // We use this sentinel value Answer.unknown here rather than having an optional since
    // it makes it nicer to conform to the protocol by just adding a let foo = "asdf"
    
    var knownAnswerToFirstPart: String {
        return Answer.unknown
    }
    var knownAnswerToSecondPart: String {
        return Answer.unknown
    }
    var knownAnswerToExampleForFirstPart: String {
        return Answer.unknown
    }
    var knownAnswerToExampleForSecondPart: String {
        return Answer.unknown
    }
}

enum AdventError: Swift.Error {
    case unimplemented
    case noAnswerFound
}
