import Foundation

extension Year2015 {
    struct Day03: AdventDay2015 {
        let day = 3

        func answerToFirstPart(_ data: Data) throws -> String {
            var current: Location = .zero
            var visited: [Location: Int] = [current: 1]
            for instruction in data {
                current = move(instruction, from: current)
                visited[current, default: 0] += 1
            }
            return visited.keys.count.toString
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            throw AdventError.unimplemented
        }
        
        let knownAnswerToFirstPart = "2592"
    }
}

private struct Location: Hashable {
    let x: Int
    let y: Int

    static let zero = Location(x: 0, y: 0)
}

private func move(_ instruction: UInt8, from start: Location) -> Location {
    switch instruction {
    case ASCII.circumflexAccent:
        return Location(x: start.x, y: start.y - 1)
    case ASCII.v:
        return Location(x: start.x, y: start.y + 1)
    case ASCII.lessThanSign:
        return Location(x: start.x - 1, y: start.y)
    case ASCII.greaterThanSign:
        return Location(x: start.x + 1, y: start.y)
    default:
        fatalError("Unknown move instruction: \(instruction)")
    }
}
