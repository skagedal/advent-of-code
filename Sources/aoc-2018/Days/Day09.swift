import Foundation

struct Day09: AdventDay2018 {
    let day = 9
    
    func answerToFirstPart(_ data: Data) throws -> String {
        let input = Array(data.integers)
        return playGame(players: input[0], lastMarble: input[1]).toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        let input = Array(data.integers)
        return playGame(players: input[0], lastMarble: input[1] * 100).toString
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
    
    let knownAnswerToExampleForFirstPart = "8317"
    let knownAnswerToFirstPart = "437654"
}
