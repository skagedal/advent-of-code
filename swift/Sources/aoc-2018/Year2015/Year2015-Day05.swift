import Foundation

extension Year2015 {
    struct Day05: AdventDay2015 {
        let day = 5

        func answerToFirstPart(_ data: Data) throws -> String {
            return data.lines.filter { s in !s.isEmpty }.count(where: isNiceAccordingToFirstSetOfRules).toString
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            return data.lines.filter { s in !s.isEmpty }.count(where: isNiceAccordingToSecondSetOfRules).toString
        }

        let knownAnswerToFirstPart = "236"
        let knownAnswerToSecondPart = "51"
    }
}

// MARK: Part 1

private func isNiceAccordingToFirstSetOfRules(_ string: String) -> Bool {
    let sequencesOfTwo = string.subsequences(ofLength: 2)
    return
        string.count(where: isElementOf(vowels)) >= 3 &&
            sequencesOfTwo.contains(where: isRepeated) &&
            !sequencesOfTwo.contains(where: { badSubstrings.contains(String($0)) })
}

private let vowels: Set<Character> = Set("aeiou")
private let badSubstrings: Set<String> = ["ab", "cd", "pq", "xy"]

// MARK: Part 2

private func isNiceAccordingToSecondSetOfRules(_ string: String) -> Bool {
    return
        string
            .subsequences(ofLength: 2)
            .trianglePairs()
            .contains(where: { String($0.0) == String($0.1) && !string.substringsOverlap($0.0, $0.1) })
        &&
        string
            .subsequences(ofLength: 3)
            .contains(where: isPalindromic)
}
