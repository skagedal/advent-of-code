import Foundation

struct Day07: AdventDay2018 {
    let day = 7
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let edges = SleighInstructionsDecoder().decode(data)
        let dag = DirectedGraph(acyclicEdges: edges)
        return(dag.topologicallySorted(by: <).joined())
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        let edges = SleighInstructionsDecoder().decode(data)
        let elfScheduler = ElfScheduler(edges: edges, numberOfWorkers: 5, timeOffset: 60)
        return "\(elfScheduler.scheduledSeconds())"
    }
    
    func answerToExampleForSecondPart(_ data: Data) throws -> String {
        let edges = SleighInstructionsDecoder().decode(data)
        let elfScheduler = ElfScheduler(edges: edges, numberOfWorkers: 2, timeOffset: 0)
        return "\(elfScheduler.scheduledSeconds())"
    }
    
    let knownAnswerToExampleForFirstPart = "CABDFE"
    let knownAnswerToFirstPart = "LFMNJRTQVZCHIABKPXYEUGWDSO"
    let knownAnswerToExampleForSecondPart = "15"
    let knownAnswerToSecondPart = "1180"
}

private class ElfScheduler {
    typealias Node = String
    typealias Edge = (from: Node, to: Node)

    class Worker {
        var node: Node?
        var secondsLeft: Int = 0
    }
    
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
        let edgesFrom = Dictionary(grouping: edges, by: first)
        let edgesTo = Dictionary(grouping: edges, by: second)
        
        self.edgesFrom = edgesFrom
        self.edgesTo = edgesTo
        self.nodesReadyToRun = allNodes.filter({ edgesTo[$0] == nil })
    }
    
    func scheduledSeconds() -> Int {
        var elapsed = 0
        while !nodesReadyToRun.isEmpty {
            scheduleWork()
            elapsed += processShortestPeriod()
        }
        return elapsed
    }
    
    private func scheduleWork() {
        for worker in workers {
            if worker.node == nil {
                worker.node = assignTask()
                if let node = worker.node {
                    worker.secondsLeft = timeForTask(node)
                } else {
                    worker.secondsLeft = 0
                }
            }
        }
    }
    
    private func processShortestPeriod() -> Int {
        let period = workers.filter({ $0.node != nil }).map(^\.secondsLeft).min()!
        for worker in workers {
            if let node = worker.node {
                worker.secondsLeft -= period
                assert(worker.secondsLeft >= 0)
                if worker.secondsLeft == 0 {
                    completeTask(node)
                    worker.node = nil
                }
            }
        }
        return period
    }
    
    private func assignTask() -> Node? {
        let unassignedNodes = nodesReadyToRun.filter({ node in
            !self.workers.contains(where: { $0.node == node })
        })
        return unassignedNodes.min()
    }
    
    private func timeForTask(_ node: Node) -> Int {
        return Int(node.utf8.first! - ASCII.A) + 1 + timeOffset
    }
    
    private func completeTask(_ node: Node) {
        nodesReadyToRun.remove(node)
        for edge in edgesFrom[node, default: []] {
            removeEdge(edge)
            if edgesTo[edge.to].isEmptyOrNil {
                nodesReadyToRun.insert(edge.to)
            }
        }
    }
    
    private func removeEdge(_ edge: Edge) {
        edgesFrom[edge.from]!.removeAll(where: { $0 == edge })
        edgesTo[edge.to]!.removeAll(where: { $0 == edge })
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
