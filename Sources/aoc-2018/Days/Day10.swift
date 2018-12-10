import Foundation

struct Day10: AdventDay2018 {
    let day = 10
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let stars = data
            .integers
            .array
            .grouping(count: 4)
            .map(Star.init)
        
        var i = 0
        while true {
            for star in stars {
                star.move()
            }
            i += 1
            if stars.aligned > 15 {
                print("\(i). Aligned: \(stars.aligned)")
                stars.printMap()
                if let line = readLine(strippingNewline: true), line == "q" {
                    return "done"
                }
            }
        }
    }
    
    
    
    func answerToSecondPart(_ data: Data) throws -> String {
        return "done"
    }
}

private class Star {
    var x: Int
    var y: Int
    var dx: Int
    var dy: Int
    
    init(_ ints: [Int]) {
        (x, y, dx, dy) = ints.firstFour
    }
    
    func move() {
        x += dx
        y += dy
    }
}

private extension Array where Element == Star {
    var aligned: Int {
        return Dictionary(grouping: self, by: ^\.y).values.map(^\.count).max()!
    }
    
    func printMap() {
        let minx = map(^\.x).min()!
        let miny = map(^\.y).min()!
        let maxx = map(^\.x).max()!
        let maxy = map(^\.y).max()!
        for y in miny...maxy {
            print((minx...maxx).map({ x in contains(where: { $0.x == x && $0.y == y }) ? "X" : " "}).joined())
        }
    }
}
