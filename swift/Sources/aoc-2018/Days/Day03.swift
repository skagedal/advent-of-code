import Foundation

struct Day03: AdventDay2018 {
    let day = 3
    let knownAnswerToFirstPart = "110389"
    let knownAnswerToSecondPart = "552"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        return data
            .fabricClaims
            .countPerTile()
            .values
            .count(where: { $0 > 1 })
            .toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        let claims = data.fabricClaims
        let claimsForTile = claims.countPerTile()
        return claims.first(where: { $0.tiles.allSatisfy({
            claimsForTile[$0] == 1
        })})!.identifier.toString
    }
}

// MARK: - 3A. First solution

/// This is horribly inefficient
func day3aValue_slow(_ data: Data) -> Int {
    let decoder = FabricClaimDecoder()
    let claims = data.lines.map({ try! decoder.decode($0) })
    return Array(claims)
        .trianglePairs()
        .lazy
        .map({ overlappingTiles($0.0, $0.1) })
        .reduce([], union)
        .count
}

// MARK: - Common algorithm for solutions to A & B

extension Sequence where Element == FabricClaim {
    func countPerTile() -> [FabricTile: Int] {
        var claimsForTile: [FabricTile: Int] = [:]
        for claim in self {
            for tile in claim.tiles {
                claimsForTile[tile, default: 0] += 1
            }
        }
        return claimsForTile
    }
}

// MARK: - Data stuctures

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

    var tiles: [FabricTile] {
        return (top..<bottom).flatMap({ y in (left..<right).map({ x in FabricTile(x: x, y: y) }) })
    }
}

extension FabricClaim: CustomStringConvertible {
    var description: String {
        return "decoded #\(identifier) @ \(left),\(top): \(width)x\(height)"
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

// MARK: - Decoding claims

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

private extension Data {
    var fabricClaims: AnySequence<FabricClaim> {
        let decoder = FabricClaimDecoder()
        return AnySequence(nonEmptyLines.map({ try! decoder.decode($0) }))
    }
}
