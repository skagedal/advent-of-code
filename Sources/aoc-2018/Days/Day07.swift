import Foundation

struct Day07: AdventDay {
    let day = 7
    let knownAnswerToExampleForFirstPart = "CABDFE"
    let knownAnswerToFirstPart = "CFGHAEMNBPRDISVWQUZJYTKLOX"
    let knownAnswerToExampleForSecondPart = "15"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let edges = SleighInstructionsDecoder().decode(data)
        let dag = DirectedGraph(acyclicEdges: edges)
        return(dag.topologicallySorted(by: <).joined())
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        throw AdventError.unimplemented
        let edges = SleighInstructionsDecoder().decode(data)
        let elfScheduler = ElfScheduler(edges: edges, numberOfWorkers: 5, timeOffset: 60)
        return "\(elfScheduler.scheduledSeconds())"
    }
    
    func answerToSecondPartExampleVariant(_ data: Data) throws -> String {
        throw AdventError.unimplemented
        let edges = SleighInstructionsDecoder().decode(data)
        let elfScheduler = ElfScheduler(edges: edges, numberOfWorkers: 2, timeOffset: 0)
        return "\(elfScheduler.scheduledSeconds())"
    }
}

private class ElfScheduler {
    
    
    struct Task {
        var name: String
        var secondsLeft: Int
    }
    
    class Worker {
        var task: Task?
        
    }
    
    typealias Node = String
    typealias Edge = (Node, Node)
    private let edges: [Edge]
    private let timeOffset:Int
    
    private var workers: Array<Worker>
    private var edgesFrom: [Node: [Edge]]
    private var edgesTo: [Node: [Edge]]
    private var nodesReadyToRun: Set<Node>
    
    init(edges: [(String, String)], numberOfWorkers: Int, timeOffset: Int) {
        self.edges = edges
        self.timeOffset = timeOffset
        self.workers = Array(count: numberOfWorkers, createdBy: Worker.init)

        let allNodes = Set(edges.map(first) + edges.map(second))
        
        // Continue here...
        nodesReadyToRun = []
        edgesFrom = [:]
        edgesTo = [:]
    }
    
    func scheduledSeconds() -> Int {
        
        return 0
    }
    
    private func scheduleWork() {
        var edgesFrom = Dictionary(grouping: edges, by: first)
        var edgesTo = Dictionary(grouping: edges, by: second)


        for worker in workers {
            if worker.task == nil {
                worker.task = assignTask()
            }
        }
    }
    
    private func assignTask() -> Task? {
        return Task(name: "X", secondsLeft: 3)
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
