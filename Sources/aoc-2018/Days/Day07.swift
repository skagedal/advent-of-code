import Foundation

struct Day07: AdventDay {
    let day = 7
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let regex = try RegularExpression(pattern: "^.* ([A-Z]) .* ([A-Z]) .*$")
        let edges: [(String, String)] = data.lines.map({
            let ranges = regex.firstMatch(in: $0)!.ranges()
            return (ranges[1], ranges[2])
        })
        let dag = DirectedGraph(acyclicEdges: edges)
        return(dag.topologicallySorted(by: <).joined())
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        throw AdventError.unimplemented
    }
}
