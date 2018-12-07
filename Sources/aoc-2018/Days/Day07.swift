import Foundation

struct Day07: AdventDay {
    let day = 7
    let knownAnswerToExampleForFirstPart = "CABDFE"
    let knownAnswerToFirstPart = "CFGHAEMNBPRDISVWQUZJYTKLOX"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let edges = SleighInstructionsDecoder().decode(data)
        let dag = DirectedGraph(acyclicEdges: edges)
        return(dag.topologicallySorted(by: <).joined())
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        throw AdventError.unimplemented
    }
    
    func answerToSecondPartExampleVariant(_ data: Data) throws -> String {
        return try answerToSecondPart(data)
    }
}

private class ElfScheduler {
    private let edges: [(String, String)]
    private let numberOfWorkers: Int
    private let timeOffset:Int
    
    init(edges: [(String, String)], numberOfWorkers: Int, timeOffset: Int) {
        self.edges = edges
        self.numberOfWorkers = numberOfWorkers
        self.timeOffset = timeOffset
    }
}

private class SleighInstructionsDecoder {
    private lazy var regex = try! RegularExpression(pattern: "^.* ([A-Z]) .* ([A-Z]) .*$")
    
    func decode(_ data: Data) -> [(String, String)] {
        return data
            .lines
            .map({ regex
                        .firstMatch(in: $0)!
                        .ranges()
                        .dropFirst()
                        .firstTwo
            })
    }
}

extension Collection {
    var firstTwo: (Element, Element) {
        var it = makeIterator()
        return (it.next()!, it.next()!)
    }
    
    var firstThree: (Element, Element, Element) {
        var it = makeIterator()
        return (it.next()!, it.next()!, it.next()!)
    }
}
