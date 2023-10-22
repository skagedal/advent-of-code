import Foundation

extension Year2015 {
    struct Day02: AdventDay2015 {
        let day = 2

        func answerToFirstPart(_ data: Data) throws -> String {            
            let feet: Int = Array(data.integers)
                .grouping(count: 3)
                .map({
                    let d = $0.sorted()
                    return d[0] * d[1] * 3 + d[0] * d[2] * 2 + d[1] * d[2] * 2
                }).sum()
            return feet.toString
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            let feet: Int = Array(data.integers)
                .grouping(count: 3)
                .map({
                    let d = $0.sorted()
                    return d[0] * 2 + d[1] * 2 + d[0] * d[1] * d[2]
                }).sum()
            return feet.toString
        }
        
        let knownAnswerToFirstPart = "1586300"
        let knownAnswerToSecondPart = "3737498"
    }
}
