import Foundation

func day3() throws {
    let data = try Data(file: .day3)

    let a = day3aValue(data)
    
    print("3A: \(a)")
}

/// This is horribly inefficient
func day3aValue(_ data: Data) -> Int {
    let decoder = FabricClaimDecoder()
    let claims = data.lines.map({ try! decoder.decode($0) })
    return Array(claims)
        .trianglePairs()
        .lazy
        .map({ overlappingTiles($0.0, $0.1) })
        .reduce([], union)
        .count
}

struct FabricClaim {
    let identifier: Int
    let left: Int
    let top: Int
    let width: Int
    let height: Int
}

extension FabricClaim {
    var right: Int {
        return left + width
    }
    var bottom: Int {
        return top + height
    }
}

extension FabricClaim: CustomStringConvertible {
    var description: String {
        return "decoded #\(identifier) @ \(left),\(top): \(width)x\(height)"
    }
}

class FabricClaimDecoder {
    enum Error: Swift.Error {
        case invalidClaim(String)
    }

    private lazy var regex = try! RegularExpression(pattern: "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")

    func decode(_ string: String) throws -> FabricClaim {
        let matches = regex.matches(in: string)
        guard matches.count == 1 else {
            throw Error.invalidClaim(string)
        }
        let match = matches[0]
        return FabricClaim(identifier: Int(match[1])!,
                           left: Int(match[2])!,
                           top: Int(match[3])!,
                           width: Int(match[4])!,
                           height: Int(match[5])!)
    }
}

struct FabricTile: Hashable, Equatable {
    let x: Int
    let y: Int
}

func overlappingTiles(_ a: FabricClaim, _ b: FabricClaim) -> Set<FabricTile> {
    let left = max(on: \.left, a, b)
    let right = min(on: \.right, a, b)
    let top = max(on: \.top, a, b)
    let bottom = min(on: \.bottom, a, b)
    
    guard left < right, top < bottom else {
        return []
    }
    
    let tiles = (left..<right).flatMap({ x in
        (top..<bottom).map({ y in FabricTile(x: x, y: y) })
    })
    
    return Set(tiles)
}
