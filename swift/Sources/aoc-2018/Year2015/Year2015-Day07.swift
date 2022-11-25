import Foundation

extension Year2015 {
    struct Day07: AdventDay2015 {
        let day = 7

        func answerToFirstPart(_ data: Data) throws -> String {
            let statements = data.lines.map(parseStatement)
            let evaluator = WireEvaluator(Array(statements))
            return String(evaluator.evaluate("a"))
        }
    
        func answerToExampleForFirstPart(_ data: Data) throws -> String {
            let statements = data.lines.map(parseStatement)
            let evaluator = WireEvaluator(Array(statements))
            return String(evaluator.evaluate("h"))
        }
        
        func answerToSecondPart(_ data: Data) throws -> String {
            let statements = data.lines.map(parseStatement).filter({ $0.0 != "b" })
            let newStatement = ("b", Instruction.signal(.wire("a")))
            let evaluator = WireEvaluator(Array(statements) + [newStatement])
            return String(evaluator.evaluate("a"))
        }
        
        let knownAnswerToExampleForFirstPart = "65412"
        let knownAnswerToFirstPart = "3176"
    }
}

// MARK: - Evaluator

private class WireEvaluator {
    private let instructions: Dictionary<Wire, Instruction>
    private var cache: [Wire: WireValue] = [:]

    init(_ statements: [(Wire, Instruction)]) {
        instructions = Dictionary(uniqueKeysWithValues: statements)
    }
    
    func evaluate(_ wire: Wire) -> WireValue {
        if let cached = cache[wire] {
            return cached
        }
        let value = evaluate_(wire)
        cache[wire] = value
        return value
    }
    
    private func evaluate_(_ wire: Wire) -> WireValue {
        guard let instruction = instructions[wire] else {
            fatalError("No instruction for wire \(wire)")
        }
        switch instruction {
        case let .signal(expression):
            return evaluateExpression(expression)
        case let .and(a, b):
            return evaluateExpression(a) & evaluateExpression(b)
        case let .or(a, b):
            return evaluateExpression(a) | evaluateExpression(b)
        case let .leftShift(a, b):
            return evaluateExpression(a) << b
        case let .rightShift(a, b):
            return evaluateExpression(a) >> b
        case let .not(w):
            return ~evaluateExpression(w)
        }
    }

    private func evaluateExpression(_ expression: Expression) -> WireValue {
        switch expression {
        case let .value(value):
            return value
        case let .wire(wire):
            return evaluate(wire)
        }
    }
}

// MARK: - Parser

private typealias WireValue = UInt16
private typealias Wire = String

private enum Expression {
    case wire(Wire)
    case value(WireValue)
}

private enum Instruction {
    case signal(Expression)
    case not(Expression)
    case and(Expression, Expression)
    case or(Expression, Expression)
    case leftShift(Expression, Int)
    case rightShift(Expression, Int)
}

private let WIRE = "([a-z]+)"
private let VALUE = "(\\d+)"

private let wireRegex = regex("^[a-z]+$")
private let valueRegex = regex("^\\d+$")

private func parseStatement(_ string: String) -> (Wire, Instruction) {
    let components = string.split(separator: " ").map(String.init)
    switch components.count {
    case 3:
        let expression = parseExpression(components[0])
        parseArrow(components[1])
        let wire = parseWire(components[2])
        return (wire, .signal(expression))
    case 4:
        parseToken(components[0], expected: "NOT")
        let expression = parseExpression(components[1])
        parseArrow(components[2])
        let wire = parseWire(components[3])
        return (wire, .not(expression))
    case 5:
        let instruction: Instruction = {
            let a = parseExpression(components[0])
            switch components[1] {
            case "AND":
                let b = parseExpression(components[2])
                return .and(a, b)
            case "OR":
                let b = parseExpression(components[2])
                return .or(a, b)
            case "LSHIFT":
                let b = parseInt(components[2])
                return .leftShift(a, b)
            case "RSHIFT":
                let b = parseInt(components[2])
                return .rightShift(a, b)
            default:
                preconditionFailure("Unknown operator: \(components[1])")
            }
        }()
        parseArrow(components[3])
        let wire = parseWire(components[4])
        return (wire, instruction)
    default:
        preconditionFailure("Uknown instruction: \(string)")
    }
}

private func parseExpression(_ string: String) -> Expression {
    if let match = wireRegex.firstMatch(in: string) {
        return .wire(match[0])
    }
    if let match = valueRegex.firstMatch(in: string) {
        return .value(match[0].wireValue)
    }
    preconditionFailure("Expected expression, got: \(string)")
}

private func parseWire(_ string: String) -> Wire {
    guard let match = wireRegex.firstMatch(in: string) else {
        preconditionFailure("Expected wire, got: \(string)")
    }
    return match[0]
}

private func parseArrow(_ string: String) {
    parseToken(string, expected: "->")
}

private func parseToken(_ string: String, expected: String) {
    precondition(string == expected, "Expected \(expected), got \(string)")
}

private func parseInt(_ string: String) -> Int {
    guard let int = Int(string) else {
        preconditionFailure("Expected integer, got \(string)")
    }
    return int
}

private extension String {
    var wireValue: WireValue {
        return UInt16(self)!
    }
}

