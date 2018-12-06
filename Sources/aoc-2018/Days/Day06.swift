import Foundation

struct Day06: AdventDay {
    let day = 6
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let coordinates = Array(data.lines.enumerated().map({ id, line in InputCoordinate(line, identifier: id) }))
        let left = coordinates.min(by: \.x)!.x
        let right = coordinates.max(by: \.x)!.x
        let top = coordinates.min(by: \.y)!.y
        let bottom = coordinates.max(by: \.y)!.y
        
        var positions: [[Position]] = Array(repeating: Array(repeating: .unknown, count: right + 1), count: bottom + 1)

        for coordinate in coordinates {
            positions[coordinate.y][coordinate.x] = .fixed(coordinate.identifier)
        }
        
        for y in top...bottom {
            for x in left...right {
                if positions[y][x] == .unknown {
                    let distances = coordinates.map({ (coordinate: $0, distance: $0.manhattanDistance(x: x, y: y))}).sorted(by: { $0.distance < $1.distance })
                    if distances.count > 1, distances[0].distance != distances[1].distance {
                        positions[y][x] = .closest(distances[0].coordinate.identifier)
                    }
                }
            }
        }
        
        var ignoredIdentifiers: Set<PlaceIdentifier> = []
        for y in top...bottom {
            if let identifier = positions[y][left].identifier {
                ignoredIdentifiers.insert(identifier)
            }
            if let identifier = positions[y][right].identifier {
                ignoredIdentifiers.insert(identifier)
            }
        }
        for x in left...right {
            if let identifier = positions[top][x].identifier {
                ignoredIdentifiers.insert(identifier)
            }
            if let identifier = positions[bottom][x].identifier {
                ignoredIdentifiers.insert(identifier)
            }
        }

        var size: [PlaceIdentifier: Int] = [:]
        for y in top...bottom {
            for x in left...right {
                if let identifier = positions[y][x].identifier, !ignoredIdentifiers.contains(identifier) {
                    size[identifier, default: 0] += 1
                }
            }
        }
        
        return "\(size.values.max()!)"

    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        let coordinates = Array(data.lines.enumerated().map({ id, line in InputCoordinate(line, identifier: id) }))
        let left = coordinates.min(by: \.x)!.x
        let right = coordinates.max(by: \.x)!.x
        let top = coordinates.min(by: \.y)!.y
        let bottom = coordinates.max(by: \.y)!.y
        let size = (top...bottom).map({ y in
            (left...right).count(where: { x in
                coordinates.map({ $0.manhattanDistance(x: x, y: y)}).sum() < 10000
            })
        }).sum()
        return "\(size)"
    }
}

extension Sequence where Element == Int {
    func sum() -> Int {
        return reduce(0, +)
    }
}

enum Position: Equatable {
    case unknown
    case fixed(PlaceIdentifier)
    case closest(PlaceIdentifier)
    case tie
    
    var identifier: PlaceIdentifier? {
        switch self {
        case .fixed(let id), .closest(let id):
            return id
        default:
            return nil
            
        }
    }
}

typealias PlaceIdentifier = Int

struct InputCoordinate: CustomStringConvertible {
    var description: String {
        return "(\(x), \(y))"
    }
    
    let x: Int
    let y: Int
    let identifier: Int
    
    init(_ string: String, identifier: Int) {
        let split = string.split(separator: ",")
        x = Int(split[0])!
        y = Int(split[1].dropFirst())!
        self.identifier = identifier
    }
    
    func manhattanDistance(x: Int, y: Int) -> Int {
        return abs(x - self.x) + abs(y - self.y)
    }
}
