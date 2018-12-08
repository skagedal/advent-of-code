import Foundation

extension Year2015 {
    struct Day01: AdventDay2015 {
        let day = 1

        func answerToFirstPart(_ data: Data) throws -> String {
            return (data.count(where: equals(ASCII.leftParenthesis)) -
                    data.count(where: equals(ASCII.rightParenthesis))).toString
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            var floor = 0
            return (data.firstIndex(where: { c in
                if c == ASCII.leftParenthesis {
                    floor += 1
                } else if c == ASCII.rightParenthesis {
                    floor -= 1
                }
                return floor == -1
            })! + 1).toString
        }
        
        let knownAnswerToFirstPart = "138"
        let knownAnswerToSecondPart = "1771"
    }
}
