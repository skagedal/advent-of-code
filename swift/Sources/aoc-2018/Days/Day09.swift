import Foundation

struct Day09: AdventDay2018 {
    let day = 9
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let input = Array(data.integers)
        return playFastGame(players: input[0], lastMarble: input[1]).toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        let input = Array(data.integers)
        return playFastGame(players: input[0], lastMarble: input[1] * 100).toString
    }
    
    private func playGame(players: Int, lastMarble: Int) -> Int {
        var board: [Int] = [0]
        var currentIndex = 0
        var currentPlayer = 0
        var points = Array(repeating: 0, count: players)
        for marble in 1...lastMarble {
            if marble % 23 == 0 {
                currentIndex = (currentIndex + board.count - 7) % board.count
                points[currentPlayer] += marble + board.remove(at: currentIndex)
            } else {
                currentIndex = (currentIndex + 1) % board.count + 1
                board.insert(marble, at: currentIndex)
            }
            currentPlayer = (currentPlayer + 1) % players
        }
        return points.max()!
    }
    
    private func playFastGame(players: Int, lastMarble: Int) -> Int {
        let board = List<Int>()
        var currentNode = board.addLast(0)
        var points = Array(repeating: 0, count: players)
        var currentPlayer = 0
        for marble in 1...lastMarble {
            if marble % 23 == 0 {
                let removedNode = board.back(7, from: currentNode)
                currentNode = board.forward(1, from: removedNode)
                board.remove(removedNode)
                points[currentPlayer] += marble + removedNode.element
            } else {
                currentNode = board.insert(marble, before: board.forward(2, from: currentNode))
            }
            currentPlayer = (currentPlayer + 1) % players
        }
        return points.max()!
    }
    
    let knownAnswerToExampleForFirstPart = "8317"
    let knownAnswerToFirstPart = "429287"
    let knownAnswerToSecondPart = "3624387659"
}

class List<Element> {
    class Node {
        var element: Element
        var previous: Node?
        var next: Node?
        
        init(_ element: Element) {
            self.element = element
        }
    }
    var first: Node?
    var last: Node?
    
    func addLast(_ element: Element) -> Node {
        let node = Node(element)
        if let last = last {
            last.next = node
            node.previous = last
            self.last = node
            return node
        } else {
            self.first = node
            self.last = node
            return node
        }
    }
    
    func insert(_ element: Element, before nextNode: Node) -> Node {
        let node = Node(element)
        node.previous = nextNode.previous
        node.next = nextNode
        nextNode.previous?.next = node
        nextNode.previous = node
        
        if node.previous == nil {
            first = node
        }
        return node
    }
    
    func remove(_ node: Node) {
        node.previous?.next = node.next
        node.next?.previous = node.previous
        if node.next?.previous == nil {
            first = node.next
        }
        if node.previous?.next == nil {
            last = node.previous
        }
    }
    
    func back(_ n: Int, from node: Node) -> Node {
        var current = node
        for _ in 0..<n {
            if let previous = current.previous {
                current = previous
            } else {
                current = last!
            }
        }
        return current
    }
    
    func forward(_ n: Int, from node: Node) -> Node {
        var current = node
        for _ in 0..<n {
            if let next = current.next {
                current = next
            } else {
                current = first!
            }
        }
        return current
    }
    
    func printList() {
        print("[", terminator: "")
        var node = first
        while let thisNode = node {
            print(thisNode.element, terminator: ", ")
            node = thisNode.next
        }
        print("]")
    }
}
