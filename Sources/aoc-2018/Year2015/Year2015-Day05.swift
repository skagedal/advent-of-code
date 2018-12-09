import Foundation

extension Year2015 {
    struct Day05: AdventDay2015 {
        let day = 5

        func answerToFirstPart(_ data: Data) throws -> String {
            return data.lines.count(where: isNice).toString
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            throw AdventError.unimplemented
        }

        let knownAnswerToFirstPart = "258"
    }
}

private func isNice(_ string: String) -> Bool {
    let sequencesOfTwo = string.subsequences(ofLength: 2)
    return
        string.count(where: isVowel) >= 3 &&
            sequencesOfTwo.contains(where: isRepeated) &&
            !sequencesOfTwo.contains(where: { badSubstrings.contains(String($0)) })
}


private let vowels: Set<Character> = Set("aeiou")
private let badSubstrings: Set<String> = ["ab", "cd", "pq", "xy"]

private func isVowel(_ character: Character) -> Bool {
    return vowels.contains(character)
}
