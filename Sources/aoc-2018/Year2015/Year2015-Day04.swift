import Foundation
import CommonCrypto

extension Year2015 {
    struct Day04: AdventDay2015 {
        let day = 4

        func answerToFirstPart(_ data: Data) throws -> String {
            let prefix = String(data: data, encoding: .utf8)!
            let fiveZeros = "00000"
            for int in 0...Int.max {
                let string = "\(prefix)\(int)"
                let md5 = MD5(string: string).hexEncodedString()
                if md5.prefix(5) == fiveZeros {
                    return "\(int)"
                }
            }
            throw AdventError.noAnswerFound
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            throw AdventError.unimplemented
        }

        let knownAnswerToExampleForFirstPart = "609043"
        let knownAnswerToFirstPart = "117946"
    }
}


