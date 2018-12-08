import Foundation

struct DirectedGraph<Node> where Node: Hashable {
    typealias Edge = (Node, Node)
    fileprivate let edgesFrom: [Node: [Edge]]
    fileprivate let edgesTo: [Node: [Edge]]
    fileprivate let nodes: Set<Node>

    init<S>(acyclicEdges: S) where S: Sequence, S.Element == (Node, Node) {
        edgesFrom = Dictionary(grouping: acyclicEdges, by: first)
        edgesTo = Dictionary(grouping: acyclicEdges, by: second)
        nodes = Set(acyclicEdges.map(first) + acyclicEdges.map(second))
    }
    
    func topologicallySorted(by isLessThan: @escaping ((Node, Node) -> Bool)) -> TopologicallySortedDirectedGraphSequence<Node> {
        return TopologicallySortedDirectedGraphSequence(self, sortedBy: isLessThan)
    }
}

struct TopologicallySortedDirectedGraphSequence<Node>: Sequence where Node: Hashable {
    typealias Element = Node
    typealias Edge = (from: Node, to: Node)
    
    struct Iterator: IteratorProtocol {
        private var edgesFrom: [Node: [Edge]]
        private var edgesTo: [Node: [Edge]]
        private let isLessThan: ((Node, Node) -> Bool)
        private var nodes: Set<Node>
        
        init(_ sequence: TopologicallySortedDirectedGraphSequence<Node>) {
            self.edgesFrom = sequence.graph.edgesFrom
            self.edgesTo = sequence.graph.edgesTo
            self.isLessThan = sequence.isLessThan
            self.nodes = sequence.graph.nodes.filter({ sequence.graph.edgesTo[$0] == nil })
        }

        mutating func next() -> Node? {
            guard let node = nodes.min(by: isLessThan) else {
                return nil
            }
            nodes.remove(node)
            for edge in edgesFrom[node, default: []] {
                removeEdge(edge)
                if edgesTo[edge.to].isEmptyOrNil {
                    nodes.insert(edge.to)
                }
            }
            return node
        }
        
        private mutating func removeEdge(_ edge: Edge) {
            edgesFrom[edge.from]!.removeAll(where: { $0 == edge })
            edgesTo[edge.to]!.removeAll(where: { $0 == edge })
        }
    }
    
    private let graph: DirectedGraph<Node>
    private let isLessThan: ((Node, Node) -> Bool)
    
    fileprivate init(_ graph: DirectedGraph<Node>, sortedBy isLessThan: @escaping ((Node, Node) -> Bool)) {
        self.graph = graph
        self.isLessThan = isLessThan
    }
    
    func makeIterator() -> TopologicallySortedDirectedGraphSequence<Node>.Iterator {
        return Iterator(self)
    }
}
