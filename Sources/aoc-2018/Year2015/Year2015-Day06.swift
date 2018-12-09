import Foundation

extension Year2015 {
    struct Day06: AdventDay2015 {
        let day = 6

        func answerToFirstPart(_ data: Data) throws -> String {
            return answer(data, lights: Bitmap(width: 1000, height: 1000))
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            return answer(data, lights: CrazyPixmap(width: 1000, height: 1000))
        }
        
        private func answer(_ data: Data, lights inputLights: Lights) -> String {
            var lights = inputLights
            for line in data.splitSequence(separator: ASCII.lineFeed) {
                let rect = Rect(integers: line.integers)
                switch line.toString {
                case turnOnRegex:
                    lights.turnOnAll(in: rect)
                case turnOffRegex:
                    lights.turnOffAll(in: rect)
                case toggleRegex:
                    lights.toggleAll(in: rect)
                default:
                    fatalError("Unknown instruction: \(line.toString)")
                }
            }
            return lights.countLit.toString
        }
        
        let knownAnswerToFirstPart = "377891"
        let knownAnswerToSecondPart = "14110788"
    }
}

private let turnOnRegex = try! RegularExpression(pattern: "^turn on")
private let turnOffRegex = try! RegularExpression(pattern: "^turn off")
private let toggleRegex = try! RegularExpression(pattern: "^toggle")

private struct Rect {
    let x1: Int
    let y1: Int
    let x2: Int
    let y2: Int
    
    init<T>(integers: T) where T: Sequence, T.Element == Int {
        (x1, y1, x2, y2) = integers.firstFour
    }
}

private protocol Lights {
    var countLit: Int { get }
    mutating func turnOnAll(in rect: Rect)
    mutating func turnOffAll(in rect: Rect)
    mutating func toggleAll(in rect: Rect)
}

private struct Bitmap: Lights {
    private var store: [[Bool]]
    
    init(width: Int, height: Int) {
        store = Array(repeating: Array(repeating: false, count: width), count: height)
    }
    
    var countLit: Int {
        return store.map({ $0.count(where: identity)}).sum()
    }
    
    mutating func turnOnAll(in rect: Rect) {
        mapInPlace(in: rect, using: { $0 = true })
    }
    
    mutating func turnOffAll(in rect: Rect) {
        mapInPlace(in: rect, using: { $0 = false })
    }
    
    mutating func toggleAll(in rect: Rect) {
        mapInPlace(in: rect, using: { $0.toggle() })
    }
    
    private mutating func mapInPlace(in rect: Rect, using f: ((inout Bool) -> Void)) {
        for y in rect.y1...rect.y2 {
            for x in rect.x1...rect.x2 {
                f(&store[y][x])
            }
        }
    }
}

private struct CrazyPixmap: Lights {
    private var store: [[Int]]
    
    init(width: Int, height: Int) {
        store = Array(repeating: Array(repeating: 0, count: width), count: height)
    }

    var countLit: Int {
        return store.map({ $0.sum() }).sum()
    }
    
    mutating func turnOnAll(in rect: Rect) {
        mapInPlace(in: rect, using: { $0 += 1})
    }

    mutating func turnOffAll(in rect: Rect) {
        mapInPlace(in: rect, using: { $0 = max(0, $0 - 1) })
    }
    
    mutating func toggleAll(in rect: Rect) {
        mapInPlace(in: rect, using: { $0 += 2 })
    }

    private mutating func mapInPlace(in rect: Rect, using f: ((inout Int) -> Void)) {
        for y in rect.y1...rect.y2 {
            for x in rect.x1...rect.x2 {
                f(&store[y][x])
            }
        }
    }
}
